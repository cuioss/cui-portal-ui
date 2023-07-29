package de.cuioss.portal.ui.api.ui.context;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import javax.inject.Inject;

import org.jboss.weld.junit5.auto.AddBeanClasses;
import org.jboss.weld.junit5.auto.EnableAutoWeld;
import org.junit.jupiter.api.Test;

import de.cuioss.jsf.api.common.view.ViewDescriptor;
import de.cuioss.test.jsf.junit5.EnableJsfEnvironment;
import de.cuioss.test.valueobjects.junit5.contracts.ShouldBeNotNull;
import lombok.Getter;

@EnableJsfEnvironment
@EnableAutoWeld
@AddBeanClasses({ CurrentViewProducer.class })
class CurrentViewProducerTest implements ShouldBeNotNull<ViewDescriptor> {

    @Inject
    @CuiCurrentView
    @Getter
    private ViewDescriptor underTest;

    @Inject
    private CurrentViewProducer producer;

    @Test
    void shouldHandleCurrentView() {
        assertNotNull(producer.getCurrentView());
    }

}
