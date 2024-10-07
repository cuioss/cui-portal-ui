package de.cuioss.portal.ui.test.tests;

import de.cuioss.portal.core.test.tests.BaseModuleConsistencyTest;
import de.cuioss.test.jsf.producer.JsfObjectsProducer;
import de.cuioss.test.jsf.producer.ServletObjectsFromJSFContextProducer;
import org.jboss.weld.environment.se.Weld;

/**
 * Extension to {@link BaseModuleConsistencyTest} that configures
 * {@link ServletObjectsFromJSFContextProducer} and {@link JsfObjectsProducer}
 */
public class BaseUIModuleConsistencyTest extends BaseModuleConsistencyTest {

    @Override
    protected Weld modifyWeldContainer(Weld weld) {
        return weld.addBeanClasses(ServletObjectsFromJSFContextProducer.class, JsfObjectsProducer.class);
    }
}
