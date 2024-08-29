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

import de.cuioss.portal.core.test.junit5.EnablePortalConfiguration;
import de.cuioss.portal.core.test.mocks.configuration.PortalTestConfiguration;
import jakarta.inject.Inject;
import lombok.Getter;
import org.jboss.weld.environment.util.Collections;
import org.jboss.weld.junit5.auto.EnableAutoWeld;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@EnableAutoWeld
@EnablePortalConfiguration
class ELConfigurationResolverBeanTest {

    @Inject
    @Getter
    private ELConfigurationResolverBean underTest;

    @Inject
    private PortalTestConfiguration configuration;

    @Test
    void testGetString() {
        configuration.update("abc", "def");
        assertEquals("def", underTest.getString("abc"));
    }

    @Test
    void testGetKeys() {
        configuration.update("abc", "def");
        assertTrue(Collections.asList(underTest.getKeys()).contains("abc"));
    }
}
