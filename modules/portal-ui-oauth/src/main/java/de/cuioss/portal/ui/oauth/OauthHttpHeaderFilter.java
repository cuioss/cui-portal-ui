package de.cuioss.portal.ui.oauth;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Provider;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.deltaspike.core.spi.activation.Deactivatable;

import de.cuioss.jsf.api.application.navigation.NavigationUtils;
import de.cuioss.portal.configuration.initializer.ApplicationInitializer;
import de.cuioss.portal.configuration.initializer.PortalInitializer;
import de.cuioss.portal.core.listener.literal.ServletInitialized;
import de.cuioss.tools.string.MoreStrings;

/**
 * Enable the token renewing by setting the cors header for the login page to be
 * accessed via indirect Ajax call throu oauth server.
 */
@ApplicationScoped
@PortalInitializer
public class OauthHttpHeaderFilter implements ApplicationInitializer, Deactivatable {

    private static final String FACES_GUEST_LOGIN_JSF = "/faces/guest/login.jsf";

    @Inject
    private Provider<HttpServletRequest> requestProvider;

    @Override
    public void initialize() {
        // nothing to do
    }

    /**
     * @param response
     */
    public void onCreate(@Observes @ServletInitialized final HttpServletResponse response) {
        var request = requestProvider.get();
        var foundId = NavigationUtils.extractRequestUri(request);
        if (FACES_GUEST_LOGIN_JSF.endsWith(foundId)
                && !MoreStrings.isEmpty(requestProvider.get().getHeader("Origin"))) {
            response.setHeader("Access-Control-Allow-Origin", requestProvider.get().getHeader("Origin"));
            response.setHeader("Access-Control-Allow-Credentials", "true");
        }

    }

    @Override
    public Integer getOrder() {
        return ORDER_LATE;
    }
}
