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
package de.cuioss.portal.ui.api.lazyloading;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import jakarta.inject.Inject;

import org.jboss.weld.junit5.auto.AddBeanClasses;
import org.jboss.weld.junit5.auto.EnableAutoWeld;
import org.junit.jupiter.api.Assertions;
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

        Assertions.assertDoesNotThrow(() -> underTest.getRequestId());
    }
}
