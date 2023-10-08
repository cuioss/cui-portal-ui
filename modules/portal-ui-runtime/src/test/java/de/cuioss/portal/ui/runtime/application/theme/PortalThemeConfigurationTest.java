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

import static de.cuioss.tools.collect.CollectionLiterals.immutableList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
public class PortalThemeConfigurationTest implements ShouldHandleObjectContracts<PortalThemeConfiguration> {

    @Inject
    @Getter
    private PortalThemeConfiguration underTest;

    @Inject
    @PortalConfigurationSource
    private PortalTestConfiguration configuration;

    public static final String APPLICATION_DEFAULT_CSS = "application-default.css";

    public static final String DEFAULT = "Default";

    public static final String HIGH_CONTRAST = "High-Contrast";

    @Test
    void shouldProvideDefaultConfiguration() {
        assertEquals(PortalThemeConfigurationTest.DEFAULT, underTest.getDefaultTheme());
        assertTrue(underTest.getAvailableThemes().containsAll(
                immutableList(PortalThemeConfigurationTest.DEFAULT, PortalThemeConfigurationTest.HIGH_CONTRAST)));
        assertEquals(PortalThemeConfigurationTest.APPLICATION_DEFAULT_CSS,
                underTest.getCssForThemeName(underTest.getDefaultTheme()));
    }

}
