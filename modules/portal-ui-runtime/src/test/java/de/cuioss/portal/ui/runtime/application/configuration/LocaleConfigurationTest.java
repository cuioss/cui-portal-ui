package de.cuioss.portal.ui.runtime.application.configuration;

import static de.cuioss.portal.configuration.PortalConfigurationKeys.LOCALES_AVAILABLE;
import static de.cuioss.portal.configuration.PortalConfigurationKeys.LOCALE_DEFAULT;
import static de.cuioss.tools.collect.CollectionLiterals.immutableList;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Locale;

import javax.inject.Inject;

import org.jboss.weld.junit5.auto.EnableAutoWeld;
import org.junit.jupiter.api.Test;

import de.cuioss.portal.configuration.PortalConfigurationSource;
import de.cuioss.portal.core.test.junit5.EnablePortalConfiguration;
import de.cuioss.portal.core.test.mocks.configuration.PortalTestConfiguration;
import de.cuioss.test.valueobjects.junit5.contracts.ShouldHandleObjectContracts;
import lombok.Getter;

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
        assertEquals(3, underTest.getAvailableLocales().size());
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
