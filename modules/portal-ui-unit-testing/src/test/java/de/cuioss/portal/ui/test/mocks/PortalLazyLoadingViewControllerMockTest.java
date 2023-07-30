package de.cuioss.portal.ui.test.mocks;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import javax.inject.Inject;

import org.easymock.EasyMock;
import org.jboss.weld.junit5.auto.EnableAutoWeld;
import org.junit.jupiter.api.Test;

import de.cuioss.portal.ui.api.ui.lazyloading.LazyLoadingRequest;
import de.cuioss.test.valueobjects.junit5.contracts.ShouldBeNotNull;
import lombok.Getter;

@EnableAutoWeld
class PortalLazyLoadingViewControllerMockTest implements ShouldBeNotNull<PortalLazyLoadingViewControllerMock> {

    @Inject
    @Getter
    private PortalLazyLoadingViewControllerMock underTest;

    @Test
    void shouldHandle() {
        var request = (LazyLoadingRequest<?>) EasyMock.createNiceMock(LazyLoadingRequest.class);
        assertDoesNotThrow(() -> underTest.startRequest(request));
    }

}
