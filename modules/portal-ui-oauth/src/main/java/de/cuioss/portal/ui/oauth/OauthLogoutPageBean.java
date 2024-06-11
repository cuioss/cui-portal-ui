/*
 * Copyright 2023 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * https://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.cuioss.portal.ui.oauth;

import static de.cuioss.portal.authentication.oauth.OAuthConfigKeys.OPEN_ID_CLIENT_POST_LOGOUT_REDIRECT_URI;
import static de.cuioss.tools.string.MoreStrings.isBlank;
import static de.cuioss.tools.string.MoreStrings.isPresent;

import java.io.Serial;
import java.util.Optional;

import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.event.Event;
import jakarta.enterprise.inject.Alternative;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;

import de.cuioss.jsf.api.application.navigation.NavigationUtils;

import jakarta.annotation.Priority;
import de.cuioss.jsf.api.servlet.ServletAdapterUtil;
import de.cuioss.portal.authentication.AuthenticatedUserInfo;
import de.cuioss.portal.authentication.LoginEvent;
import de.cuioss.portal.authentication.PortalLoginEvent;
import de.cuioss.portal.authentication.PortalUser;
import de.cuioss.portal.authentication.facade.PortalAuthenticationFacade;
import de.cuioss.portal.authentication.oauth.LoginPagePath;
import de.cuioss.portal.authentication.oauth.OAuthConfigKeys;
import de.cuioss.portal.authentication.oauth.Oauth2AuthenticationFacade;
import de.cuioss.portal.authentication.oauth.Oauth2Configuration;
import de.cuioss.portal.common.priority.PortalPriorities;
import de.cuioss.portal.ui.api.ui.pages.LoginPage;
import de.cuioss.portal.ui.api.ui.pages.LogoutPage;
import de.cuioss.portal.ui.api.ui.pages.PortalCorePagesLogout;
import de.cuioss.tools.collect.CollectionBuilder;
import de.cuioss.tools.logging.CuiLogger;
import de.cuioss.tools.net.UrlParameter;
import de.cuioss.tools.string.MoreStrings;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Oauth Logout Page Bean to redirect to the oauth server logout page.
 *
 * @author Matthias Walliczek
 */
@PortalCorePagesLogout
@Named(LogoutPage.BEAN_NAME)
@Alternative
@Priority(PortalPriorities.PORTAL_MODULE_LEVEL)
@RequestScoped
@EqualsAndHashCode
@ToString
public class OauthLogoutPageBean implements LogoutPage {

    @Serial
    private static final long serialVersionUID = -3588577094632702649L;

    private static final CuiLogger LOGGER = new CuiLogger(OauthLogoutPageBean.class);

    @Inject
    @PortalAuthenticationFacade
    private Oauth2AuthenticationFacade authenticationFacade;

    @Inject
    private FacesContext facesContext;

    @Inject
    @PortalLoginEvent
    private Event<LoginEvent> preLogoutEvent;

    @Inject
    @PortalUser
    private AuthenticatedUserInfo authenticatedUserInfo;

    @Inject
    private Oauth2Configuration oauth2Configuration;

    @Inject
    @LoginPagePath
    private String loginUrl;

    @Inject
    private HttpServletRequest servletRequest;

    @Inject
    private Oauth2Configuration configuration;

    /**
     * Logs out and redirects to login page.
     */
    @Override
    public String logoutViewAction() {
        if (MoreStrings.isEmpty(oauth2Configuration.getLogoutUri())) {
            LOGGER.debug("no logout URI configured. redirecting to login page.");
            performLogout();
            return LoginPage.OUTCOME;
        }

        var idpLogoutUrl = authenticationFacade.retrieveClientLogoutUrl(
                CollectionBuilder.<UrlParameter>copyFrom().add(getPostLogoutRedirectUriParam()).toImmutableSet());
        LOGGER.debug("Redirecting to IDP logout URL: {}", idpLogoutUrl);

        // Actually wrong if strictly sticking to rp-initiated-logout specification,
        // but should not bother anyone either.
        // If done right, the iframeLogout.xhtml should be called by IDP.
        performLogout();

        NavigationUtils.redirect(facesContext, idpLogoutUrl);
        return null;
    }

    @Override
    public String performLogout() {
        if (authenticatedUserInfo.isAuthenticated()) {
            preLogoutEvent.fire(LoginEvent.builder().action(LoginEvent.Action.LOGOUT).build());
        }
        return authenticationFacade.logout(ServletAdapterUtil.getRequest(facesContext)) ? LoginPage.OUTCOME : null;
    }

    /**
     * @return url parameter for post_logout_redirect_uri. the value is obtained
     *         from {@link Oauth2Configuration#getPostLogoutRedirectUri()} or, if
     *         not present, constructed from external context path and
     *         {@link #loginUrl}.
     */
    private Optional<UrlParameter> getPostLogoutRedirectUriParam() {

        final var postLogoutRedirectUri = configuration.getPostLogoutRedirectUri();
        if (isPresent(postLogoutRedirectUri)) {
            if (isBlank(configuration.getLogoutRedirectParamName())) {
                LOGGER.warn("postLogoutRedirectUri set, but no url-parameter name. Set via: {}",
                        OAuthConfigKeys.OPEN_ID_CLIENT_LOGOUT_REDIRECT_PARAMETER);
                return Optional.empty();
            }

            LOGGER.debug("postLogoutRedirectUri: {}", postLogoutRedirectUri);
            return Optional.of(new UrlParameter(configuration.getLogoutRedirectParamName(), postLogoutRedirectUri));
        }

        LOGGER.debug("No postLogoutRedirectUri configured. Config-Key: {}", OPEN_ID_CLIENT_POST_LOGOUT_REDIRECT_URI);
        return Optional.empty();
    }
}
