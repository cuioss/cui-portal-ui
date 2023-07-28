package de.cuioss.portal.ui.oauth;

import java.util.Optional;

import javax.faces.component.FacesComponent;
import javax.faces.component.UINamingContainer;

import de.cuioss.portal.authentication.facade.PortalAuthenticationFacade;
import de.cuioss.portal.authentication.oauth.Oauth2AuthenticationFacade;
import de.cuioss.portal.core.cdi.PortalBeanManager;
import de.cuioss.tools.logging.CuiLogger;
import lombok.Getter;

/**
 * Backing bean for sso composite component.
 *
 * @author Matthias Walliczek
 */
@FacesComponent("de.cuioss.portal.ui.oauth.OauthRenewComponent")
public class OauthRenewComponent extends UINamingContainer {

    private static final CuiLogger log = new CuiLogger(OauthRenewComponent.class);

    @Getter
    private String loginUrl;

    @Getter
    private final String renewUrl;

    @Getter
    private final String renewInterval;

    /**
     * initialize
     */
    public OauthRenewComponent() {
        final Optional<Oauth2AuthenticationFacade> authenticationFacade = PortalBeanManager
                .resolveBean(Oauth2AuthenticationFacade.class, PortalAuthenticationFacade.class);
        if (authenticationFacade.isPresent()) {
            renewUrl = authenticationFacade.get().retrieveOauth2RenewUrl();
            renewInterval = authenticationFacade.get().retrieveRenewInterval();
            loginUrl = authenticationFacade.get().getLoginUrl();
        } else {
            log.error(PortalBeanManager.createLogMessage(Oauth2AuthenticationFacade.class,
                    PortalAuthenticationFacade.class));
            renewUrl = null;
            renewInterval = null;
            loginUrl = null;
        }
    }
}
