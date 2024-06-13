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
package de.cuioss.portal.ui.runtime.page;

import static de.cuioss.portal.ui.api.pages.LoginPage.KEY_REMEMBER_ME;
import static de.cuioss.portal.ui.api.pages.LoginPage.KEY_USERNAME;
import static de.cuioss.portal.ui.api.pages.LoginPage.KEY_USERSTORE;
import static de.cuioss.test.generator.Generators.strings;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import jakarta.inject.Inject;

import org.jboss.weld.junit5.auto.AddBeanClasses;
import org.junit.jupiter.api.Test;

import de.cuioss.portal.core.storage.PortalClientStorage;
import de.cuioss.portal.core.test.mocks.core.PortalClientStorageMock;
import de.cuioss.portal.ui.test.junit5.EnablePortalUiEnvironment;
import de.cuioss.portal.ui.test.tests.AbstractPageBeanTest;
import de.cuioss.uimodel.application.LoginCredentials;
import lombok.Getter;

@EnablePortalUiEnvironment
@AddBeanClasses({ PortalClientStorageMock.class })
class LoginPageClientStorageImplTest extends AbstractPageBeanTest<LoginPageClientStorageImpl> {

    @Inject
    @Getter
    private LoginPageClientStorageImpl underTest;

    @Inject
    @PortalClientStorage
    private PortalClientStorageMock clientStorage;

    @Test
    void shouldProvideEmptyLoginCredentials() {

        final var loginCredentials = underTest.extractFromClientStorage().get();

        final var emptyCredentials = new LoginCredentials();

        assertEquals(emptyCredentials, loginCredentials);
    }

    @Test
    void shouldExtractLoginCredentialsFromLocalStorage() {

        final var expected = letLoginCredentialsGetStored();

        final var loginCredentials = underTest.extractFromClientStorage().get();

        assertEquals(expected, loginCredentials);
    }

    @Test
    void shouldIgnoreStoreLoginCredentials() {
        assertEqualsNoUserSpecificCookiesAreStored();

        underTest.updateLocalStored(anyLoginCredentials(false));

        assertEqualsNoUserSpecificCookiesAreStored();
    }

    @Test
    void shouldUpdateLoginCredentialsInLocalStorage() {

        final var expected = letLoginCredentialsGetStored();

        assertEquals(expected, underTest.extractFromClientStorage().get());

        final var newLoginCredentials = anyLoginCredentials(true);

        underTest.updateLocalStored(newLoginCredentials);

        assertEquals(newLoginCredentials, underTest.extractFromClientStorage().get());
    }

    private void assertEqualsNoUserSpecificCookiesAreStored() {

        assertFalse(underTest.getWrapped().containsKey(KEY_USERNAME), "User name cookie detected");
        assertFalse(underTest.getWrapped().containsKey(KEY_REMEMBER_ME), "Remember me cookie detected");
    }

    private LoginCredentials letLoginCredentialsGetStored() {

        final var expected = anyLoginCredentials(true);

        clientStorage.put(KEY_USERSTORE, expected.getUserStore());
        clientStorage.put(KEY_USERNAME, expected.getUsername());
        clientStorage.put(KEY_REMEMBER_ME, Boolean.TRUE.toString());

        return expected;
    }

    private static LoginCredentials anyLoginCredentials(final boolean rememberMeActive) {
        return LoginCredentials.builder().rememberLoginCredentials(rememberMeActive).username(strings(20, 30).next())
                .userStore(strings(20, 30).next()).build();
    }

}
