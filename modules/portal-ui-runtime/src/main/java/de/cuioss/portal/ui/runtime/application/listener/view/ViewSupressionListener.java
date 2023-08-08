package de.cuioss.portal.ui.runtime.application.listener.view;

import static de.cuioss.portal.configuration.PortalConfigurationKeys.PORTAL_LISTENER_VIEW_SUPRESSION;

import javax.annotation.Priority;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import de.cuioss.jsf.api.common.view.ViewDescriptor;
import de.cuioss.portal.configuration.common.PortalPriorities;
import de.cuioss.portal.ui.api.exception.ExceptionAsEvent;
import de.cuioss.portal.ui.api.listener.view.PhaseExecution;
import de.cuioss.portal.ui.api.listener.view.PortalRestoreViewListener;
import de.cuioss.portal.ui.api.listener.view.ViewListener;
import de.cuioss.portal.ui.runtime.application.configuration.ViewConfiguration;
import de.cuioss.portal.ui.runtime.application.view.ViewSuppressedException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Checks whether the current page is to be suppressed and fires an
 * {@link ViewSuppressedException} if so.
 *
 * @author Oliver Wolff
 */
@PortalRestoreViewListener(PhaseExecution.AFTER_PHASE)
@Priority(PortalPriorities.PORTAL_CORE_LEVEL + 1)
// Must be called before AuthenticatationListener
@RequestScoped
@EqualsAndHashCode(of = "viewConfiguration")
@ToString(of = "viewConfiguration")
public class ViewSupressionListener implements ViewListener {

    private static final long serialVersionUID = -1757867591272259164L;

    @Inject
    private ViewConfiguration viewConfiguration;

    @Inject
    private Event<ExceptionAsEvent> catchEvent;

    @Getter
    @Inject
    @ConfigProperty(name = PORTAL_LISTENER_VIEW_SUPRESSION)
    private boolean enabled;

    @Override
    public void handleView(final ViewDescriptor viewDescriptor) {
        if (this.viewConfiguration.getSuppressedViewMatcher().match(viewDescriptor)) {
            this.catchEvent.fire(new ExceptionAsEvent(new ViewSuppressedException(viewDescriptor)));
        }
    }

}
