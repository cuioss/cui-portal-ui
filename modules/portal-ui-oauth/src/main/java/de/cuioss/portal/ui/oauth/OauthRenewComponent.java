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

import java.util.Optional;

import jakarta.faces.component.FacesComponent;
import jakarta.faces.component.UINamingContainer;

import de.cuioss.portal.authentication.facade.PortalAuthenticationFacade;
import de.cuioss.portal.authentication.oauth.Oauth2AuthenticationFacade;
import de.cuioss.portal.common.cdi.PortalBeanManager;
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
    private final String loginUrl;

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
