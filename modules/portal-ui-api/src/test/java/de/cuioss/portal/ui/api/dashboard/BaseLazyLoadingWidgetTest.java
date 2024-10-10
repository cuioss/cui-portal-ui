/*
 * Copyright 2023 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * https://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.cuioss.portal.ui.api.dashboard;

import de.cuioss.portal.ui.api.lazyloading.LazyLoadingErrorHandler;
import de.cuioss.test.valueobjects.junit5.contracts.ShouldBeNotNull;
import de.cuioss.test.valueobjects.junit5.contracts.ShouldImplementEqualsAndHashCode;
import jakarta.inject.Inject;
import lombok.Getter;
import org.jboss.weld.junit5.auto.AddBeanClasses;
import org.jboss.weld.junit5.auto.EnableAutoWeld;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

@EnableAutoWeld
@AddBeanClasses({LazyLoadingTestSupportProducer.class})
class BaseLazyLoadingWidgetTest
        implements ShouldBeNotNull<TestLazyLoadingWidget>, ShouldImplementEqualsAndHashCode<TestLazyLoadingWidget> {

    @Inject
    @Getter
    private TestLazyLoadingWidget underTest;

    @Test
    void shouldDelegateHappyCase() {
        var underTest = getUnderTest();

        assertDoesNotThrow(underTest::startInitialize);
        assertDoesNotThrow(() -> underTest.processAction(null));
        assertDoesNotThrow(underTest::getRequestId);
        assertDoesNotThrow(underTest::getNotificationBoxState);
        assertDoesNotThrow(underTest::getNotificationBoxValue);
        assertDoesNotThrow(underTest::isInitialized);
        assertDoesNotThrow(underTest::isRenderContent);
    }

    @Test
    void shouldProvideDefaultErrorHandler() {
        assertInstanceOf(LazyLoadingErrorHandler.class, underTest.getErrorHandler());
    }

}
