package de.cuioss.portal.ui.runtime.application.theme.impl;

import static de.cuioss.jsf.test.mock.application.ThemeConfigurationMock.DEFAULT_THEME;
import static de.cuioss.jsf.test.mock.application.ThemeConfigurationMock.HIGH_CONTRAST;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import javax.inject.Inject;

import org.jboss.weld.junit5.auto.AddBeanClasses;
import org.jboss.weld.junit5.auto.EnableAlternatives;
import org.junit.jupiter.api.Test;

import de.cuioss.portal.core.test.mocks.core.PortalClientStorageMock;
import de.cuioss.portal.ui.api.theme.PortalThemePersistencesService;
import de.cuioss.portal.ui.runtime.support.PortalThemeConfigurationMock;
import de.cuioss.portal.ui.test.junit5.EnablePortalUiEnvironment;
import de.cuioss.test.valueobjects.junit5.contracts.ShouldHandleObjectContracts;
import lombok.Getter;

@EnablePortalUiEnvironment
@AddBeanClasses({ PortalClientStorageMock.class, PortalThemeConfigurationMock.class })
@EnableAlternatives({ PortalThemeConfigurationMock.class })
class PortalThemePersistenceServiceImplTest implements ShouldHandleObjectContracts<PortalThemePersistenceServiceImpl> {

    private static final String NOT_THERE = "notThere";

    @Inject
    @PortalThemePersistencesService
    @Getter
    private PortalThemePersistenceServiceImpl underTest;

    @Test
    void shouldChangeThemeCorrectly() {
        assertEquals(DEFAULT_THEME, underTest.getTheme());
        underTest.saveTheme(HIGH_CONTRAST);
        assertEquals(HIGH_CONTRAST, underTest.getTheme());
    }

    @Test
    void shouldFailOnIncorrectTheme() {
        assertThrows(IllegalArgumentException.class, () -> {
            underTest.saveTheme(NOT_THERE);
        });
    }

}
