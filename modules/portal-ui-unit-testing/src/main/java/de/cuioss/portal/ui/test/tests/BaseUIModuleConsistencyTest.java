package de.cuioss.portal.ui.test.tests;

import org.jboss.weld.environment.se.Weld;

import de.cuioss.portal.core.test.tests.BaseModuleConsistencyTest;
import de.cuioss.test.jsf.producer.JsfObjectsProducers;
import de.cuioss.test.jsf.producer.ServletObjectsFromJSFContextProducers;

/**
 * Extension to {@link BaseModuleConsistencyTest} that configures
 * {@link ServletObjectsFromJSFContextProducers} and {@link JsfObjectsProducers}
 */
public class BaseUIModuleConsistencyTest extends BaseModuleConsistencyTest {

    @Override
    protected Weld modifyWeldContainer(Weld weld) {
        return weld.addBeanClasses(ServletObjectsFromJSFContextProducers.class, JsfObjectsProducers.class);
    }
}
