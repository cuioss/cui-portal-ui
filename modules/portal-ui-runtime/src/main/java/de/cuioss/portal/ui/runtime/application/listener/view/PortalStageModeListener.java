package de.cuioss.portal.ui.runtime.application.listener.view;

import javax.annotation.Priority;
import javax.enterprise.context.Dependent;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.apache.deltaspike.core.api.exception.control.event.ExceptionToCatchEvent;

import de.cuioss.jsf.api.common.view.ViewDescriptor;
import de.cuioss.portal.configuration.application.PortalProjectStageProducer;
import de.cuioss.portal.configuration.common.PortalPriorities;
import de.cuioss.portal.ui.api.configuration.PortalNotConfiguredException;
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
 * @author Sven Haag, Sven Haag
 */
@PortalRestoreViewListener(PhaseExecution.AFTER_PHASE)
@Dependent
@Priority(PortalPriorities.PORTAL_CORE_LEVEL + 5) // Must be called before other listeners
@EqualsAndHashCode(of = "stageProducer")
@ToString(of = "stageProducer")
public class PortalStageModeListener implements ViewListener {

    private static final long serialVersionUID = 8427405526881056257L;

    @Inject
    @PortalProjectStageProducer
    private CuiProjectStage stageProducer;

    @Inject
    private Event<ExceptionToCatchEvent> catchEvent;

    @Override
    public void handleView(final ViewDescriptor viewDescriptor) {
        if (stageProducer.isConfiguration()) {
            this.catchEvent.fire(new ExceptionToCatchEvent(new PortalNotConfiguredException()));
        }
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
