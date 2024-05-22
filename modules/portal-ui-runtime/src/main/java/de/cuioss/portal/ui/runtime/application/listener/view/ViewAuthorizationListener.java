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

import static de.cuioss.portal.configuration.PortalConfigurationKeys.PORTAL_LISTENER_AUTHORIZATION;

import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;

import jakarta.annotation.Priority;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import de.cuioss.jsf.api.common.view.ViewDescriptor;
import de.cuioss.portal.authentication.AuthenticatedUserInfo;
import de.cuioss.portal.authentication.PortalUser;
import de.cuioss.portal.common.priority.PortalPriorities;
import de.cuioss.portal.ui.api.authentication.UserNotAuthorizedException;
import de.cuioss.portal.ui.api.exception.ExceptionAsEvent;
import de.cuioss.portal.ui.api.listener.view.PhaseExecution;
import de.cuioss.portal.ui.api.listener.view.PortalRestoreViewListener;
import de.cuioss.portal.ui.api.listener.view.ViewListener;
import de.cuioss.portal.ui.api.view.PortalViewRestrictionManager;
import de.cuioss.portal.ui.api.view.ViewRestrictionManager;
import de.cuioss.portal.ui.runtime.application.configuration.ViewConfiguration;
import de.cuioss.portal.ui.runtime.application.view.DefaultViewRestrictionManager;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * This listener checks the authorization status of a given request. It uses
 * {@link DefaultViewRestrictionManager} to determine the corresponding
 * access-rights. If the request addresses a view that is not admitted, it will
 * fire a {@link UserNotAuthorizedException}.
 *
 * @author Oliver Wolff
 */
@PortalRestoreViewListener(PhaseExecution.AFTER_PHASE)
@RequestScoped
@Priority(PortalPriorities.PORTAL_CORE_LEVEL - 1)
// Must be called after AuthenticatationListener
@EqualsAndHashCode(of = "viewConfiguration")
@ToString(of = "viewConfiguration")
public class ViewAuthorizationListener implements ViewListener {

    private static final long serialVersionUID = 8427405526881056257L;

    @Inject
    private ViewConfiguration viewConfiguration;

    @Inject
    @PortalViewRestrictionManager
    private ViewRestrictionManager viewRestrictionManager;

    @Inject
    private Event<ExceptionAsEvent> catchEvent;

    @Inject
    @PortalUser
    private AuthenticatedUserInfo userInfo;

    @Getter
    @Inject
    @ConfigProperty(name = PORTAL_LISTENER_AUTHORIZATION)
    private boolean enabled;

    @Override
    public void handleView(final ViewDescriptor viewDescriptor) {
        // Should only be checked is the view is a secured view
        if (!viewConfiguration.getNonSecuredViewMatcher().match(viewDescriptor)
                && !viewRestrictionManager.isUserAuthorized(viewDescriptor)) {
            var requiredRoles = viewRestrictionManager.getRequiredRolesForView(viewDescriptor);
            catchEvent.fire(new ExceptionAsEvent(
                    new UserNotAuthorizedException(viewDescriptor, requiredRoles, userInfo.getRoles())));
        }
    }
}
