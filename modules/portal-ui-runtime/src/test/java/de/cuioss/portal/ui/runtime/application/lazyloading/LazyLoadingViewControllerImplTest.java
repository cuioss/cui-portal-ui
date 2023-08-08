package de.cuioss.portal.ui.runtime.application.lazyloading;

import javax.inject.Inject;

import org.jboss.weld.junit5.auto.AddBeanClasses;

import de.cuioss.portal.ui.runtime.application.listener.view.testhelper.StickyMessageProviderMock;
import de.cuioss.portal.ui.test.junit5.EnablePortalUiEnvironment;
import de.cuioss.test.valueobjects.junit5.contracts.ShouldHandleObjectContracts;
import lombok.Getter;

@EnablePortalUiEnvironment
@AddBeanClasses({ StickyMessageProviderMock.class })
class LazyLoadingViewControllerImplTest implements ShouldHandleObjectContracts<LazyLoadingViewControllerImpl> {

    @Inject
    @Getter
    private LazyLoadingViewControllerImpl underTest;
}
