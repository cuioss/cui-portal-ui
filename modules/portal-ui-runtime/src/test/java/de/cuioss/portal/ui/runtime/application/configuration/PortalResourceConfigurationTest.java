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
package de.cuioss.portal.ui.runtime.application.configuration;

import static de.cuioss.portal.configuration.PortalConfigurationKeys.RESOURCE_HANDLED_LIBRARIES;
import static de.cuioss.portal.configuration.PortalConfigurationKeys.RESOURCE_HANDLED_SUFFIXES;
import static de.cuioss.portal.configuration.PortalConfigurationKeys.RESOURCE_VERSION;
import static de.cuioss.test.generator.Generators.letterStrings;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import javax.inject.Inject;

import org.jboss.weld.junit5.auto.EnableAutoWeld;
import org.junit.jupiter.api.Test;

import de.cuioss.portal.configuration.PortalConfigurationSource;
import de.cuioss.portal.core.test.junit5.EnablePortalConfiguration;
import de.cuioss.portal.core.test.mocks.configuration.PortalTestConfiguration;
import de.cuioss.test.generator.TypedGenerator;
import de.cuioss.test.valueobjects.junit5.contracts.ShouldHandleObjectContracts;
import de.cuioss.tools.string.MoreStrings;
import lombok.Getter;

@EnableAutoWeld
@EnablePortalConfiguration
class PortalResourceConfigurationTest implements ShouldHandleObjectContracts<PortalResourceConfiguration> {

    private final TypedGenerator<String> strings = letterStrings(1, 11);

    @Inject
    @Getter
    private PortalResourceConfiguration underTest;

    @Inject
    @PortalConfigurationSource
    private PortalTestConfiguration configuration;

    @Test
    void shouldProvideDefaults() {
        assertNotNull(MoreStrings.emptyToNull(underTest.getVersion()));
        assertEquals(5, underTest.getHandledLibraries().size());
        assertEquals(6, underTest.getHandledSuffixes().size());
    }

    @Test
    void shouldReconfigure() {
        // Ensure initialization

        // Should detect changed version
        final var version = strings.next();
        configuration.fireEvent(RESOURCE_VERSION, version);
        assertEquals(version, underTest.getVersion());

        // Should detect changed libraries
        configuration.fireEvent(RESOURCE_HANDLED_LIBRARIES, strings.next());
        assertEquals(1, underTest.getHandledLibraries().size());
        assertEquals(version, underTest.getVersion());

        // Should detect changed suffixes
        configuration.fireEvent(RESOURCE_HANDLED_SUFFIXES, strings.next());
        assertEquals(1, underTest.getHandledLibraries().size());
        assertEquals(version, underTest.getVersion());
        assertEquals(1, underTest.getHandledSuffixes().size());

    }
}
