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
package de.icw.cui.portal.ui.errorpages;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import jakarta.inject.Inject;

import org.junit.jupiter.api.Test;

import de.cuioss.portal.core.storage.PortalSessionStorage;
import de.cuioss.portal.core.test.mocks.core.PortalSessionStorageMock;
import de.cuioss.portal.ui.api.exception.DefaultErrorMessage;
import de.cuioss.portal.ui.api.ui.pages.PortalCorePagesError;
import de.cuioss.portal.ui.test.junit5.EnablePortalUiEnvironment;
import de.cuioss.portal.ui.test.tests.AbstractPageBeanTest;
import lombok.Getter;

@EnablePortalUiEnvironment
class ErrorPageBeanTest extends AbstractPageBeanTest<ErrorPageBean> {

    @Inject
    @PortalSessionStorage
    private PortalSessionStorageMock mapStorage;

    @Inject
    @PortalCorePagesError
    @Getter
    private ErrorPageBean underTest;

    @Test
    void shouldIgnoreNotExisitingMessage() {
        assertFalse(underTest.isMessageAvailable());
    }

    @Test
    void shouldResolveAndRemoveErrorMessage() {
        var errorMessage = new DefaultErrorMessage("errorCode", "errorTicket", "errorMessage", "pageId");
        DefaultErrorMessage.addErrorMessageToSessionStorage(errorMessage, mapStorage);

        assertTrue(mapStorage.containsKey(DefaultErrorMessage.LOOKUP_KEY));
        assertTrue(underTest.isMessageAvailable());
        assertEquals(errorMessage, underTest.getMessage());
        assertFalse(mapStorage.containsKey(DefaultErrorMessage.LOOKUP_KEY));
    }

}
