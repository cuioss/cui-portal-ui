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
package de.cuioss.portal.ui.authentication.form;

import de.cuioss.jsf.api.servlet.ServletAdapterUtil;
import de.cuioss.portal.authentication.AuthenticatedUserInfo;
import de.cuioss.portal.authentication.LoginEvent;
import de.cuioss.portal.authentication.PortalLoginEvent;
import de.cuioss.portal.authentication.PortalUser;
import de.cuioss.portal.authentication.facade.AuthenticationFacade;
import de.cuioss.portal.authentication.facade.PortalAuthenticationFacade;
import de.cuioss.portal.common.priority.PortalPriorities;
import de.cuioss.portal.ui.api.pages.LoginPage;
import de.cuioss.portal.ui.api.pages.LogoutPage;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.event.Event;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serial;

/**
 * @author Oliver Wolff
 */
@Named(LogoutPage.BEAN_NAME)
@Priority(PortalPriorities.PORTAL_CORE_LEVEL)
@RequestScoped
@EqualsAndHashCode(doNotUseGetters = true)
@ToString(doNotUseGetters = true)
public class LogoutPageBean implements LogoutPage {

    @Serial
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
