package de.cuioss.portal.ui.api.ui.context;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import javax.inject.Inject;

import org.jboss.weld.junit5.auto.AddBeanClasses;
import org.jboss.weld.junit5.auto.EnableAutoWeld;
import org.junit.jupiter.api.Test;

@EnableAutoWeld
@AddBeanClasses({ CurrentViewProducer.class })
class CurrentViewProducerTestWOFacesContextTest {

    @Inject
    private CurrentViewProducer producer;

    @Test
    void shouldHandleCurrentView() {
        assertNotNull(producer.getCurrentView());
    }

}
