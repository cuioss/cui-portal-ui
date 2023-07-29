package de.cuioss.portal.ui.api.menu.items;

import javax.inject.Inject;
import javax.inject.Provider;

import org.jboss.weld.junit5.auto.AddBeanClasses;
import org.jboss.weld.junit5.auto.EnableAutoWeld;

import de.cuioss.portal.core.test.junit5.EnablePortalConfiguration;
import de.cuioss.portal.ui.api.menu.PortalMenuItem;
import de.cuioss.test.valueobjects.junit5.contracts.ShouldHandleObjectContracts;

@EnableAutoWeld
@AddBeanClasses(AboutMenuItem.class)
@EnablePortalConfiguration
class AboutMenuItemTest implements ShouldHandleObjectContracts<AboutMenuItem> {

    @Inject
    @PortalMenuItem
    private Provider<AboutMenuItem> underTestProvider;

    @Override
    public AboutMenuItem getUnderTest() {
        return underTestProvider.get();
    }

}
