package de.cuioss.portal.ui.runtime.application.menu;

import javax.inject.Inject;

import org.jboss.weld.junit5.auto.AddBeanClasses;

import de.cuioss.portal.core.test.mocks.authentication.PortalTestUserProducer;
import de.cuioss.portal.ui.test.junit5.EnablePortalUiEnvironment;
import de.cuioss.portal.ui.test.tests.AbstractPageBeanTest;
import lombok.Getter;

@EnablePortalUiEnvironment
@AddBeanClasses({ NavigationMenuProviderImpl.class, PortalTestUserProducer.class })
class NavigationMenuPageBeanTest extends AbstractPageBeanTest<NavigationMenuPageBean> {

    @Inject
    @Getter
    private NavigationMenuPageBean underTest;

}
