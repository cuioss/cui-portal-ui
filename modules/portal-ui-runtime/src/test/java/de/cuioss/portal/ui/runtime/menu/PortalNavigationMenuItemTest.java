package de.cuioss.portal.ui.runtime.menu;

import org.jboss.weld.junit5.auto.AddBeanClasses;

import de.cuioss.portal.ui.api.menu.items.AboutMenuItem;
import de.cuioss.portal.ui.api.menu.items.AccountMenuItem;
import de.cuioss.portal.ui.api.menu.items.LogoutMenuItem;
import de.cuioss.portal.ui.api.menu.items.PreferencesMenuItem;
import de.cuioss.portal.ui.test.junit5.EnablePortalUiEnvironment;
import de.cuioss.portal.ui.test.tests.PortalNavigationMenuItemsTest;

@EnablePortalUiEnvironment
@AddBeanClasses({ AboutMenuItem.class, AccountMenuItem.class, LogoutMenuItem.class, PreferencesMenuItem.class })
class PortalNavigationMenuItemTest extends PortalNavigationMenuItemsTest {

}
