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
package de.cuioss.portal.ui.runtime.application.lazyloading;

import de.cuioss.jsf.api.components.model.result_content.ResultErrorHandler;
import de.cuioss.jsf.test.MessageProducerMock;
import de.cuioss.portal.ui.test.junit5.EnablePortalUiEnvironment;
import de.cuioss.portal.ui.test.mocks.PortalStickyMessageProducerMock;
import de.cuioss.test.valueobjects.junit5.contracts.ShouldHandleObjectContracts;
import de.cuioss.uimodel.nameprovider.DisplayName;
import de.cuioss.uimodel.result.ResultDetail;
import de.cuioss.uimodel.result.ResultObject;
import de.cuioss.uimodel.result.ResultState;
import jakarta.inject.Inject;
import lombok.Getter;
import org.jboss.weld.junit5.auto.AddBeanClasses;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@EnablePortalUiEnvironment
@AddBeanClasses({MessageProducerMock.class, PortalStickyMessageProducerMock.class})
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
