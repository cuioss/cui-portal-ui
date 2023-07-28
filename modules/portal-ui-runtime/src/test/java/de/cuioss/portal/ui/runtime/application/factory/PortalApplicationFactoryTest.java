package de.cuioss.portal.ui.runtime.application.factory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import javax.faces.FactoryFinder;
import javax.faces.application.ApplicationFactory;

import org.junit.jupiter.api.Test;

import de.cuioss.portal.ui.runtime.application.PortalApplication;
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

        assertEquals(PortalApplication.class, wrapped.getClass());
    }

    @Test
    void shouldBuildFromFactoryWithSetMethod() {
        var applicationFactory = (ApplicationFactory) FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);

        var factory = new PortalApplicationFactory(applicationFactory);
        factory.setApplication(getApplication());
        assertNotNull(factory.getWrapped());

        var wrapped = factory.getApplication();

        assertEquals(PortalApplication.class, wrapped.getClass());
    }

}
