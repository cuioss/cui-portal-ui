package de.cuioss.portal.ui.test.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.jboss.weld.junit5.auto.AddBeanClasses;
import org.junit.jupiter.api.Test;

import de.cuioss.portal.ui.api.menu.items.AboutMenuItem;
import de.cuioss.portal.ui.api.menu.items.AccountMenuItem;
import de.cuioss.portal.ui.test.junit5.EnablePortalUiEnvironment;
import de.cuioss.portal.ui.test.mocks.PortalMirrorResourceBundle;
import de.cuioss.test.jsf.config.BeanConfigurator;
import de.cuioss.test.jsf.config.decorator.BeanConfigDecorator;

@EnablePortalUiEnvironment
@AddBeanClasses({ AboutMenuItem.class, AccountMenuItem.class })
class PortalNavigationMenuItemsTestTest extends PortalNavigationMenuItemsTest implements BeanConfigurator {

    @Test
    void shouldFilterNoType() {
        assertEquals(2, getFilteredInstances().size());
    }

    @Override
    public void configureBeans(BeanConfigDecorator decorator) {
        decorator.register(new PortalMirrorResourceBundle());

    }

}
