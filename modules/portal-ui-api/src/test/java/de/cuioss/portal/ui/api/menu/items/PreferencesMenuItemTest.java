package de.cuioss.portal.ui.api.menu.items;

import javax.inject.Inject;
import javax.inject.Provider;

import org.jboss.weld.junit5.auto.AddBeanClasses;
import org.jboss.weld.junit5.auto.EnableAutoWeld;

import de.cuioss.portal.core.test.junit5.EnablePortalConfiguration;
import de.cuioss.portal.ui.api.menu.PortalMenuItem;
import de.cuioss.test.valueobjects.junit5.contracts.ShouldHandleObjectContracts;

@EnableAutoWeld
@AddBeanClasses(PreferencesMenuItem.class)
@EnablePortalConfiguration
class PreferencesMenuItemTest implements ShouldHandleObjectContracts<PreferencesMenuItem> {

    @Inject
    @PortalMenuItem
    private Provider<PreferencesMenuItem> underTestProvider;

    @Override
    public PreferencesMenuItem getUnderTest() {
        return underTestProvider.get();
    }

}
