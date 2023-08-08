package de.cuioss.portal.ui.runtime.application.listener.view;

import javax.annotation.Priority;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import de.cuioss.jsf.api.common.view.ViewDescriptor;
import de.cuioss.portal.configuration.application.PortalProjectStageProducer;
import de.cuioss.portal.configuration.common.PortalPriorities;
import de.cuioss.portal.ui.api.configuration.PortalNotConfiguredException;
import de.cuioss.portal.ui.api.exception.ExceptionAsEvent;
import de.cuioss.portal.ui.api.listener.view.PhaseExecution;
import de.cuioss.portal.ui.api.listener.view.PortalRestoreViewListener;
import de.cuioss.portal.ui.api.listener.view.ViewListener;
import de.cuioss.uimodel.application.CuiProjectStage;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * This listener checks the portal stage configuration. In case the portal stage
 * is 'configuration' it will fire an {@link PortalNotConfiguredException}.
 *
 * @author Sven Haag
 */
@PortalRestoreViewListener(PhaseExecution.AFTER_PHASE)
@RequestScoped
@Priority(PortalPriorities.PORTAL_CORE_LEVEL + 5) // Must be called before other listeners
@EqualsAndHashCode(of = "stageProducer")
@ToString(of = "stageProducer")
public class PortalStageModeListener implements ViewListener {

    private static final long serialVersionUID = 8427405526881056257L;

    @Inject
    @PortalProjectStageProducer
    private CuiProjectStage stageProducer;

    @Inject
    private Event<ExceptionAsEvent> catchEvent;

    @Override
    public void handleView(final ViewDescriptor viewDescriptor) {
        if (stageProducer.isConfiguration()) {
            this.catchEvent.fire(new ExceptionAsEvent(new PortalNotConfiguredException()));
        }
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
