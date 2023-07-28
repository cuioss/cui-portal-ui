package de.cuioss.portal.ui.runtime.page;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Locale;

import javax.enterprise.event.Observes;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;

import org.jboss.weld.junit5.auto.AddBeanClasses;
import org.jboss.weld.junit5.auto.EnableAlternatives;
import org.jboss.weld.junit5.auto.ExcludeBeanClasses;
import org.junit.jupiter.api.Test;

import de.cuioss.jsf.api.components.support.DummyComponent;
import de.cuioss.jsf.test.mock.application.ThemeConfigurationMock;
import de.cuioss.portal.ui.api.locale.LocaleChangeEvent;
import de.cuioss.portal.ui.api.ui.context.NavigationHandlerProducer;
import de.cuioss.portal.ui.api.ui.pages.PortalCorePagesPreferences;
import de.cuioss.portal.ui.api.ui.pages.PreferencesPage;
import de.cuioss.portal.ui.runtime.support.PortalLocaleResolverServiceMock;
import de.cuioss.portal.ui.runtime.support.PortalThemeConfigurationMock;
import de.cuioss.portal.ui.runtime.support.PortalThemePersistenceServiceMock;
import de.cuioss.portal.ui.test.junit5.EnablePortalUiEnvironment;
import de.cuioss.portal.ui.test.mocks.PortalLocaleProducerMock;
import de.cuioss.portal.ui.test.tests.AbstractPageBeanTest;
import lombok.Getter;

@EnablePortalUiEnvironment
@AddBeanClasses({ NavigationHandlerProducer.class, PortalThemeConfigurationMock.class,
        PortalThemePersistenceServiceMock.class, PortalLocaleResolverServiceMock.class })
@EnableAlternatives({ PortalThemeConfigurationMock.class, PortalThemePersistenceServiceMock.class,
        PortalLocaleResolverServiceMock.class })
@ExcludeBeanClasses({ PortalLocaleProducerMock.class })
class PreferencesPageBeanTest extends AbstractPageBeanTest<PreferencesPageBean> {

    @Inject
    @PortalCorePagesPreferences
    @Getter
    private PreferencesPageBean underTest;

    private Locale changeEventResult;

    /**
     * Mock Configuration provides 2 locales and 2 themes
     */
    @Test
    void shouldConfigureCorrectly() {
        assertNotNull(underTest);
        assertEquals(2, underTest.getAvailableLocales().size());
        assertEquals(2, underTest.getAvailableThemes().size());
    }

    @Test
    void shouldRedirectOnThemeChange() {
        underTest.themeChangeListener(new ValueChangeEvent(new DummyComponent(), ThemeConfigurationMock.DEFAULT_THEME,
                ThemeConfigurationMock.HIGH_CONTRAST));
        assertNavigatedWithOutcome(PreferencesPage.OUTCOME);
    }

    @Test
    void shouldRedirectOnLocaleChange() {
        assertNull(changeEventResult);
        underTest.localeChangeListener(new ValueChangeEvent(new DummyComponent(), Locale.GERMAN, Locale.ENGLISH));
        assertNavigatedWithOutcome(PreferencesPage.OUTCOME);
        assertEquals(Locale.ENGLISH, changeEventResult);
    }

    void actOnLocaleChangeEven(@Observes @LocaleChangeEvent final Locale newLocale) {
        changeEventResult = newLocale;
    }

}
