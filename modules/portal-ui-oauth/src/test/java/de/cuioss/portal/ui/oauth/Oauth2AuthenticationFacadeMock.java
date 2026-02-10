/*
 * Copyright © 2025 CUI-OpenSource-Software (info@cuioss.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.cuioss.portal.ui.oauth;

import de.cuioss.portal.authentication.AuthenticatedUserInfo;
import de.cuioss.portal.authentication.facade.AuthenticationSource;
import de.cuioss.portal.authentication.facade.PortalAuthenticationFacade;
import de.cuioss.portal.authentication.model.BaseAuthenticatedUserInfo;
import de.cuioss.portal.authentication.oauth.LoginPagePath;
import de.cuioss.portal.authentication.oauth.Oauth2AuthenticationFacade;
import de.cuioss.portal.common.priority.PortalPriorities;
import de.cuioss.tools.logging.CuiLogger;
import de.cuioss.tools.net.UrlParameter;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Alternative;
import jakarta.inject.Inject;
import jakarta.inject.Provider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static de.cuioss.tools.net.UrlParameter.createParameterString;

@PortalAuthenticationFacade
@ApplicationScoped
@EqualsAndHashCode
@ToString
@Alternative
@Priority(PortalPriorities.PORTAL_ASSEMBLY_LEVEL)
public class Oauth2AuthenticationFacadeMock implements Oauth2AuthenticationFacade {

    private static final CuiLogger LOGGER = new CuiLogger(Oauth2AuthenticationFacadeMock.class);

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
            LOGGER.debug("servletRequest.getSession().getAttribute failed", e);
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
        return false;
    }

    @Override
    public AuthenticatedUserInfo retrieveCurrentAuthenticationContext(final HttpServletRequest servletRequest) {
        var authenticatedUserInfo = BaseAuthenticatedUserInfo.builder().authenticated(authenticated).identifier("user")
                .qualifiedIdentifier("user").displayName("user").build();
        try {
            servletRequest.getSession().setAttribute("AuthenticatedUserInfo", authenticatedUserInfo);
        } catch (IllegalStateException e) {
            LOGGER.debug(".getSession().setAttribute failed", e);
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
