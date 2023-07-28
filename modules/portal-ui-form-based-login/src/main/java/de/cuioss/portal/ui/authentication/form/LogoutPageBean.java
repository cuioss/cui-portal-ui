package de.cuioss.portal.ui.authentication.form;

import javax.annotation.Priority;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Event;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import de.cuioss.jsf.api.servlet.ServletAdapterUtil;
import de.cuioss.portal.authentication.AuthenticatedUserInfo;
import de.cuioss.portal.authentication.LoginEvent;
import de.cuioss.portal.authentication.PortalLoginEvent;
import de.cuioss.portal.authentication.PortalUser;
import de.cuioss.portal.authentication.facade.AuthenticationFacade;
import de.cuioss.portal.authentication.facade.PortalAuthenticationFacade;
import de.cuioss.portal.configuration.common.PortalPriorities;
import de.cuioss.portal.ui.api.ui.pages.LoginPage;
import de.cuioss.portal.ui.api.ui.pages.LogoutPage;
import de.cuioss.portal.ui.api.ui.pages.PortalCorePagesLogout;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author Oliver Wolff
 */
@PortalCorePagesLogout
@Named(LogoutPage.BEAN_NAME)
@Priority(PortalPriorities.PORTAL_CORE_LEVEL)
@RequestScoped
@EqualsAndHashCode(doNotUseGetters = true)
@ToString(doNotUseGetters = true)
public class LogoutPageBean implements LogoutPage {

    private static final long serialVersionUID = -3588577094632702649L;

    @Inject
    @PortalAuthenticationFacade
    private AuthenticationFacade authenticationFacade;

    @Inject
    private FacesContext facesContext;

    @Inject
    @PortalLoginEvent
    private Event<LoginEvent> preLogoutEvent;

    @Inject
    @PortalUser
    private AuthenticatedUserInfo authenticatedUserInfo;

    /**
     * Logs out and redirects to login page.
     */
    @Override
    public String logoutViewAction() {
        performLogout();
        return LoginPage.OUTCOME;
    }

    @Override
    public String performLogout() {
        if (authenticatedUserInfo.isAuthenticated()) {
            preLogoutEvent.fire(LoginEvent.builder().action(LoginEvent.Action.LOGOUT).build());
        }
        return authenticationFacade.logout(ServletAdapterUtil.getRequest(facesContext)) ? LoginPage.OUTCOME : null;
    }
}
