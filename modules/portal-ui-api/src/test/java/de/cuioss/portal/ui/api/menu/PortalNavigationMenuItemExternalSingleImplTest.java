package de.cuioss.portal.ui.api.menu;

import org.jboss.weld.junit5.auto.AddBeanClasses;
import org.jboss.weld.junit5.auto.EnableAutoWeld;

import de.cuioss.portal.core.test.junit5.EnablePortalConfiguration;
import de.cuioss.test.jsf.junit5.EnableJsfEnvironment;
import de.cuioss.test.jsf.producer.JsfObjectsProducers;
import de.cuioss.test.valueobjects.junit5.contracts.ShouldHandleObjectContracts;

@EnableAutoWeld
@AddBeanClasses({ JsfObjectsProducers.class })
@EnableJsfEnvironment
@EnablePortalConfiguration
class PortalNavigationMenuItemExternalSingleImplTest
        implements ShouldHandleObjectContracts<PortalNavigationMenuItemExternalSingleImpl> {

    @Override
    public PortalNavigationMenuItemExternalSingleImpl getUnderTest() {
        return new PortalNavigationMenuItemExternalSingleImpl();
    }

}
