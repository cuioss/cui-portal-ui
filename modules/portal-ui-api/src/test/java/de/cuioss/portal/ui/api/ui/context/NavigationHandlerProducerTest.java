package de.cuioss.portal.ui.api.ui.context;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import javax.faces.application.NavigationHandler;
import javax.inject.Inject;
import javax.inject.Provider;

import org.jboss.weld.junit5.auto.AddBeanClasses;
import org.jboss.weld.junit5.auto.EnableAutoWeld;
import org.junit.jupiter.api.Test;

import de.cuioss.test.jsf.junit5.EnableJsfEnvironment;
import de.cuioss.test.jsf.producer.JsfObjectsProducers;

@EnableJsfEnvironment
@EnableAutoWeld
@AddBeanClasses({ NavigationHandlerProducer.class, JsfObjectsProducers.class })
class NavigationHandlerProducerTest {

    @Inject
    @CuiNavigationHandler
    private Provider<NavigationHandler> handlerProvider;

    @Inject
    private NavigationHandlerProducer navigationHandlerProducer;

    @Test
    void shouldProduce() {
        assertInstanceOf(NavigationHandler.class, handlerProvider.get());
        assertInstanceOf(NavigationHandler.class, navigationHandlerProducer.getNavigationHandler());
    }

}
