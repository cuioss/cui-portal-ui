package de.cuioss.portal.ui.runtime.application.listener.view;

import static de.cuioss.portal.configuration.PortalConfigurationKeys.PORTAL_LISTENER_AUTHORIZATION;

import javax.annotation.Priority;
import javax.enterprise.context.Dependent;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.inject.Provider;

import org.apache.deltaspike.core.api.exception.control.event.ExceptionToCatchEvent;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import de.cuioss.jsf.api.common.view.ViewDescriptor;
import de.cuioss.portal.authentication.AuthenticatedUserInfo;
import de.cuioss.portal.authentication.PortalUser;
import de.cuioss.portal.configuration.common.PortalPriorities;
import de.cuioss.portal.ui.api.authentication.UserNotAuthorizedException;
import de.cuioss.portal.ui.api.listener.view.PhaseExecution;
import de.cuioss.portal.ui.api.listener.view.PortalRestoreViewListener;
import de.cuioss.portal.ui.api.listener.view.ViewListener;
import de.cuioss.portal.ui.api.view.PortalViewRestrictionManager;
import de.cuioss.portal.ui.api.view.ViewRestrictionManager;
import de.cuioss.portal.ui.runtime.application.configuration.ViewConfiguration;
import de.cuioss.portal.ui.runtime.application.view.DefaultViewRestrictionManager;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * This listener checks the authorization status of a given request. It uses
 * {@link DefaultViewRestrictionManager} to determine the corresponding
 * access-rights. If the request addresses a view that is not admitted, it will
 * fire a {@link UserNotAuthorizedException}.
 *
 * @author Oliver Wolff
 */
@PortalRestoreViewListener(PhaseExecution.AFTER_PHASE)
@Dependent
@Priority(PortalPriorities.PORTAL_CORE_LEVEL - 1)
// Must be called after AuthenticatationListener
@EqualsAndHashCode(of = "viewConfiguration")
@ToString(of = "viewConfiguration")
public class ViewAuthorizationListener implements ViewListener {

    private static final long serialVersionUID = 8427405526881056257L;

    @Inject
    private ViewConfiguration viewConfiguration;

    @Inject
    @PortalViewRestrictionManager
    private Provider<ViewRestrictionManager> viewRestrictionManagerProvider;

    @Inject
    private Event<ExceptionToCatchEvent> catchEvent;

    @Inject
    @PortalUser
    private Provider<AuthenticatedUserInfo> userInfoProvider;

    @Getter
    @Inject
    @ConfigProperty(name = PORTAL_LISTENER_AUTHORIZATION)
    private boolean enabled;

    @Override
    public void handleView(final ViewDescriptor viewDescriptor) {
        // Should only be checked is the view is a secured view
        if (!viewConfiguration.getNonSecuredViewMatcher().match(viewDescriptor)) {
            // ViewRestrictionManager is stateful, therefore we need a provider
            var defaultViewRestrictionManager = viewRestrictionManagerProvider.get();
            if (!defaultViewRestrictionManager.isUserAuthorized(viewDescriptor)) {
                var requiredRoles = defaultViewRestrictionManager.getRequiredRolesForView(viewDescriptor);
                // AuthenticatedUserInfo is stateful, therefore we need a provider
                catchEvent.fire(new ExceptionToCatchEvent(new UserNotAuthorizedException(viewDescriptor, requiredRoles,
                        userInfoProvider.get().getRoles())));
            }
        }
    }
}
