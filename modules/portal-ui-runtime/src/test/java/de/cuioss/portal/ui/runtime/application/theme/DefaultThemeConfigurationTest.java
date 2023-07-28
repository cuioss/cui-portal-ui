package de.cuioss.portal.ui.runtime.application.theme;

import static de.cuioss.jsf.test.mock.application.ThemeConfigurationMock.AVAILABLE_THEMES;
import static de.cuioss.jsf.test.mock.application.ThemeConfigurationMock.DEFAULT_THEME;
import static de.cuioss.jsf.test.mock.application.ThemeConfigurationMock.HIGH_CONTRAST;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.inject.Inject;

import org.jboss.weld.junit5.auto.EnableAutoWeld;
import org.junit.jupiter.api.Test;

import de.cuioss.portal.configuration.PortalConfigurationKeys;
import de.cuioss.portal.configuration.PortalConfigurationSource;
import de.cuioss.portal.core.test.junit5.EnablePortalConfiguration;
import de.cuioss.portal.core.test.mocks.configuration.PortalTestConfiguration;
import de.cuioss.portal.ui.api.theme.PortalThemeConfiguration;
import de.cuioss.test.valueobjects.junit5.contracts.ShouldHandleObjectContracts;
import lombok.Getter;

@EnableAutoWeld
@EnablePortalConfiguration
class DefaultThemeConfigurationTest implements ShouldHandleObjectContracts<DefaultThemeConfiguration> {

    @Inject
    @Getter
    @PortalThemeConfiguration
    private DefaultThemeConfiguration underTest;

    @Inject
    @PortalConfigurationSource
    private PortalTestConfiguration configuration;

    @Test
    void shouldProvideDefaultConfiguration() {
        assertEquals(DEFAULT_THEME, underTest.getDefaultTheme());
        assertTrue(underTest.getAvailableThemes().containsAll(AVAILABLE_THEMES));
        assertNotNull(underTest.getCssForThemeName(underTest.getDefaultTheme()));
    }

    @Test

    void shouldReconfigureOnEvent() {
        // do nothing on no change
        configuration.fireEvent();
        shouldProvideDefaultConfiguration();

        configuration.fireEvent(PortalConfigurationKeys.THEME_DEFAULT, HIGH_CONTRAST);

        assertEquals(HIGH_CONTRAST, underTest.getDefaultTheme());
        assertEquals(2, underTest.getAvailableThemes().size());

        configuration.fireEvent(PortalConfigurationKeys.THEME_AVAILABLE, HIGH_CONTRAST);

        assertEquals(HIGH_CONTRAST, underTest.getDefaultTheme());
        assertEquals(1, underTest.getAvailableThemes().size());
        assertEquals(HIGH_CONTRAST, underTest.getAvailableThemes().iterator().next());
    }

}
