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
package de.cuioss.portal.ui.runtime.application.locale;

import de.cuioss.portal.common.locale.LocaleChangeEvent;
import de.cuioss.portal.ui.runtime.support.PortalLocaleResolverServiceMock;
import de.cuioss.portal.ui.test.junit5.EnablePortalUiEnvironment;
import de.cuioss.portal.ui.test.mocks.PortalLocaleProducerMock;
import de.cuioss.test.valueobjects.junit5.contracts.ShouldHandleObjectContracts;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import lombok.Getter;
import org.jboss.weld.junit5.auto.AddBeanClasses;
import org.jboss.weld.junit5.auto.EnableAlternatives;
import org.jboss.weld.junit5.auto.ExcludeBeanClasses;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@EnablePortalUiEnvironment
@AddBeanClasses({PortalLocaleResolverServiceMock.class})
@EnableAlternatives({PortalLocaleResolverServiceMock.class})
@ExcludeBeanClasses(PortalLocaleProducerMock.class)
class PortalLocaleManagerBeanTest implements ShouldHandleObjectContracts<PortalLocaleManagerBean> {

    @Inject
    @Getter
    private PortalLocaleManagerBean underTest;

    private Locale changeEventResult;

    @Test
    void shouldProvideCorrectLocales() {
        assertEquals(Locale.ENGLISH, underTest.getUserLocale());
        assertEquals(2, underTest.getAvailableLocales().size());
    }

    @Test
    void shouldSaveLocaleCorrectly() {
        assertNull(changeEventResult);
        assertEquals(Locale.ENGLISH, underTest.getUserLocale());
        underTest.saveUserLocale(Locale.GERMAN);
        assertEquals(Locale.GERMAN, changeEventResult);
        assertEquals(Locale.GERMAN, underTest.getUserLocale());
        underTest.saveUserLocale(Locale.ENGLISH);
        assertEquals(Locale.ENGLISH, underTest.getUserLocale());
    }

    @Test
    void shouldFailOnSavingInvalidLocale() {
        assertThrows(IllegalArgumentException.class, () -> underTest.saveUserLocale(Locale.SIMPLIFIED_CHINESE));
    }

    void actOnLocaleChangeEven(@Observes @LocaleChangeEvent final Locale newLocale) {
        changeEventResult = newLocale;
    }

}
