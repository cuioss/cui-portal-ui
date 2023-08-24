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
