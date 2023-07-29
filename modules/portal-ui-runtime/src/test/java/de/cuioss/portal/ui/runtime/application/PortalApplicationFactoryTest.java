package de.cuioss.portal.ui.runtime.application;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import javax.faces.FactoryFinder;
import javax.faces.application.ApplicationFactory;

import org.junit.jupiter.api.Test;

import de.cuioss.test.jsf.junit5.EnableJsfEnvironment;
import de.cuioss.test.jsf.util.JsfEnvironmentConsumer;
import de.cuioss.test.jsf.util.JsfEnvironmentHolder;
import lombok.Getter;
import lombok.Setter;

@EnableJsfEnvironment
class PortalApplicationFactoryTest implements JsfEnvironmentConsumer {

    @Setter
    @Getter
    private JsfEnvironmentHolder environmentHolder;

    @Test
    void shouldBuildFromFactoryWithGetMethod() {
        var applicationFactory = (ApplicationFactory) FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);

        var factory = new PortalApplicationFactory(applicationFactory);
        assertNotNull(factory.getWrapped());

        var wrapped = factory.getApplication();

        assertInstanceOf(PortalApplication.class, wrapped);
    }

    @Test
    void shouldBuildFromFactoryWithSetMethod() {
        var applicationFactory = (ApplicationFactory) FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);

        var factory = new PortalApplicationFactory(applicationFactory);
        factory.setApplication(getApplication());
        assertNotNull(factory.getWrapped());

        var wrapped = factory.getApplication();

        assertInstanceOf(PortalApplication.class, wrapped);
    }

}
