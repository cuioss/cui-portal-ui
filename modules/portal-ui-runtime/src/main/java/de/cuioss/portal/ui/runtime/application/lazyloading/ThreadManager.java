package de.cuioss.portal.ui.runtime.application.lazyloading;

import static de.cuioss.portal.configuration.PortalConfigurationKeys.PORTAL_LAZYLOADING_REQUEST_HANDLE_TIMEOUT;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Provider;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import de.cuioss.portal.configuration.PortalConfigurationChangeEvent;
import de.cuioss.portal.configuration.PortalConfigurationKeys;
import de.cuioss.portal.configuration.initializer.ApplicationInitializer;
import de.cuioss.portal.configuration.initializer.PortalInitializer;
import de.cuioss.tools.logging.CuiLogger;

/**
 * Manages FutureHandle based requests and executes them via
 * {@link Executors#newCachedThreadPool()}. All requests that were not retrieved
 * after
 * {@link PortalConfigurationKeys#PORTAL_LAZYLOADING_REQUEST_HANDLE_TIMEOUT}
 * seconds are canceled and removed.
 */
@ApplicationScoped
@PortalInitializer
public class ThreadManager implements ApplicationInitializer {

    /**
     * Milliseconds to sleep between a cleanup cycle.
     */
    private static final int CLEANUP_SLEEP = 1000;

    private static final CuiLogger log = new CuiLogger(ThreadManager.class);

    @Inject
    @ConfigProperty(name = PORTAL_LAZYLOADING_REQUEST_HANDLE_TIMEOUT)
    private Provider<Integer> requestHandleTimeoutProvider;

    private int requestHandleTimeout;

    private ExecutorService executorService;

    /**
     * To stop the cleanup worker thread.
     */
    private boolean executorRunning = false;

    private final Map<Long, FutureHandle> registry = new HashMap<>();

    /**
     * Store and start a {@link Callable} task.
     *
     * @param id      a unique id to store and #retrieve(long) the task afterwards
     * @param task    the task to execute
     * @param context a context object
     */
    public void store(final long id, final Callable<?> task, final Object context) {
        synchronized (registry) {
            var timestamp = System.currentTimeMillis();
            log.debug(() -> "task " + id + " added at time (ms): " + timestamp);
            registry.put(id, new FutureHandle(executorService.submit(task), context, timestamp));
        }
    }

    /*
     * private Callable<?> propagateMdcToCallable(final Callable<?> task) { final
     * Map<String, String> context = ThreadContext.getContext(); return () -> { Map
     * previous = ThreadContext.getContext(); if (context == null) {
     * ThreadContext.clearAll();clear(); } else {
     * ThreadContext.setContextMap(context); } try { return task.call(); } finally {
     * if (previous == null) { ThreadContext.clear(); } else {
     * ThreadContext.setContextMap(previous); } } }; }
     */

    /**
     * Retrieve a task that was {@link #store(long, Callable, Object)}ed before.
     *
     * @param id the unique id
     * @return a {@link FutureHandle} containing the task and the context object
     */
    FutureHandle retrieve(final long id) {
        synchronized (registry) {
            log.debug(() -> "task retrieved and removed: " + id);
            return registry.remove(id);
        }
    }

    @Override
    public void initialize() {
        log.debug("Starting ThreadManager");
        if (null == requestHandleTimeoutProvider || null == requestHandleTimeoutProvider.get()) {
            throw new IllegalStateException(
                    "Invalid configuration, please check property " + PORTAL_LAZYLOADING_REQUEST_HANDLE_TIMEOUT);
        }
        requestHandleTimeout = requestHandleTimeoutProvider.get();
        log.trace("requestHandleTimeout={}", requestHandleTimeout);
        executorService = Executors.newCachedThreadPool();
        executorService.execute(cleanupExecutor());
        executorRunning = true;
    }

    @Override
    public void destroy() {
        log.debug("Shutting down ThreadManager");
        executorRunning = false;
        if (null != executorService) {
            executorService.shutdownNow();
        }
    }

    @Override
    public Integer getOrder() {
        return ApplicationInitializer.ORDER_LATE;
    }

    void configurationChangeEventListener(
            @Observes @PortalConfigurationChangeEvent final Map<String, String> deltaMap) {
        if (deltaMap.containsKey(PORTAL_LAZYLOADING_REQUEST_HANDLE_TIMEOUT)) {
            requestHandleTimeout = Integer.parseInt(deltaMap.get(PORTAL_LAZYLOADING_REQUEST_HANDLE_TIMEOUT));
            log.debug("Portal-011: Changed configuration of '{}' found, new value is '{}', reconfigure",
                    PORTAL_LAZYLOADING_REQUEST_HANDLE_TIMEOUT, requestHandleTimeout);
        }
    }

    private Runnable cleanupExecutor() {
        return () -> {
            while (executorRunning) {
                try {
                    TimeUnit.MILLISECONDS.sleep(CLEANUP_SLEEP);
                } catch (final InterruptedException e) {
                    log.debug("Interrupted cleanup timout, exiting loop", e);
                    Thread.currentThread().interrupt();
                    break;
                }

                synchronized (registry) {
                    for (final Map.Entry<Long, FutureHandle> entry : registry.entrySet()) {
                        if ((System.currentTimeMillis() - entry.getValue().getTimestamp())
                                / 1000 > requestHandleTimeout) {

                            log.debug("timeout. terminating id={}, future={}", entry.getKey(), entry.getValue());

                            entry.getValue().getFuture().cancel(true);
                            registry.remove(entry.getKey());
                            break;
                        }
                    }
                }
            }
        };
    }
}
