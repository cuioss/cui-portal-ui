package de.cuioss.portal.ui.oauth;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import de.cuioss.portal.authentication.AuthenticatedUserInfo;
import de.cuioss.portal.authentication.LoginEvent;
import de.cuioss.portal.authentication.PortalLoginEvent;
import de.cuioss.portal.authentication.PortalUser;
import de.cuioss.portal.authentication.facade.AuthenticationFacade;
import de.cuioss.portal.authentication.facade.PortalAuthenticationFacade;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Provides an empty logout page to be embedded from the oauth server via iframe
 * for the front channel logout - no further action is necessary.
 *
 * @author Matthias Walliczek
 */
@RequestScoped
@Named
@EqualsAndHashCode(of = "servletRequest", doNotUseGetters = true)
@ToString(of = "servletRequest", doNotUseGetters = true)
public class OauthIFrameLogoutPageBean {

    @Inject
    @PortalAuthenticationFacade
    private AuthenticationFacade authenticationFacade;

    @Inject
    private HttpServletRequest servletRequest;

    @Inject
    @PortalUser
    private AuthenticatedUserInfo authenticatedUserInfo;

    @Inject
    @PortalLoginEvent
    private Event<LoginEvent> preLougoutEvent;

    /**
     * @return
     */
    public String logoutViewAction() {
        if (authenticatedUserInfo.isAuthenticated()) {
            preLougoutEvent.fire(LoginEvent.builder().action(LoginEvent.Action.LOGOUT).build());
        }
        authenticationFacade.logout(servletRequest);
        return null;
    }
}
