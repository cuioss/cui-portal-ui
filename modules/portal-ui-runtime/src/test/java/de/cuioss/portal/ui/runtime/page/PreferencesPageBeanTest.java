/*
 * Copyright © 2025 CUI-OpenSource-Software (info@cuioss.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.cuioss.portal.ui.runtime.page;

import de.cuioss.jsf.api.components.support.DummyComponent;
import de.cuioss.portal.common.locale.LocaleChangeEvent;
import de.cuioss.portal.core.test.mocks.core.PortalClientStorageMock;
import de.cuioss.portal.ui.api.context.NavigationHandlerProducer;
import de.cuioss.portal.ui.api.pages.PreferencesPage;
import de.cuioss.portal.ui.runtime.application.locale.PortalLocaleManagerBean;
import de.cuioss.portal.ui.runtime.application.theme.PortalThemeConfiguration;
import de.cuioss.portal.ui.runtime.application.theme.PortalThemeConfigurationTest;
import de.cuioss.portal.ui.runtime.application.theme.UserThemeBean;
import de.cuioss.portal.ui.runtime.support.PortalLocaleResolverServiceMock;
import de.cuioss.portal.ui.test.junit5.EnablePortalUiEnvironment;
import de.cuioss.portal.ui.test.mocks.PortalLocaleProducerMock;
import de.cuioss.portal.ui.test.tests.AbstractPageBeanTest;
import de.cuioss.test.jsf.junit5.NavigationAsserts;
import jakarta.enterprise.event.Observes;
import jakarta.faces.event.ValueChangeEvent;
import jakarta.inject.Inject;
import lombok.Getter;
import org.jboss.weld.junit5.ExplicitParamInjection;
import org.jboss.weld.junit5.auto.AddBeanClasses;
import org.jboss.weld.junit5.auto.EnableAlternatives;
import org.jboss.weld.junit5.auto.ExcludeBeanClasses;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

@EnablePortalUiEnvironment
@ExplicitParamInjection
@AddBeanClasses({NavigationHandlerProducer.class, PortalThemeConfiguration.class, UserThemeBean.class,
        PortalLocaleResolverServiceMock.class, PortalClientStorageMock.class, PortalLocaleManagerBean.class})
@EnableAlternatives({PortalLocaleResolverServiceMock.class})
@ExcludeBeanClasses({PortalLocaleProducerMock.class})
class PreferencesPageBeanTest extends AbstractPageBeanTest<PreferencesPageBean> {

    @Inject
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
    void shouldRedirectOnThemeChange(NavigationAsserts navigationAsserts) {
        underTest.themeChangeListener(new ValueChangeEvent(new DummyComponent(), PortalThemeConfigurationTest.DEFAULT,
                PortalThemeConfigurationTest.HIGH_CONTRAST));
        navigationAsserts.assertNavigatedWithOutcome(PreferencesPage.OUTCOME);
    }

    @Test
    void shouldRedirectOnLocaleChange(NavigationAsserts navigationAsserts) {
        assertNull(changeEventResult);
        underTest.localeChangeListener(new ValueChangeEvent(new DummyComponent(), Locale.GERMAN, Locale.ENGLISH));
        navigationAsserts.assertNavigatedWithOutcome(PreferencesPage.OUTCOME);
        assertEquals(Locale.ENGLISH, changeEventResult);
    }

    void actOnLocaleChangeEven(@Observes @LocaleChangeEvent final Locale newLocale) {
        changeEventResult = newLocale;
    }

}
