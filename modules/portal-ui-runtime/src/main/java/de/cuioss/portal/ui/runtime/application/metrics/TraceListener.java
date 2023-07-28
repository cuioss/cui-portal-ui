package de.cuioss.portal.ui.runtime.application.metrics;

import static de.cuioss.portal.configuration.PortalConfigurationKeys.PORTAL_LISTENER_TRACE_ENABLED;

import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.inject.Inject;
import javax.inject.Provider;

import org.apache.deltaspike.jsf.api.listener.phase.JsfPhaseListener;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import de.cuioss.portal.configuration.PortalConfigurationKeys;
import de.cuioss.tools.logging.CuiLogger;

/**
 * Uses the deltaspike mechanism for registering a {@link PhaseListener} at the
 * desired location. The very high ordinal of 5000 ensures that the tracing
 * starts before any other listener and will be stopped after all other
 * listeners: deltaspike takes care on this by sorting / reverse sorting. This
 * feature is enabled by the configuration of the key
 * {@link PortalConfigurationKeys#PORTAL_LISTENER_TRACE_ENABLED}. In order to
 * materialize at the log the Logger for {@link RequestTracer} must be set to
 * debug.
 *
 * @author Oliver Wolff
 *
 */
@JsfPhaseListener(ordinal = 5000)
public class TraceListener implements PhaseListener {

    private static final String DISABLED_BY_CONFIGURATION = "Disabled by configuration";

    private static final long serialVersionUID = -3549927752283027321L;

    private static final CuiLogger log = new CuiLogger(TraceListener.class);

    @Inject
    @ConfigProperty(name = PORTAL_LISTENER_TRACE_ENABLED)
    private boolean enabled;

    @Inject
    private Provider<RequestTracer> traceProvider;

    @Override
    public void beforePhase(PhaseEvent event) {
        if (enabled) {
            traceProvider.get().start(event.getPhaseId());
        } else {
            log.trace(DISABLED_BY_CONFIGURATION);
        }
    }

    @Override
    public void afterPhase(PhaseEvent event) {
        if (enabled) {
            var requestTracer = traceProvider.get();
            requestTracer.stop(event.getPhaseId());
            if (PhaseId.RENDER_RESPONSE.equals(event.getPhaseId())) {
                requestTracer.writeStatistics();
            }
        } else {
            log.trace(DISABLED_BY_CONFIGURATION);
        }

    }

    @Override
    public PhaseId getPhaseId() {
        return PhaseId.ANY_PHASE;
    }

}
