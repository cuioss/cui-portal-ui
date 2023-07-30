package de.cuioss.portal.ui.test.mocks;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import javax.inject.Inject;

import org.jboss.weld.junit5.auto.EnableAutoWeld;
import org.junit.jupiter.api.Test;

import de.cuioss.portal.ui.api.message.PortalMessageProducer;
import de.cuioss.test.valueobjects.junit5.contracts.ShouldBeNotNull;
import lombok.Getter;

@EnableAutoWeld
class PortalMessageProducerMockTest implements ShouldBeNotNull<PortalMessageProducerMock> {

    @Getter
    @PortalMessageProducer
    @Inject
    private PortalMessageProducerMock underTest;

    @Test
    void shouldProduce() {
        assertDoesNotThrow(() -> underTest.assertThatNoMessageIsPresent());
    }

}
