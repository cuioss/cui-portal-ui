package de.cuioss.portal.ui.runtime.application.theme;

import static de.cuioss.portal.configuration.PortalConfigurationKeys.THEME_DEFAULT;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import jakarta.inject.Inject;

import org.jboss.weld.junit5.auto.AddBeanClasses;
import org.junit.jupiter.api.Test;

import de.cuioss.portal.core.storage.PortalClientStorage;
import de.cuioss.portal.core.test.mocks.core.PortalClientStorageMock;
import de.cuioss.portal.ui.test.junit5.EnablePortalUiEnvironment;
import de.cuioss.test.generator.Generators;
import de.cuioss.test.generator.junit.EnableGeneratorController;
import de.cuioss.test.valueobjects.junit5.contracts.ShouldHandleObjectContracts;
import lombok.Getter;

@EnablePortalUiEnvironment
@AddBeanClasses({ PortalThemeConfiguration.class, PortalClientStorageMock.class })
@EnableGeneratorController
class UserThemeBeanTest implements ShouldHandleObjectContracts<UserThemeBean> {

    @Inject
    @Getter
    private UserThemeBean underTest;

    @Inject
    @PortalClientStorage
    private PortalClientStorageMock clientStorage;

    @Test
    void shouldHandleSelectedTheme() {
        assertEquals(PortalThemeConfigurationTest.DEFAULT, underTest.getTheme());
        assertDoesNotThrow(() -> underTest.saveTheme(PortalThemeConfigurationTest.HIGH_CONTRAST));
        assertEquals(PortalThemeConfigurationTest.HIGH_CONTRAST, underTest.getTheme());

        assertTrue(clientStorage.containsKey(THEME_DEFAULT));
        assertEquals(PortalThemeConfigurationTest.HIGH_CONTRAST, clientStorage.get(THEME_DEFAULT));
    }

    @Test
    void shouldFailOnSavingInvalidThemeName() {
        var next = Generators.strings().next();
        assertThrows(IllegalArgumentException.class, () -> {
            underTest.saveTheme(next);
        });
    }

    @Test
    void shouldDeriveThemeNameFromClientStorage() {
        clientStorage.put(THEME_DEFAULT, PortalThemeConfigurationTest.HIGH_CONTRAST);
        assertEquals(PortalThemeConfigurationTest.HIGH_CONTRAST, underTest.getTheme());
    }

    @Test
    void shouldDefaultThemeNameOnInvalidClientStorage() {
        clientStorage.put(THEME_DEFAULT, Generators.letterStrings().next());
        assertEquals(PortalThemeConfigurationTest.DEFAULT, underTest.getTheme());
    }

    @Test
    void shouldReturnApplicationCss() {
        assertEquals(PortalThemeConfigurationTest.APPLICATION_DEFAULT_CSS, underTest.resolveFinalThemeCssName());
    }

}
