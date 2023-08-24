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
package de.cuioss.portal.ui.api.authentication;

import java.util.Collection;

import de.cuioss.jsf.api.common.view.ViewDescriptor;
import de.cuioss.portal.authentication.AuthenticatedUserInfo;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * To be fired if a user is not authorized for a certain view. It will be
 * derived from and {@link AuthenticatedUserInfo#getRoles()}
 *
 * @author Matthias Walliczek
 */
@RequiredArgsConstructor
public class UserNotAuthorizedException extends RuntimeException {

    private static final long serialVersionUID = -2144266323935586941L;

    /** The view that is requested */
    @Getter
    @NonNull
    private final ViewDescriptor requestedView;

    /** The required roles to access the views. */
    @Getter
    @NonNull
    private final Collection<String> requiredRoles;

    /** The roles the user provides. */
    @Getter
    @NonNull
    private final Collection<String> userRoles;

}
