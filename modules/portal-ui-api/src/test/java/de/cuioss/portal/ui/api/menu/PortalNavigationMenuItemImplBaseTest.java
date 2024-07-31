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
package de.cuioss.portal.ui.api.menu;

import de.cuioss.portal.core.test.junit5.EnablePortalConfiguration;
import de.cuioss.portal.core.test.mocks.configuration.PortalTestConfiguration;
import de.cuioss.test.jsf.junit5.EnableJsfEnvironment;
import de.cuioss.test.jsf.producer.JsfObjectsProducer;
import jakarta.inject.Inject;
import jakarta.inject.Provider;
import lombok.Getter;
import org.jboss.weld.junit5.auto.AddBeanClasses;
import org.jboss.weld.junit5.auto.EnableAutoWeld;
import org.junit.jupiter.api.Test;

import static de.cuioss.portal.configuration.PortalConfigurationKeys.MENU_BASE;
import static de.cuioss.portal.configuration.PortalConfigurationKeys.MENU_TOP_IDENTIFIER;
import static de.cuioss.portal.ui.api.menu.MockPortalNavigationMenuItemImplBase.ID;
import static org.junit.jupiter.api.Assertions.*;

@EnableAutoWeld
@AddBeanClasses({ JsfObjectsProducer.class })
@EnableJsfEnvironment
@EnablePortalConfiguration
class PortalNavigationMenuItemImplBaseTest {

    private static final String BASE = MENU_BASE + ID + ".";

    private static final String ENABLED = BASE + "enabled";
    private static final String ORDER = BASE + "order";
    private static final String PARENT = BASE + "parent";

    private static final Integer DEFAULT_ORDER = 10;
    private static final String DEFAULT_PARENT = MENU_TOP_IDENTIFIER;

    @Inject
    @Getter
    private MockPortalNavigationMenuItemImplBase underTest;

    @Inject
    private Provider<MockPortalNavigationMenuItemImplBase> underTestProvider;

    @Inject
    private PortalTestConfiguration configuration;

    @Test
    void shouldHandleMissingConfigurationGracefully() {
        assertNull(underTest.getParentId());
        assertEquals(Integer.valueOf(-1), underTest.getOrder());
        assertNull(underTest.getIconStyleClass());
        assertNull(underTest.getResolvedLabel());
        assertNull(underTest.getResolvedTitle());
        assertFalse(underTest.isRendered());
    }

    @Test
    void shouldHandleConfiguration() {
        configuration.fireEvent(ENABLED, "true", ORDER, DEFAULT_ORDER.toString());
        configuration.fireEvent(PARENT, DEFAULT_PARENT);

        underTest = underTestProvider.get();
        assertEquals(DEFAULT_PARENT, underTest.getParentId());
        assertEquals(DEFAULT_ORDER, underTest.getOrder());
        assertTrue(underTest.isRendered());
    }
}
