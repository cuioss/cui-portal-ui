package de.cuioss.portal.ui.runtime.application.listener;

import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

import de.cuioss.portal.core.cdi.PortalBeanManager;
import de.cuioss.portal.ui.runtime.application.listener.metrics.TraceListener;

/**
 * Replacement for deltaspike phase listener. In essence it routes the
 * corresponding calls to {@link TraceListener}
 */
public class TraceListenerAdapter implements PhaseListener {

    private static final long serialVersionUID = -7081872463546867289L;

    private static final String UNABLE_TO_ACCESS_TRACE_LISTENER = "Unable to access TraceListener";

    @Override
    public void afterPhase(PhaseEvent event) {
        PortalBeanManager.resolveBean(TraceListener.class, null)
                .orElseThrow(() -> new IllegalStateException(UNABLE_TO_ACCESS_TRACE_LISTENER)).afterPhase(event);

    }

    @Override
    public void beforePhase(PhaseEvent event) {
        PortalBeanManager.resolveBean(TraceListener.class, null)
                .orElseThrow(() -> new IllegalStateException(UNABLE_TO_ACCESS_TRACE_LISTENER)).beforePhase(event);

    }

    @Override
    public PhaseId getPhaseId() {
        return PhaseId.ANY_PHASE;
    }

}
