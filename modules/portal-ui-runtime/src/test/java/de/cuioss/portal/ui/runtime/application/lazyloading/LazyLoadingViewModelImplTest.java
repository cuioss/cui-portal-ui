package de.cuioss.portal.ui.runtime.application.lazyloading;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import javax.inject.Inject;

import org.jboss.weld.junit5.auto.AddBeanClasses;
import org.junit.jupiter.api.Test;

import de.cuioss.jsf.api.components.model.resultContent.ResultErrorHandler;
import de.cuioss.portal.ui.test.junit5.EnablePortalUiEnvironment;
import de.cuioss.portal.ui.test.mocks.PortalMessageProducerMock;
import de.cuioss.portal.ui.test.mocks.PortalStickyMessageProducerMock;
import de.cuioss.test.valueobjects.junit5.contracts.ShouldHandleObjectContracts;
import de.cuioss.uimodel.nameprovider.DisplayName;
import de.cuioss.uimodel.result.ResultDetail;
import de.cuioss.uimodel.result.ResultObject;
import de.cuioss.uimodel.result.ResultState;
import lombok.Getter;

@EnablePortalUiEnvironment
@AddBeanClasses({ PortalMessageProducerMock.class, PortalStickyMessageProducerMock.class })
class LazyLoadingViewModelImplTest implements ShouldHandleObjectContracts<LazyLoadingViewModelImpl<String>> {

    @Inject
    @Getter
    private LazyLoadingViewModelImpl<String> underTest;

    @Test
    void testHandleRequestResultAndResetNotificationBox() {
        assertNull(underTest.getNotificationBoxValue());
        underTest.handleRequestResult(new ResultObject<>("", ResultState.VALID), new ResultErrorHandler());
        assertNull(underTest.getNotificationBoxValue());
        underTest.handleRequestResult(
                new ResultObject<>("", ResultState.ERROR, new ResultDetail(new DisplayName("Test")), null),
                new ResultErrorHandler());
        assertEquals(new DisplayName("Test"), underTest.getNotificationBoxValue());
        underTest.resetNotificationBox();
        assertNull(underTest.getNotificationBoxValue());
    }

}
