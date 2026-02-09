/*
 * Copyright © 2025 CUI-OpenSource-Software (info@cuioss.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.cuioss.portal.ui.errorpages;

import de.cuioss.portal.core.storage.MapStorage;
import de.cuioss.portal.core.storage.PortalSessionStorage;
import de.cuioss.portal.core.test.mocks.core.PortalSessionStorageMock;
import de.cuioss.portal.ui.api.exception.DefaultErrorMessage;
import de.cuioss.portal.ui.test.junit5.EnablePortalUiEnvironment;
import de.cuioss.portal.ui.test.tests.AbstractPageBeanTest;
import jakarta.inject.Inject;
import lombok.Getter;
import org.jboss.weld.junit5.auto.AddBeanClasses;
import org.junit.jupiter.api.Test;

import java.io.Serializable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@EnablePortalUiEnvironment
@AddBeanClasses({PortalSessionStorageMock.class})
class Http401PageBeanTest extends AbstractPageBeanTest<Http401PageBean> {

    @Getter
    @Inject
    private Http401PageBean underTest;

    @Inject
    @PortalSessionStorage
    private MapStorage<Serializable, Serializable> mapStorage;

    @Test
    void shouldProvideCorrectCode() {
        assertEquals(401, underTest.getErrorCode());
    }

    @Test
    void shouldHandleNoMessage() {
        underTest.initView();
        assertFalse(underTest.isMessageAvailable());
    }

    @Test
    void shouldHandleMessage() {
        DefaultErrorMessage.addErrorMessageToSessionStorage(new DefaultErrorMessage("1", "1", "1", "1"), mapStorage);
        underTest.initView();
        assertTrue(underTest.isMessageAvailable());
    }
}
