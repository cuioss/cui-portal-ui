package de.cuioss.portal.ui.runtime.application.listener.view.testhelper;

import javax.annotation.Priority;
import javax.enterprise.context.RequestScoped;

import de.cuioss.portal.ui.api.listener.view.PhaseExecution;
import de.cuioss.portal.ui.api.listener.view.PortalRestoreViewListener;

@SuppressWarnings("javadoc")
@PortalRestoreViewListener(PhaseExecution.BEFORE_PHASE)
@Priority(1)
@RequestScoped
public class BeforeViewListener extends ViewListenerCallRecorder {

    private static final long serialVersionUID = -6341878146268681299L;

}
