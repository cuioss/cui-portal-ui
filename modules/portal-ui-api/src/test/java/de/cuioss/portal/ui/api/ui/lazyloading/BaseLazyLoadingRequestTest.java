package de.cuioss.portal.ui.api.ui.lazyloading;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import javax.inject.Inject;

import org.jboss.weld.junit5.auto.AddBeanClasses;
import org.jboss.weld.junit5.auto.EnableAutoWeld;
import org.junit.jupiter.api.Test;

import de.cuioss.portal.ui.api.dashboard.LazyLoadingTestSupportProducer;
import de.cuioss.test.valueobjects.junit5.contracts.ShouldBeNotNull;
import de.cuioss.test.valueobjects.junit5.contracts.ShouldImplementEqualsAndHashCode;
import lombok.Getter;

@EnableAutoWeld
@AddBeanClasses({ LazyLoadingTestSupportProducer.class, TestBaseLazyLoadingRequest.class })
class BaseLazyLoadingRequestTest implements ShouldBeNotNull<TestBaseLazyLoadingRequest>,
        ShouldImplementEqualsAndHashCode<TestBaseLazyLoadingRequest> {

    @Inject
    @Getter
    private TestBaseLazyLoadingRequest underTest;

    @Test
    void shouldDelegateHappyCase() {

        assertDoesNotThrow(() -> underTest.getRequestId());
    }
}
