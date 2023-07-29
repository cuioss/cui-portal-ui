package de.cuioss.portal.ui.api.menu.items;

import javax.inject.Inject;
import javax.inject.Provider;

import org.jboss.weld.junit5.auto.AddBeanClasses;
import org.jboss.weld.junit5.auto.EnableAutoWeld;

import de.cuioss.portal.core.test.junit5.EnablePortalConfiguration;
import de.cuioss.portal.core.test.mocks.authentication.PortalTestUserProducer;
import de.cuioss.portal.ui.api.menu.PortalMenuItem;
import de.cuioss.portal.ui.api.test.support.PortalResourceBundleMock;
import de.cuioss.test.valueobjects.junit5.contracts.ShouldHandleObjectContracts;

@EnableAutoWeld
@AddBeanClasses({ UserMenuItem.class, PortalTestUserProducer.class, PortalResourceBundleMock.class })
@EnablePortalConfiguration
class UserMenuItemTest implements ShouldHandleObjectContracts<UserMenuItem> {

    @Inject
    @PortalMenuItem
    private Provider<UserMenuItem> underTestProvider;

    @Override
    public UserMenuItem getUnderTest() {
        return underTestProvider.get();
    }

}
