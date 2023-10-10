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
package de.cuioss.portal.ui.runtime.application.locale.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Locale;

import javax.inject.Inject;

import org.jboss.weld.junit5.auto.AddBeanClasses;
import org.junit.jupiter.api.Test;

import de.cuioss.portal.core.test.mocks.core.PortalClientStorageMock;
import de.cuioss.portal.ui.runtime.application.configuration.LocaleConfiguration;
import de.cuioss.portal.ui.test.junit5.EnablePortalUiEnvironment;
import de.cuioss.test.valueobjects.junit5.contracts.ShouldHandleObjectContracts;
import lombok.Getter;

@EnablePortalUiEnvironment
@AddBeanClasses({ LocaleConfiguration.class, PortalClientStorageMock.class })
class PortalLocaleResolverServiceImplTest implements ShouldHandleObjectContracts<PortalLocaleResolverServiceImpl> {

    @Inject
    @Getter
    private PortalLocaleResolverServiceImpl underTest;

    @Test
    void shouldProvideCorrectLocales() {
        assertEquals(Locale.ENGLISH, underTest.resolveUserLocale());
        assertEquals(3, underTest.getAvailableLocales().size());
    }

    @Test
    void shouldSaveLocaleCorrectly() {
        assertEquals(Locale.ENGLISH, underTest.resolveUserLocale());
        underTest.saveUserLocale(Locale.GERMAN);
        assertEquals(Locale.GERMAN, underTest.resolveUserLocale());
    }

    @Test
    void shouldFailOnSavingInvalidLocale() {
        assertEquals(Locale.ENGLISH, underTest.resolveUserLocale());
        assertThrows(IllegalArgumentException.class, () -> {
            underTest.saveUserLocale(Locale.SIMPLIFIED_CHINESE);
        });
    }

}
