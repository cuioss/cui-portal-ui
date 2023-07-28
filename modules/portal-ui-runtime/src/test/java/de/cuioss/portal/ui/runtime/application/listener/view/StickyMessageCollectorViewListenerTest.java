package de.cuioss.portal.ui.runtime.application.listener.view;

import static org.junit.jupiter.api.Assertions.assertFalse;

import javax.inject.Inject;

import org.jboss.weld.junit5.auto.AddBeanClasses;
import org.junit.jupiter.api.Test;

import de.cuioss.portal.ui.api.listener.view.PhaseExecution;
import de.cuioss.portal.ui.api.listener.view.PortalRestoreViewListener;
import de.cuioss.portal.ui.api.message.PortalStickyMessageProducer;
import de.cuioss.portal.ui.test.junit5.EnablePortalUiEnvironment;
import de.cuioss.portal.ui.test.mocks.PortalStickyMessageProducerMock;
import de.cuioss.test.valueobjects.junit5.contracts.ShouldHandleObjectContracts;
import lombok.Getter;

@EnablePortalUiEnvironment
@AddBeanClasses({ StickyMessageProviderMock.class })
class StickyMessageCollectorViewListenerTest
        implements ShouldHandleObjectContracts<StickyMessageCollectorViewListener> {

    @Inject
    @PortalRestoreViewListener(PhaseExecution.BEFORE_PHASE)
    @Getter
    private StickyMessageCollectorViewListener underTest;

    @Inject
    @PortalStickyMessageProducer
    private PortalStickyMessageProducerMock stickyMessageProducer;

    @Test
    void shouldCollectMessages() {

        underTest.handleView(null);

        assertFalse(stickyMessageProducer.getMessages().isEmpty(), "Expected one message available");

    }

}
