package de.cuioss.portal.ui.runtime.application.locale.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Locale;

import javax.inject.Inject;

import org.jboss.weld.junit5.auto.AddBeanClasses;
import org.junit.jupiter.api.Test;

import de.cuioss.portal.core.test.mocks.core.PortalClientStorageMock;
import de.cuioss.portal.ui.api.locale.PortalLocaleResolver;
import de.cuioss.portal.ui.runtime.application.configuration.LocaleConfiguration;
import de.cuioss.portal.ui.test.junit5.EnablePortalUiEnvironment;
import de.cuioss.test.valueobjects.junit5.contracts.ShouldHandleObjectContracts;
import lombok.Getter;

@EnablePortalUiEnvironment
@AddBeanClasses({ LocaleConfiguration.class, PortalClientStorageMock.class })
class PortalLocaleResolverServiceImplTest implements ShouldHandleObjectContracts<PortalLocaleResolverServiceImpl> {

    @Inject
    @PortalLocaleResolver
    @Getter
    private PortalLocaleResolverServiceImpl underTest;

    @Test
    void shouldProvideCorrectLocales() {
        assertEquals(Locale.ENGLISH, underTest.getLocale());
        assertEquals(3, underTest.getAvailableLocales().size());
    }

    @Test
    void shouldSaveLocaleCorrectly() {
        assertEquals(Locale.ENGLISH, underTest.getLocale());
        underTest.saveUserLocale(Locale.GERMAN);
        assertEquals(Locale.GERMAN, underTest.getLocale());
    }

    @Test
    void shouldFailOnSavingInvalidLocale() {
        assertEquals(Locale.ENGLISH, underTest.getLocale());
        assertThrows(IllegalArgumentException.class, () -> {
            underTest.saveUserLocale(Locale.SIMPLIFIED_CHINESE);
        });
    }

}
