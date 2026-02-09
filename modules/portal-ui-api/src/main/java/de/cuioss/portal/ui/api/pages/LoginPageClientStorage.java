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
package de.cuioss.portal.ui.api.pages;

import de.cuioss.portal.core.storage.ClientStorage;
import de.cuioss.uimodel.application.LoginCredentials;

import java.util.function.Supplier;

/**
 * {@linkplain LoginPageClientStorage} is a decorator for
 * {@linkplain ClientStorage} which provide shortcut methods for login page
 * specific interaction
 *
 * @author i000576
 */
public interface LoginPageClientStorage {

    /**
     * {@linkplain Supplier} use {@linkplain ClientStorage} to extract
     * {@linkplain LoginPage#KEY_USERSTORE}, {@linkplain LoginPage#KEY_USERNAME} and
     * {@linkplain LoginPage#KEY_REMEMBER_ME}. The retrieved
     * {@linkplain LoginCredentials} could be still empty if no data is available
     * but never {@code null}
     *
     * @return {@linkplain LoginCredentials}
     */
    Supplier<LoginCredentials> extractFromClientStorage();

    /**
     * Update local stored login credentials according passed throw parameter
     *
     * @param loginCredentials {@linkplain LoginCredentials} must not be
     *                         {@code null}
     * @throws NullPointerException if parameter is {@code null}
     */
    void updateLocalStored(final LoginCredentials loginCredentials);

    /**
     * Retrieve wrapped
     *
     * @return {@linkplain ClientStorage}
     */
    ClientStorage getWrapped();

}
