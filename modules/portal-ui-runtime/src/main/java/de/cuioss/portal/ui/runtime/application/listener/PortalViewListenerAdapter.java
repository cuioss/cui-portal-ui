package de.cuioss.portal.ui.runtime.application.listener;

import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

import de.cuioss.portal.core.cdi.PortalBeanManager;
import de.cuioss.portal.ui.api.listener.view.PortalRestoreViewListener;
import de.cuioss.portal.ui.runtime.application.listener.view.PortalCDIViewListener;

/**
 * Replacement for deltaspike phase listener. In essence it routes the
 * corresponding calls to {@link PortalRestoreViewListener}
 */
public class PortalViewListenerAdapter implements PhaseListener {

    private static final String UNABLE_TO_ACCESS_PORTAL_CDI_VIEW_LISTENER = "Unable to access PortalCDIViewListener";
    private static final long serialVersionUID = -4417626731612443835L;

    @Override
    public void afterPhase(PhaseEvent event) {
        PortalBeanManager.resolveBean(PortalCDIViewListener.class, null)
                .orElseThrow(() -> new IllegalStateException(UNABLE_TO_ACCESS_PORTAL_CDI_VIEW_LISTENER))
                .afterPhase(event);

    }

    @Override
    public void beforePhase(PhaseEvent event) {
        PortalBeanManager.resolveBean(PortalCDIViewListener.class, null)
                .orElseThrow(() -> new IllegalStateException(UNABLE_TO_ACCESS_PORTAL_CDI_VIEW_LISTENER))
                .beforePhase(event);

    }

    @Override
    public PhaseId getPhaseId() {
        return PhaseId.RESTORE_VIEW;
    }

}
