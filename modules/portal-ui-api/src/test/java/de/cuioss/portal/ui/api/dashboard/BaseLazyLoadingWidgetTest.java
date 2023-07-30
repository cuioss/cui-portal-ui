package de.cuioss.portal.ui.api.dashboard;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import javax.inject.Inject;

import org.jboss.weld.junit5.auto.AddBeanClasses;
import org.jboss.weld.junit5.auto.EnableAutoWeld;
import org.junit.jupiter.api.Test;

import de.cuioss.portal.ui.api.ui.lazyloading.LazyLoadingErrorHandler;
import de.cuioss.test.valueobjects.junit5.contracts.ShouldBeNotNull;
import de.cuioss.test.valueobjects.junit5.contracts.ShouldImplementEqualsAndHashCode;
import lombok.Getter;

@EnableAutoWeld
@AddBeanClasses({ LazyLoadingTestSupportProducer.class })
class BaseLazyLoadingWidgetTest
        implements ShouldBeNotNull<TestLazyLoadingWidget>, ShouldImplementEqualsAndHashCode<TestLazyLoadingWidget> {

    @Inject
    @Getter
    private TestLazyLoadingWidget underTest;

    @Test
    void shouldDelegateHappyCase() {
        var underTest = getUnderTest();

        assertDoesNotThrow(() -> underTest.startInitialize());
        assertDoesNotThrow(() -> underTest.processAction(null));
        assertDoesNotThrow(() -> underTest.getRequestId());
        assertDoesNotThrow(() -> underTest.getNotificationBoxState());
        assertDoesNotThrow(() -> underTest.getNotificationBoxValue());
        assertDoesNotThrow(() -> underTest.isInitialized());
        assertDoesNotThrow(() -> underTest.isRenderContent());
    }

    @Test
    void shouldProvideDefaultErrorHandler() {
        assertInstanceOf(LazyLoadingErrorHandler.class, underTest.getErrorHandler());
    }

}
