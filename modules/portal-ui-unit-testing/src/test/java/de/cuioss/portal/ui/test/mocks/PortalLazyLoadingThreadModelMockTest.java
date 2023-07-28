package de.cuioss.portal.ui.test.mocks;

import javax.inject.Inject;

import org.jboss.weld.junit5.auto.EnableAutoWeld;

import de.cuioss.test.valueobjects.junit5.contracts.ShouldBeNotNull;
import lombok.Getter;

@EnableAutoWeld
class PortalLazyLoadingThreadModelMockTest implements ShouldBeNotNull<PortalLazyLoadingThreadModelMock<?>> {

    @Inject
    @Getter
    private PortalLazyLoadingThreadModelMock<?> underTest;

}
