package de.cuioss.portal.ui.test.mocks;

import javax.inject.Inject;

import org.jboss.weld.junit5.auto.EnableAutoWeld;

import de.cuioss.test.valueobjects.junit5.contracts.ShouldBeNotNull;
import lombok.Getter;

@EnableAutoWeld
class PortalLazyLoadingViewControllerMockTest implements ShouldBeNotNull<PortalLazyLoadingViewControllerMock> {

    @Inject
    @Getter
    private PortalLazyLoadingViewControllerMock underTest;

}
