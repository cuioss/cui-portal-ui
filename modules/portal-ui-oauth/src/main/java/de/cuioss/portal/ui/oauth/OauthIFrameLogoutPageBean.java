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

import java.io.Serial;
import java.io.Serializable;

import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;

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
@EqualsAndHashCode(of = "authenticatedUserInfo", doNotUseGetters = true)
@ToString(of = "authenticatedUserInfo", doNotUseGetters = true)
public class OauthIFrameLogoutPageBean implements Serializable {

    @Serial
    private static final long serialVersionUID = 7139554877749164888L;

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
