package de.cuioss.portal.ui.authentication.form;

import org.jboss.weld.environment.se.Weld;

import de.cuioss.portal.core.test.mocks.authentication.PortalAuthenticationFacadeMock;
import de.cuioss.portal.core.test.tests.BaseModuleConsistencyTest;
import de.cuioss.test.jsf.producer.JsfObjectsProducers;
import de.cuioss.test.jsf.producer.ServletObjectsFromJSFContextProducers;

class ModuleConsistencyTest extends BaseModuleConsistencyTest {

    @Override
    protected Weld modifyWeldContainer(Weld weld) {
        return weld.addBeanClasses(ServletObjectsFromJSFContextProducers.class, JsfObjectsProducers.class,
                PortalAuthenticationFacadeMock.class);
    }
}
