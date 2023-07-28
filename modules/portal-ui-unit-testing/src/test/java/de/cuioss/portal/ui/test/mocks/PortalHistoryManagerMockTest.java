package de.cuioss.portal.ui.test.mocks;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import javax.inject.Inject;

import org.jboss.weld.junit5.auto.EnableAutoWeld;
import org.junit.jupiter.api.Test;

import de.cuioss.portal.ui.api.history.PortalHistoryManager;
import de.cuioss.test.valueobjects.junit5.contracts.ShouldBeNotNull;
import lombok.Getter;

@EnableAutoWeld
class PortalHistoryManagerMockTest implements ShouldBeNotNull<PortalHistoryManagerMock> {

    @Getter
    @Inject
    @PortalHistoryManager
    private PortalHistoryManagerMock underTest;

    @Test
    void shouldDefaultSensibly() {
        assertNotNull(underTest.getCurrentView());
    }

}
