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
package de.cuioss.portal.ui.runtime.application.listener.view;

import static de.cuioss.portal.configuration.PortalConfigurationKeys.PORTAL_LISTENER_AUTHENTICATION;

import javax.annotation.Priority;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import de.cuioss.jsf.api.common.view.ViewDescriptor;
import de.cuioss.portal.authentication.AuthenticatedUserInfo;
import de.cuioss.portal.authentication.PortalUser;
import de.cuioss.portal.configuration.common.PortalPriorities;
import de.cuioss.portal.ui.api.authentication.UserNotAuthenticatedException;
import de.cuioss.portal.ui.api.exception.ExceptionAsEvent;
import de.cuioss.portal.ui.api.listener.view.PhaseExecution;
import de.cuioss.portal.ui.api.listener.view.PortalRestoreViewListener;
import de.cuioss.portal.ui.api.listener.view.ViewListener;
import de.cuioss.portal.ui.runtime.application.configuration.ViewConfiguration;
import de.cuioss.tools.logging.CuiLogger;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * This listener checks the authorization status of a given request. In case the
 * injected {@link ViewConfiguration#getNonSecuredViewMatcher()} returns false
 * and the current {@link AuthenticatedUserInfo#isAuthenticated()} returns false
 * it will fire an {@link UserNotAuthenticatedException}.
 *
 * @author Oliver Wolff
 */
@PortalRestoreViewListener(PhaseExecution.AFTER_PHASE)
@RequestScoped
@Priority(PortalPriorities.PORTAL_CORE_LEVEL)
@EqualsAndHashCode(of = "viewConfiguration")
@ToString(of = "viewConfiguration")
public class ViewAuthenticationListener implements ViewListener {

    private static final long serialVersionUID = 8427405526881056257L;

    private static final CuiLogger log = new CuiLogger(ViewAuthenticationListener.class);

    @Inject
    private ViewConfiguration viewConfiguration;

    @Inject
    private Event<ExceptionAsEvent> catchEvent;

    @Inject
    @PortalUser
    private AuthenticatedUserInfo userInfo;

    @Getter
    @Inject
    @ConfigProperty(name = PORTAL_LISTENER_AUTHENTICATION)
    private boolean enabled;

    @Override
    public void handleView(final ViewDescriptor viewDescriptor) {
        // The user info must be accessed dynamically because of the scoping
        // of the Listener
        if (!viewConfiguration.getNonSecuredViewMatcher().match(viewDescriptor) && !userInfo.isAuthenticated()) {
            log.debug("Try to access '" + viewDescriptor.getLogicalViewId() + "' which does not match '"
                    + viewConfiguration.getNonSecuredViewMatcher() + "' -> UserNotAuthenticatedException");
            catchEvent.fire(new ExceptionAsEvent(new UserNotAuthenticatedException()));
        }
    }
}
