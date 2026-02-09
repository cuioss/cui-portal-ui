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
package de.cuioss.portal.ui.runtime.application.listener.view;

import de.cuioss.jsf.api.common.view.ViewDescriptor;
import de.cuioss.portal.authentication.AuthenticatedUserInfo;
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
import jakarta.annotation.Priority;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.io.Serial;

import static de.cuioss.portal.configuration.PortalConfigurationKeys.PORTAL_LISTENER_AUTHORIZATION;

/**
 * This listener checks the authorization status of a given request. It uses
 * {@link DefaultViewRestrictionManager} to determine the corresponding
 * access-rights. If the request addresses a view that is not admitted, it will
 * fire a {@link UserNotAuthorizedException}.
 * Must be called after AuthenticationListener!
 *
 * @author Oliver Wolff
 */
@PortalRestoreViewListener(PhaseExecution.AFTER_PHASE)
@RequestScoped
@Priority(PortalPriorities.PORTAL_CORE_LEVEL - 1)
@EqualsAndHashCode(of = "viewConfiguration")
@ToString(of = "viewConfiguration")
public class ViewAuthorizationListener implements ViewListener {

    @Serial
    private static final long serialVersionUID = 8427405526881056257L;

    @Inject
    ViewConfiguration viewConfiguration;

    @Inject
    @PortalViewRestrictionManager
    ViewRestrictionManager viewRestrictionManager;

    @Inject
    Event<ExceptionAsEvent> catchEvent;

    @Inject
    AuthenticatedUserInfo userInfo;

    @Getter
    @Inject
    @ConfigProperty(name = PORTAL_LISTENER_AUTHORIZATION)
    boolean enabled;

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
