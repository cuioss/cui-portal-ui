/*
 * Copyright 2023 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * https://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.cuioss.portal.ui.runtime.application.lazyloading;

import de.cuioss.portal.configuration.PortalConfigurationKeys;
import de.cuioss.tools.logging.CuiLogger;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Provider;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.context.ManagedExecutor;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static de.cuioss.portal.configuration.PortalConfigurationKeys.PORTAL_LAZY_LOADING_REQUEST_HANDLE_TIMEOUT;

/**
 * Manages FutureHandle based requests and executes them via
 * {@link Executors#newCachedThreadPool()}. All requests that were not retrieved
 * after
 * {@link PortalConfigurationKeys#PORTAL_LAZY_LOADING_REQUEST_HANDLE_TIMEOUT}
 * seconds are canceled and removed.
 */
@ApplicationScoped
public class ThreadManager {

    /**
     * Milliseconds to sleep between a cleanup cycle.
     */
    private static final int CLEANUP_SLEEP = 1000;

    private static final CuiLogger LOGGER = new CuiLogger(ThreadManager.class);

    @Inject
    @ConfigProperty(name = PORTAL_LAZY_LOADING_REQUEST_HANDLE_TIMEOUT)
    private Provider<Integer> requestHandleTimeoutProvider;

    private int requestHandleTimeout;

    private ExecutorService executorService;

    /**
     * To stop the cleanup worker thread.
     */
    private boolean executorRunning = false;

    private final Map<String, FutureHandle> registry = new HashMap<>();

    /**
     * Store and start a {@link Callable} task.
     *
     * @param id      a unique id to store and #retrieve(long) the task afterward
     * @param task    the task to execute
     * @param context a context object
     */
    public void store(final String id, final Callable<?> task, final Object context) {
        synchronized (registry) {
            var timestamp = System.currentTimeMillis();
            LOGGER.debug(() -> "task " + id + " added at time (ms): " + timestamp);
            registry.put(id, new FutureHandle(executorService.submit(task), context, timestamp));
        }
    }

    /**
     * Retrieve a task that was {@link #store(long, Callable, Object)}ed before.
     *
     * @param id the unique id
     * @return a {@link FutureHandle} containing the task and the context object
     */
    FutureHandle retrieve(final String id) {
        synchronized (registry) {
            LOGGER.debug("task retrieved and removed: '%s'", id);
            return registry.remove(id);
        }
    }

    @PostConstruct
    public void initialize() {
        LOGGER.debug("Starting ThreadManager");
        if (null == requestHandleTimeoutProvider || null == requestHandleTimeoutProvider.get()) {
            throw new IllegalStateException(
                "Invalid configuration, please check property " + PORTAL_LAZY_LOADING_REQUEST_HANDLE_TIMEOUT);
        }
        requestHandleTimeout = requestHandleTimeoutProvider.get();
        LOGGER.trace("requestHandleTimeout={}", requestHandleTimeout);
        executorService = ManagedExecutor.builder().build();
        executorService.execute(cleanupExecutor());
        executorRunning = true;
    }

    @PreDestroy
    public void destroy() {
        LOGGER.debug("Shutting down ThreadManager");
        executorRunning = false;
        if (null != executorService) {
            executorService.shutdownNow();
        }
    }

    private Runnable cleanupExecutor() {
        return () -> {
            while (executorRunning) {
                try {
                    TimeUnit.MILLISECONDS.sleep(CLEANUP_SLEEP);
                } catch (final InterruptedException e) {
                    LOGGER.debug("Interrupted cleanup timout, exiting loop", e);
                    Thread.currentThread().interrupt();
                    break;
                }

                synchronized (registry) {
                    for (final Map.Entry<String, FutureHandle> entry : registry.entrySet()) {
                        if ((System.currentTimeMillis() - entry.getValue().timestamp())
                            / 1000 > requestHandleTimeout) {

                            LOGGER.debug("timeout. terminating id={}, future={}", entry.getKey(), entry.getValue());

                            entry.getValue().future().cancel(true);
                            registry.remove(entry.getKey());
                            break;
                        }
                    }
                }
            }
        };
    }
}
