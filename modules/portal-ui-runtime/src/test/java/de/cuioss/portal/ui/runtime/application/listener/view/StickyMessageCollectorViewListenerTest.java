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
package de.cuioss.portal.ui.runtime.application.listener.view;

import de.cuioss.portal.ui.api.listener.view.PhaseExecution;
import de.cuioss.portal.ui.api.listener.view.PortalRestoreViewListener;
import de.cuioss.portal.ui.runtime.application.listener.view.testhelper.StickyMessageProviderMock;
import de.cuioss.portal.ui.test.junit5.EnablePortalUiEnvironment;
import de.cuioss.portal.ui.test.mocks.PortalStickyMessageProducerMock;
import de.cuioss.test.valueobjects.junit5.contracts.ShouldHandleObjectContracts;
import jakarta.inject.Inject;
import lombok.Getter;
import org.jboss.weld.junit5.auto.AddBeanClasses;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;

@EnablePortalUiEnvironment
@AddBeanClasses({ StickyMessageProviderMock.class })
class StickyMessageCollectorViewListenerTest
        implements ShouldHandleObjectContracts<StickyMessageCollectorViewListener> {

    @Inject
    @PortalRestoreViewListener(PhaseExecution.BEFORE_PHASE)
    @Getter
    private StickyMessageCollectorViewListener underTest;

    @Inject
    private PortalStickyMessageProducerMock stickyMessageProducer;

    @Test
    void shouldCollectMessages() {

        underTest.handleView(null);

        assertFalse(stickyMessageProducer.getMessages().isEmpty(), "Expected one message available");

    }

}
