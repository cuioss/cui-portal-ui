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
package de.cuioss.portal.ui.test.mocks;

import static java.util.Objects.requireNonNull;

import java.util.HashSet;
import java.util.Set;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.SessionScoped;

import de.cuioss.jsf.api.common.view.ViewDescriptor;
import de.cuioss.portal.ui.api.view.PortalViewRestrictionManager;
import de.cuioss.portal.ui.api.view.ViewRestrictionManager;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Mock-implementation of {@link ViewRestrictionManager}. It is
 * {@link ApplicationScoped} in order to simplify state management for
 * unit-tests, The portal implementations are usually {@link SessionScoped}
 *
 * @author Oliver Wolff
 */
@PortalViewRestrictionManager
@ApplicationScoped
@EqualsAndHashCode
@ToString
public class PortalViewRestrictionManagerMock implements ViewRestrictionManager {

    private static final long serialVersionUID = 5290360665409615415L;

    /**
     * The required roles to be returned with
     * {@link #getRequiredRolesForView(ViewDescriptor)}
     */
    @Getter
    @Setter
    private Set<String> requiredRoles = new HashSet<>();

    @Setter
    private boolean authorized = true;

    @Override
    public Set<String> getRequiredRolesForView(final ViewDescriptor descriptor) {
        requireNonNull(descriptor);
        return requiredRoles;
    }

    @Override
    public boolean isUserAuthorized(final ViewDescriptor descriptor) {
        requireNonNull(descriptor);
        return authorized;
    }

    @Override
    public boolean isUserAuthorizedForViewOutcome(final String viewOutcome) {
        requireNonNull(viewOutcome);
        return authorized;
    }

}
