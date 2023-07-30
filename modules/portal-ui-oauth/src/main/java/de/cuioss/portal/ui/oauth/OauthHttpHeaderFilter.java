package de.cuioss.portal.ui.oauth;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Provider;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.cuioss.jsf.api.application.navigation.NavigationUtils;
import de.cuioss.portal.core.listener.literal.ServletInitialized;
import de.cuioss.tools.string.MoreStrings;

/**
 * Enable the token renewing by setting the cors header for the login page to be
 * accessed via indirect Ajax call through oauth server.
 */
@RequestScoped
public class OauthHttpHeaderFilter {

    static final String ACCESS_CONTROL_ALLOW_CREDENTIALS = "Access-Control-Allow-Credentials";

    static final String ACCESS_CONTROL_ALLOW_ORIGIN = "Access-Control-Allow-Origin";

    static final String ORIGIN = "Origin";

    static final String FACES_GUEST_LOGIN_JSF = "/faces/guest/login.jsf";

    @Inject
    private Provider<HttpServletRequest> requestProvider;

    /**
     * @param response
     */
    public void onCreate(@Observes @ServletInitialized final HttpServletResponse response) {
        var request = requestProvider.get();
        var foundId = NavigationUtils.extractRequestUri(request);
        if (FACES_GUEST_LOGIN_JSF.endsWith(foundId) && !MoreStrings.isEmpty(requestProvider.get().getHeader(ORIGIN))) {
            response.setHeader(ACCESS_CONTROL_ALLOW_ORIGIN, requestProvider.get().getHeader(ORIGIN));
            response.setHeader(ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");
        }

    }
}
