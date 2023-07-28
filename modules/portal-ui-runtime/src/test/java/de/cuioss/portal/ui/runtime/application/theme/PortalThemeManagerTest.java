package de.cuioss.portal.ui.runtime.application.theme;

import static de.cuioss.jsf.test.mock.application.ThemeConfigurationMock.DEFAULT_THEME;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import javax.inject.Inject;

import org.jboss.weld.junit5.auto.AddBeanClasses;
import org.jboss.weld.junit5.auto.EnableAlternatives;
import org.junit.jupiter.api.Test;

import de.cuioss.jsf.api.application.theme.ThemeNameProducer;
import de.cuioss.portal.ui.api.theme.PortalThemeNameProducer;
import de.cuioss.portal.ui.runtime.support.PortalThemePersistenceServiceMock;
import de.cuioss.portal.ui.test.junit5.EnablePortalUiEnvironment;
import de.cuioss.test.valueobjects.junit5.contracts.ShouldHandleObjectContracts;
import lombok.Getter;

@EnablePortalUiEnvironment
@AddBeanClasses({ PortalThemePersistenceServiceMock.class })
@EnableAlternatives({ PortalThemePersistenceServiceMock.class })
class PortalThemeManagerTest implements ShouldHandleObjectContracts<PortalThemeManager> {

    @Inject
    @Getter
    private PortalThemeManager underTest;

    @Inject
    @PortalThemeNameProducer
    private ThemeNameProducer themeNameProducer;

    @Test
    void shouldProduceThemeNameProducer() {
        assertNotNull(themeNameProducer);
        assertEquals(DEFAULT_THEME, themeNameProducer.getTheme());
    }

}
