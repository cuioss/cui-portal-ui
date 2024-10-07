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
package de.cuioss.portal.ui.runtime.application.listener.metrics;

import de.cuioss.jsf.api.common.view.ViewDescriptor;
import de.cuioss.portal.ui.api.context.CuiCurrentView;
import de.cuioss.tools.logging.CuiLogger;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.event.PhaseId;
import jakarta.inject.Inject;
import jakarta.inject.Provider;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.metrics.MetricRegistry;
import org.eclipse.microprofile.metrics.Tag;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static de.cuioss.portal.configuration.MetricsConfigKeys.PORTAL_METRICS_ENABLED;
import static de.cuioss.tools.collect.CollectionLiterals.mutableList;

/**
 * Gathers all lifecycle-information regarding tracing of certain Phases.
 *
 * @author Oliver Wolff
 *
 */
@RequestScoped
public class RequestTracer implements Serializable {

    public static final String PROCESSING_IDENTIFIER = "Processing of view '";

    static final String NO_TRACING_INFORMATION_AVAILABLE = "No tracing information available";

    private static final CuiLogger log = new CuiLogger(RequestTracer.class);

    @Serial
    private static final long serialVersionUID = 2706209506496310554L;

    private final Map<Integer, PhaseTracer> tracerMap = new HashMap<>();

    private final PhaseTracer complete = new PhaseTracer(PhaseId.ANY_PHASE);

    private static final String METRIC_ID_PHASE = "jsf.view.phase";
    private static final String METRIC_ID_TOTAL = "jsf.view.total";

    @Inject
    @CuiCurrentView
    private Provider<ViewDescriptor> currentViewProvider;

    /**
     * The default value is explicitly set here, because the actual configuration of
     * that value is set by the "portal-metrics" module, which is optional.
     */
    @Inject
    @ConfigProperty(name = PORTAL_METRICS_ENABLED, defaultValue = "false")
    private boolean metricsEnabled;

    @Inject
    private Provider<MetricRegistry> metricRegistry;

    /**
     * Starts the tracing of a certain phase.
     *
     * @param id identifying the phase. Must not be null. {@link PhaseId#ANY_PHASE}
     *           will be ignored
     */
    public void start(PhaseId id) {
        if (0 == id.getOrdinal()) {
            log.trace("Ignoring any-phase");
            return;
        }
        if (PhaseId.RESTORE_VIEW.equals(id)) {
            log.trace("Using RESTORE_VIEW as starting point for measuring the full processing");
            complete.start();
            startMetricTotal();
        }
        tracerMap.put(id.getOrdinal(), new PhaseTracer(id).start());
        startMetricPhase(id);
    }

    /**
     * Stops the tracing of a certain phase if applicable.
     *
     * @param id identifying the phase. Must not be null. It solely stops elements
     *           that have been initially started
     */
    public void stop(PhaseId id) {
        if (PhaseId.RENDER_RESPONSE.equals(id)) {
            log.trace("Using RENDER_RESPONSE as termination for measuring the full processing");
            complete.stop();
            stopMetricTotal();
        }
        if (!tracerMap.containsKey(id.getOrdinal())) {
            log.trace("Tracer for Phase '%s' is not present, ignoring call", id);
            return;
        }
        tracerMap.get(id.getOrdinal()).stop();
        stopMetricPhase(id);
    }

    /**
     * Writes / persists statistics, depending on the configuration. It will be
     * implicitly logged at debug-level. Later on it will be written to the
     * Metrics-registry
     */
    public void writeStatistics() {
        if (log.isDebugEnabled()) {
            var builder = new StringBuilder();
            builder.append("\n").append(PROCESSING_IDENTIFIER).append(determineCurrentView()).append("'");
            if (!complete.isDidRun()) {
                builder.append("\n").append(NO_TRACING_INFORMATION_AVAILABLE);
            } else {
                builder.append(", took ").append(complete.getStopWatch().toString());
                List<PhaseTracer> tracer = mutableList(tracerMap.values());
                Collections.sort(tracer);
                List<String> elements = mutableList();
                var elapsedComplete = complete.getStopWatch().elapsed(TimeUnit.MILLISECONDS);
                elements.add("0-Request Total: " + elapsedComplete);
                tracer.forEach(phase -> phase.addTimeToList(elements));
                log.trace("Gathering differential timings");
                for (PhaseTracer phaseTracer : tracer) {
                    elapsedComplete = elapsedComplete - phaseTracer.getStopWatch().elapsed(TimeUnit.MILLISECONDS);
                }
                elements.add("Delta: " + elapsedComplete);
                builder.append("\nTimings in ms: ").append(elements);
            }
            log.debug(builder.toString());
        }
    }

    private String determineCurrentView() {
        var currentView = currentViewProvider.get();
        if (currentView.isViewDefined()) {
            return currentView.getLogicalViewId();
        }
        return "unknown";
    }

    void startMetricTotal() {
        if (metricsEnabled) {
            metricRegistry.get().timer(METRIC_ID_TOTAL, new Tag("view", determineCurrentView())).time();
        }
    }

    void stopMetricTotal() {
        if (metricsEnabled) {
            metricRegistry.get().timer(METRIC_ID_TOTAL, new Tag("view", determineCurrentView())).time().stop();
        }
    }

    void startMetricPhase(final PhaseId id) {
        if (metricsEnabled) {
            metricRegistry.get()
                    .timer(METRIC_ID_PHASE, new Tag("phase", id.getName()), new Tag("view", determineCurrentView()))
                    .time();
        }
    }

    void stopMetricPhase(final PhaseId id) {
        if (metricsEnabled) {
            metricRegistry.get()
                    .timer(METRIC_ID_PHASE, new Tag("phase", id.getName()), new Tag("view", determineCurrentView()))
                    .time().stop();
        }
    }
}
