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
package de.cuioss.portal.ui.runtime.application.menu;

import de.cuioss.jsf.api.components.model.menu.NavigationMenuItemContainer;
import de.cuioss.jsf.api.converter.StringIdentConverter;
import de.cuioss.portal.configuration.PortalConfigurationKeys;
import de.cuioss.portal.core.test.mocks.authentication.PortalAuthenticationFacadeMock;
import de.cuioss.portal.core.test.mocks.authentication.PortalTestUserProducer;
import de.cuioss.portal.core.test.mocks.configuration.PortalTestConfiguration;
import de.cuioss.portal.ui.api.menu.items.*;
import de.cuioss.portal.ui.test.junit5.EnablePortalUiEnvironment;
import de.cuioss.portal.ui.test.mocks.PortalMirrorResourceBundle;
import de.cuioss.test.jsf.config.ComponentConfigurator;
import de.cuioss.test.jsf.config.decorator.ComponentConfigDecorator;
import de.cuioss.test.valueobjects.junit5.contracts.ShouldHandleObjectContracts;
import jakarta.inject.Inject;
import lombok.Getter;
import org.jboss.weld.junit5.auto.AddBeanClasses;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static de.cuioss.portal.configuration.PortalConfigurationKeys.MENU_BASE;
import static de.cuioss.portal.configuration.PortalConfigurationKeys.MENU_TOP_IDENTIFIER;
import static org.junit.jupiter.api.Assertions.*;

@EnablePortalUiEnvironment
@AddBeanClasses({AccountMenuItem.class, AboutMenuItem.class, LogoutMenuItem.class, EmptyNavigationContainer.class,
        PreferencesMenuItem.class, UserMenuItem.class, TestNavigationMenuItemSingleWithIdA.class,
        TestNavigationMenuItemSingleWithIdB.class, TestNavigationMenuItemSingleWithoutId.class,
        PortalMirrorResourceBundle.class, PortalTestUserProducer.class})
class NavigationMenuProviderImplTest
        implements ShouldHandleObjectContracts<NavigationMenuProviderImpl>, ComponentConfigurator {

    @Inject
    @Getter
    private NavigationMenuProviderImpl underTest;

    @Inject
    private PortalTestConfiguration configuration;

    @BeforeEach
    void before() {
        configuration.update(MENU_BASE + "accountMenuItem.enabled", "true");
    }

    /**
     * Checks whether the actual configuration results it the portal default.
     */
    @Test
    void shouldProvideDefaultConfiguration() {
        var topLevelElements = underTest.getNavigationMenuRoots();
        assertEquals(2, topLevelElements.size());
        assertEquals(UserMenuItem.MENU_ID, topLevelElements.get(0).getId());
        assertEquals(LogoutMenuItem.MENU_ID, topLevelElements.get(1).getId());

        // Check UserMenu
        var userMenu = topLevelElements.get(0);
        assertEquals(PortalAuthenticationFacadeMock.USER, ((NavigationMenuItemContainer) userMenu).getResolvedLabel());
        assertEquals(3, ((NavigationMenuItemContainer) userMenu).getChildren().size());

        assertEquals(PreferencesMenuItem.MENU_ID,
                ((NavigationMenuItemContainer) userMenu).getChildren().get(0).getId());
        assertEquals(AccountMenuItem.MENU_ID, ((NavigationMenuItemContainer) userMenu).getChildren().get(1).getId());
        assertEquals(AboutMenuItem.MENU_ID, ((NavigationMenuItemContainer) userMenu).getChildren().get(2).getId());
    }

    @Test
    void shouldIgnoreEmptyContainer() {
        configuration.update(MENU_BASE + EmptyNavigationContainer.MENU_ID + ".order", "40");
        configuration.update(MENU_BASE + EmptyNavigationContainer.MENU_ID + ".enabled", "true");
        configuration.update(MENU_BASE + EmptyNavigationContainer.MENU_ID + ".parent", MENU_TOP_IDENTIFIER);

        var topLevelElements = underTest.getNavigationMenuRoots();
        assertEquals(2, topLevelElements.size());
        assertEquals(UserMenuItem.MENU_ID, topLevelElements.get(0).getId());
        assertEquals(LogoutMenuItem.MENU_ID, topLevelElements.get(1).getId());
    }

    @Test
    void shouldFindById() {
        assertFalse(underTest.getMenuItemById(null).isPresent());
        assertFalse(underTest.getMenuItemById("NotThere").isPresent());
        var userMenu = underTest.getMenuItemById(UserMenuItem.MENU_ID);
        assertTrue(userMenu.isPresent());
        assertEquals(3, ((NavigationMenuItemContainer) userMenu.get()).getChildren().size());
    }

    @Test
    void shouldFindContainerById() {
        assertFalse(underTest.getContainerMenuItemById(null).isPresent());
        assertFalse(underTest.getContainerMenuItemById("NotThere").isPresent());
        assertFalse(underTest.getContainerMenuItemById(AboutMenuItem.MENU_ID).isPresent());
        assertTrue(underTest.getContainerMenuItemById(UserMenuItem.MENU_ID).isPresent());
    }

    @Test
    void shouldFindByIds() {
        assertTrue(underTest.getMenuItemsByIds().isEmpty());
        assertTrue(underTest.getMenuItemsByIds("").isEmpty());
        assertFalse(underTest.getMenuItemsByIds(UserMenuItem.MENU_ID).isEmpty());
        assertEquals(2, underTest.getMenuItemsByIds(UserMenuItem.MENU_ID, PreferencesMenuItem.MENU_ID).size());
    }

    @Test
    void shouldHandleSeparator() {
        // Separator
        configuration.update(MENU_BASE + "separator1.order", "47");
        configuration.update(MENU_BASE + "separator1.enabled", "true");
        configuration.update(MENU_BASE + "separator1.parent", UserMenuItem.MENU_ID);
        configuration.update(MENU_BASE + "separator2.order", "49");
        configuration.update(MENU_BASE + "separator2.enabled", "true");
        configuration.update(MENU_BASE + "separator2.parent", UserMenuItem.MENU_ID);
        assertEquals(4, underTest.getContainerMenuItemById(UserMenuItem.MENU_ID).get().getChildren().size());
    }

    @Test
    void shouldIgnoreNotRenderedSeparator() {
        // Separator
        configuration.update(MENU_BASE + "separator1.order", "47");
        configuration.update(MENU_BASE + "separator1.enabled", "false");
        configuration.update(MENU_BASE + "separator1.parent", UserMenuItem.MENU_ID);
        assertEquals(3, underTest.getContainerMenuItemById(UserMenuItem.MENU_ID).get().getChildren().size());
    }

    @Test
    void shouldIgnoreSeparatorWithInvalidParent() {
        // Separator
        configuration.update(MENU_BASE + "separator1.order", "47");
        configuration.update(MENU_BASE + "separator1.enabled", "true");
        configuration.update(MENU_BASE + "separator1.parent", "NotThere");
        // Hm hard to test. At least it should not have been added to existing container
        assertEquals(3, underTest.getContainerMenuItemById(UserMenuItem.MENU_ID).get().getChildren().size());
    }

    @Override
    public void configureComponents(final ComponentConfigDecorator decorator) {
        decorator.registerConverter(StringIdentConverter.class, String.class);
    }

    @Test
    void testApplicationMenusWithoutConfig() {
        assertNotNull(underTest.getMenuItemsByParentId("application"));
        assertTrue(underTest.getMenuItemsByParentId("application").isEmpty());
    }

    @Test
    void testApplicationMenusWithConfigForA() {
        configuration.update(PortalConfigurationKeys.MENU_BASE + "A.order", "10");
        configuration.update(PortalConfigurationKeys.MENU_BASE + "A.parent", "application");

        assertNotNull(underTest.getMenuItemsByParentId("application"));
        assertEquals(1, underTest.getMenuItemsByParentId("application").size());
        assertEquals("A", underTest.getMenuItemsByParentId("application").get(0).getId());
    }

    @Test
    void testApplicationMenusWithConfigForAAndB() {
        configuration.update(PortalConfigurationKeys.MENU_BASE + "A.order", "10");
        configuration.update(PortalConfigurationKeys.MENU_BASE + "A.parent", "application");
        configuration.update(PortalConfigurationKeys.MENU_BASE + "B.order", "20");
        configuration.update(PortalConfigurationKeys.MENU_BASE + "B.parent", "application");

        assertNotNull(underTest.getMenuItemsByParentId("application"));
        assertEquals(2, underTest.getMenuItemsByParentId("application").size());
        assertEquals("A", underTest.getMenuItemsByParentId("application").get(0).getId());
        assertEquals("B", underTest.getMenuItemsByParentId("application").get(1).getId());
    }
}
