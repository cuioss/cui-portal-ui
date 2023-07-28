package de.cuioss.portal.ui.oauth;

import static de.cuioss.tools.base.Preconditions.checkArgument;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import de.cuioss.jsf.api.application.navigation.NavigationUtils;
import de.cuioss.jsf.api.common.view.ViewDescriptor;
import de.cuioss.jsf.api.servlet.ServletAdapterUtil;
import de.cuioss.portal.authentication.AuthenticatedUserInfo;
import de.cuioss.portal.authentication.facade.AuthenticationResults;
import de.cuioss.portal.authentication.facade.PortalAuthenticationFacade;
import de.cuioss.portal.authentication.oauth.Oauth2AuthenticationFacade;
import de.cuioss.portal.authentication.oauth.Oauth2Configuration;
import de.cuioss.portal.ui.api.ui.context.CuiCurrentView;
import de.cuioss.portal.ui.api.ui.pages.HomePage;
import de.cuioss.portal.ui.runtime.page.AbstractLoginPageBean;
import de.cuioss.tools.logging.CuiLogger;
import de.cuioss.uimodel.nameprovider.IDisplayNameProvider;
import de.cuioss.uimodel.result.ResultObject;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Page Bean for the Oauth2 Login Page Bean. Supports two mode:<br />
 * <ul>
 * <li>Pseudo login page bean: The login page bean consists only of the view
 * action directly redirecting either to the oauth server or into the
 * application</li>
 * <li>Landing page bean: A landing page with a button linking to the oauth
 * server login page. When using deep linking, this page is skipped.</li>
 * </ul>
 * In both mode it is checked if page is accessed via redirect from oauth server
 * with matching parameters. <br />
 *
 * @author Matthias Walliczek
 */
@Named
@RequestScoped
@EqualsAndHashCode(callSuper = false)
@ToString
public class OauthLoginPageBean extends AbstractLoginPageBean {

    private static final CuiLogger log = new CuiLogger(OauthLoginPageBean.class);

    private static final long serialVersionUID = 5261664290647994366L;

    @Inject
    private FacesContext facesContext;

    @Inject
    @CuiCurrentView
    private ViewDescriptor currentView;

    @Inject
    @PortalAuthenticationFacade
    private Oauth2AuthenticationFacade authenticationFacade;

    @Inject
    @PortalWrappedOauthFacade
    private WrappedOauthFacade wrappedOauthFacade;

    @Inject
    private Oauth2Configuration oauth2Configuration;

    private boolean doRedirectOnFailure;

    /**
     * Initialize the bean and check the configuration.
     */
    @PostConstruct
    public void init() {
        if (null == oauth2Configuration) {
            throw new IllegalStateException(
                    "Portal-528: Oauth2 configuration is invalid or missing, please fix the oauth2 configuration!");
        }
    }

    /**
     * Check if the client was redirected from an oauth2 server with valid code and
     * login if so. Otherwise check if the client tries to access a deep link and
     * skip this page if so.
     *
     * @return the next view after login if login was successful. Otherwise null.
     */
    public String testLoginViewAction() {
        var targetView = wrappedOauthFacade.retrieveTargetView();
        doRedirectOnFailure = !targetView.getViewId().equalsIgnoreCase(currentView.getLogicalViewId()) && !targetView
                .getViewId().equalsIgnoreCase(NavigationUtils.lookUpToLogicalViewIdBy(facesContext, HomePage.OUTCOME));
        return loginAction(() -> targetView, ServletAdapterUtil.getRequest(facesContext), facesContext);
    }

    /**
     * Check if the client was redirected from an oauth2 server with valid code and
     * login if so. Otherwise start the login implicit by redirecting to the oauth
     * server.
     *
     * @return the next view after login if login was successful. Otherwise null.
     */
    public String testLoginAndRedirectViewAction() {
        var targetView = wrappedOauthFacade.retrieveTargetView();
        doRedirectOnFailure = true;
        return loginAction(() -> targetView, ServletAdapterUtil.getRequest(facesContext), facesContext);
    }

    private String loginTarget;

    /**
     * @return The oauth2 login url.
     */
    public String loginTarget() {
        if (null == loginTarget) {
            loginTarget = authenticationFacade.retrieveOauth2RedirectUrl(oauth2Configuration.getInitialScopes(), null);
        }
        return loginTarget;
    }

    @Override
    protected ResultObject<AuthenticatedUserInfo> doLogin(final HttpServletRequest currentServletRequest) {
        checkArgument(null != currentView, "currentView must be available");
        return AuthenticationResults.validResult(
                authenticationFacade.testLogin(currentView.getUrlParameter(), oauth2Configuration.getInitialScopes()));
    }

    @Override
    protected void handleLoginFailed(final IDisplayNameProvider<?> message) {
        if (doRedirectOnFailure) {
            log.debug("login failed, redirecting to oauth server");
            wrappedOauthFacade.preserveCurrentView();
            doRedirectOnFailure = false;
            authenticationFacade.sendRedirect(oauth2Configuration.getInitialScopes());
        } else {
            log.debug("noop. redirectOnFailure is false");
        }
    }
}
