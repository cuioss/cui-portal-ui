package de.cuioss.portal.ui.oauth;

import static de.cuioss.tools.net.UrlParameter.createParameterString;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Priority;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;
import javax.inject.Inject;
import javax.inject.Provider;
import javax.servlet.http.HttpServletRequest;

import de.cuioss.portal.authentication.AuthenticatedUserInfo;
import de.cuioss.portal.authentication.facade.AuthenticationSource;
import de.cuioss.portal.authentication.facade.PortalAuthenticationFacade;
import de.cuioss.portal.authentication.model.BaseAuthenticatedUserInfo;
import de.cuioss.portal.authentication.oauth.LoginPagePath;
import de.cuioss.portal.authentication.oauth.Oauth2AuthenticationFacade;
import de.cuioss.portal.configuration.common.PortalPriorities;
import de.cuioss.tools.logging.CuiLogger;
import de.cuioss.tools.net.UrlParameter;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@SuppressWarnings("javadoc")
@PortalAuthenticationFacade
@ApplicationScoped
@EqualsAndHashCode
@ToString
@Alternative
@Priority(PortalPriorities.PORTAL_ASSEMBLY_LEVEL)
public class Oauth2AuthenticationFacadeMock implements Oauth2AuthenticationFacade {

    private static final CuiLogger log = new CuiLogger(Oauth2AuthenticationFacadeMock.class);

    @Getter
    @Setter
    private boolean authenticated = true;

    @Getter
    private String redirectUrl;

    @Setter
    private String tokenToRetrieve = "token";

    @Setter
    private String renewUrl;

    @Setter
    private String renewInterval;

    @Setter
    private String clientLogoutUrl;

    @Inject
    @LoginPagePath
    private String loginUrl;

    @Inject
    private Provider<HttpServletRequest> servletRequestProvider;

    public void resetRedirectUrl() {
        redirectUrl = null;
    }

    @Override
    public AuthenticatedUserInfo testLogin(final List<UrlParameter> parameters, final String scopes) {
        AuthenticatedUserInfo currentUser = null;
        var servletRequest = servletRequestProvider.get();
        try {
            if (null != servletRequest.getSession(false)) {
                currentUser = (AuthenticatedUserInfo) servletRequest.getSession().getAttribute("AuthenticatedUserInfo");
            }
        } catch (IllegalStateException e) {
            log.debug("servletRequest.getSession().getAttribute failed", e);
        }
        if ((null == currentUser || !currentUser.isAuthenticated()) && authenticated) {
            servletRequest.getSession().invalidate();
        }
        return retrieveCurrentAuthenticationContext(servletRequest);
    }

    @Override
    public void invalidateToken() {

    }

    @Override
    public boolean logout(final HttpServletRequest servletRequest) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public AuthenticatedUserInfo retrieveCurrentAuthenticationContext(final HttpServletRequest servletRequest) {
        var authenticatedUserInfo = BaseAuthenticatedUserInfo.builder()
                .authenticated(authenticated).identifier("user").qualifiedIdentifier("user").displayName("user")
                .build();
        try {
            servletRequest.getSession().setAttribute("AuthenticatedUserInfo", authenticatedUserInfo);
        } catch (IllegalStateException e) {
            log.debug(".getSession().setAttribute failed", e);
        }
        return authenticatedUserInfo;

    }

    @Override
    public String retrieveToken(final String scopes) {
        return tokenToRetrieve;
    }

    @Override
    public String retrieveToken(final AuthenticatedUserInfo currentUser, final String scopes) {
        return tokenToRetrieve;
    }

    @Override
    public Map<String, Object> retrieveIdToken(final AuthenticatedUserInfo currentUser) {
        return Collections.emptyMap();
    }

    @Override
    public String retrieveClientToken(final String scopes) {
        return null;
    }

    @Override
    public void sendRedirect(final String scopes) {
        redirectUrl = loginUrl;
    }

    @Override
    public String retrieveOauth2RedirectUrl(final String scopes, final String idToken) {
        return "redirect";
    }

    @Override
    public String retrieveOauth2RenewUrl() {
        return renewUrl;
    }

    @Override
    public String retrieveRenewInterval() {
        return renewInterval;
    }

    @Override
    public AuthenticatedUserInfo refreshUserinfo() {
        return null;
    }

    @Override
    public String getLoginUrl() {
        return loginUrl;
    }

    @Override
    public String retrieveClientLogoutUrl(Set<UrlParameter> additionalUrlParams) {
        return clientLogoutUrl + createParameterString(additionalUrlParams.toArray(UrlParameter[]::new));
    }

    @Override
    public void sendRedirect() {
        redirectUrl = loginUrl;
    }

    @Override
    public AuthenticationSource getAuthenticationSource() {
        return AuthenticationSource.OPEN_ID_CONNECT;
    }
}
