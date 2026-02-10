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
package de.cuioss.portal.ui.api.view;

import de.cuioss.jsf.api.common.view.ViewDescriptor;
import jakarta.enterprise.context.SessionScoped;

import java.io.Serializable;
import java.util.Set;

/**
 * The ViewRestrictionManager is used for deciding whether a concrete view is
 * authorized for the current users. The implementation is therefore stateful,
 * usually {@link SessionScoped}.
 *
 * @author Oliver Wolff
 */
public interface ViewRestrictionManager extends Serializable {

    /**
     * Determines the roles required for accessing this specific views.
     *
     * @param descriptor identifying the view to be accessed, must not be null
     * @return an immutable {@link Set} of role-names needed to access this views,
     * may be empty but never {@code null}
     */
    Set<String> getRequiredRolesForView(ViewDescriptor descriptor);

    /**
     * Determines whether the currently logged-in user is allowed / authorized to
     * access the given view.
     *
     * @param descriptor identifying the view to be accessed, must not be null
     * @return a boolean indicating whether the current user is authorized to access
     * the given view {@code true} or not {@code false}
     */
    boolean isUserAuthorized(ViewDescriptor descriptor);

    /**
     * Determines whether the currently logged-in user is allowed / authorized to
     * access the given view, identified by the given outcome.
     *
     * @param viewOutcome String outcome identifying a concrete view that should be
     *                    checked
     * @return a boolean indicating whether the current user is authorized to access
     * the given view {@code true} or not {@code false}
     * @throws IllegalStateException signaling, that the view cannot be
     *                               determined, e.g.g there is no navigation-rule
     *                               defined for the given outcome
     */
    boolean isUserAuthorizedForViewOutcome(String viewOutcome);

}
