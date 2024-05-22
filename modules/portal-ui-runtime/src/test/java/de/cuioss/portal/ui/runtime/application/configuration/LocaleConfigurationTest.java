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
package de.cuioss.portal.ui.runtime.application.configuration;

import de.cuioss.portal.configuration.PortalConfigurationSource;
import de.cuioss.portal.core.test.junit5.EnablePortalConfiguration;
import de.cuioss.portal.core.test.mocks.configuration.PortalTestConfiguration;
import de.cuioss.test.valueobjects.junit5.contracts.ShouldHandleObjectContracts;
import jakarta.inject.Inject;
import lombok.Getter;
import org.jboss.weld.junit5.auto.EnableAutoWeld;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static de.cuioss.portal.configuration.PortalConfigurationKeys.LOCALES_AVAILABLE;
import static de.cuioss.portal.configuration.PortalConfigurationKeys.LOCALE_DEFAULT;
import static de.cuioss.tools.collect.CollectionLiterals.immutableList;
import static org.junit.jupiter.api.Assertions.assertEquals;

@EnableAutoWeld
@EnablePortalConfiguration
class LocaleConfigurationTest implements ShouldHandleObjectContracts<LocaleConfiguration> {

    @Inject
    @Getter
    private LocaleConfiguration underTest;

    @Inject
    @PortalConfigurationSource
    private PortalTestConfiguration configuration;

    @Test
    void shouldProvideDefaultConfiguration() {
        assertEquals(Locale.GERMAN, underTest.getDefaultLocale());
        assertEquals(2, underTest.getAvailableLocales().size());
    }

    @Test
    void shouldReconfigureOnEvent() {

        configuration.fireEvent(LOCALE_DEFAULT, Locale.JAPANESE.getLanguage(), LOCALES_AVAILABLE,
            Locale.JAPANESE.getLanguage());

        assertEquals(Locale.JAPANESE, underTest.getDefaultLocale());
        assertEquals(immutableList(Locale.JAPANESE), underTest.getAvailableLocales());

        configuration.fireEvent(LOCALE_DEFAULT, Locale.FRENCH.getLanguage(), LOCALES_AVAILABLE,
            Locale.JAPANESE.getLanguage());

        assertEquals(Locale.FRENCH, underTest.getDefaultLocale());
        assertEquals(immutableList(Locale.JAPANESE), underTest.getAvailableLocales());
    }

    @Test
    void defaultsToEnglishOnFailure() {
        configuration.fireEvent(LOCALE_DEFAULT, "b00m", LOCALES_AVAILABLE, "b00m");

        assertEquals(Locale.ENGLISH, underTest.getDefaultLocale());
        assertEquals(immutableList(Locale.ENGLISH), underTest.getAvailableLocales());

        configuration.fireEvent(LOCALE_DEFAULT, "de");
        assertEquals(Locale.GERMAN, underTest.getDefaultLocale());
        assertEquals(immutableList(Locale.GERMAN), underTest.getAvailableLocales());
    }
}
