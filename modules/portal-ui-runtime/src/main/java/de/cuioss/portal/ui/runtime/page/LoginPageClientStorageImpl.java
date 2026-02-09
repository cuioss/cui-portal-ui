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
package de.cuioss.portal.ui.runtime.page;

import de.cuioss.portal.core.storage.ClientStorage;
import de.cuioss.portal.core.storage.PortalClientStorage;
import de.cuioss.portal.ui.api.pages.LoginPage;
import de.cuioss.portal.ui.api.pages.LoginPageClientStorage;
import de.cuioss.uimodel.application.LoginCredentials;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.util.function.Supplier;

import static de.cuioss.portal.ui.api.pages.LoginPage.KEY_REMEMBER_ME;
import static de.cuioss.portal.ui.api.pages.LoginPage.KEY_USERNAME;
import static de.cuioss.portal.ui.api.pages.LoginPage.KEY_USERSTORE;
import static java.util.Objects.requireNonNull;

/**
 * {@linkplain LoginPageClientStorageImpl} is a decorator for
 * {@linkplain ClientStorage} which provide shortcut methods for login page
 * specific interaction
 *
 * @author i000576
 */
@RequestScoped
@ToString
@EqualsAndHashCode
public class LoginPageClientStorageImpl implements LoginPageClientStorage, Serializable {

    @Serial
    private static final long serialVersionUID = 5017201224201952867L;

    @SuppressWarnings("cdi-ambiguous-dependency")
    @Inject
    @PortalClientStorage
    private ClientStorage clientStorage;

    /**
     * {@linkplain Supplier} use {@linkplain ClientStorage} to extract
     * {@linkplain LoginPage#KEY_USERSTORE}, {@linkplain LoginPage#KEY_USERNAME} and
     * {@linkplain LoginPage#KEY_REMEMBER_ME}. The retrieved
     * {@linkplain LoginCredentials} could be still empty if no data is available
     * but never {@code null}
     *
     * @return {@linkplain LoginCredentials}
     */
    @Override
    public Supplier<LoginCredentials> extractFromClientStorage() {
        return () -> LoginCredentials.builder()
                .rememberLoginCredentials(Boolean.parseBoolean(this.clientStorage.get(KEY_REMEMBER_ME)))
                .userStore(clientStorage.get(KEY_USERSTORE)).username(clientStorage.get(KEY_USERNAME)).build();
    }

    /**
     * Update local stored login credentials according passed throw parameter
     *
     * @param loginCredentials {@linkplain LoginCredentials} must not be
     *                         {@code null}
     * @throws NullPointerException if parameter is {@code null}
     */
    @Override
    public void updateLocalStored(final LoginCredentials loginCredentials) {
        requireNonNull(loginCredentials, "LoginCredentials must not be null");

        this.clientStorage.put(KEY_USERSTORE, loginCredentials.getUserStore());

        if (loginCredentials.isRememberLoginCredentials()) {
            this.clientStorage.put(KEY_USERNAME, loginCredentials.getUsername());
            this.clientStorage.put(KEY_REMEMBER_ME, Boolean.TRUE.toString());
        } else {
            this.clientStorage.remove(KEY_USERNAME);
            this.clientStorage.remove(KEY_REMEMBER_ME);
        }
    }

    @Override
    public ClientStorage getWrapped() {
        return clientStorage;
    }

}
