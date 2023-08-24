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
package de.cuioss.portal.ui.runtime.application.exception;

import static de.cuioss.portal.ui.test.configuration.PortalNavigationConfiguration.VIEW_HOME_LOGICAL_VIEW_ID;
import static de.cuioss.portal.ui.test.configuration.PortalNavigationConfiguration.VIEW_PREFERENCES_LOGICAL_VIEW_ID;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.inject.Inject;

import org.jboss.weld.junit5.auto.AddBeanClasses;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.cuioss.jsf.api.common.view.ViewDescriptor;
import de.cuioss.jsf.api.common.view.ViewDescriptorImpl;
import de.cuioss.portal.core.test.mocks.authentication.PortalTestUserProducer;
import de.cuioss.portal.ui.api.history.PortalHistoryManager;
import de.cuioss.portal.ui.api.message.PortalMessageProducer;
import de.cuioss.portal.ui.api.ui.context.CurrentViewProducer;
import de.cuioss.portal.ui.api.ui.context.NavigationHandlerProducer;
import de.cuioss.portal.ui.api.view.PortalViewRestrictionManager;
import de.cuioss.portal.ui.runtime.application.view.ViewSuppressedException;
import de.cuioss.portal.ui.runtime.exception.PortalUiExceptionHandler;
import de.cuioss.portal.ui.runtime.exception.ViewRelatedExceptionHandler;
import de.cuioss.portal.ui.test.junit5.EnablePortalUiEnvironment;
import de.cuioss.portal.ui.test.mocks.PortalHistoryManagerMock;
import de.cuioss.portal.ui.test.mocks.PortalMessageProducerMock;
import de.cuioss.portal.ui.test.mocks.PortalViewRestrictionManagerMock;
import de.cuioss.test.jsf.util.JsfEnvironmentConsumer;
import de.cuioss.test.jsf.util.JsfEnvironmentHolder;
import lombok.Getter;
import lombok.Setter;

@EnablePortalUiEnvironment
@AddBeanClasses({ CurrentViewProducer.class, NavigationHandlerProducer.class, PortalUiExceptionHandler.class,
        ViewRelatedExceptionHandler.class, PortalTestUserProducer.class })
class JSFPortalExceptionHandlerBridgeTest implements JsfEnvironmentConsumer {

    static final ViewDescriptor DESCRIPTOR_SUPRRESSED_VIEW = ViewDescriptorImpl.builder().withViewId("suppressedViewId")
            .withLogicalViewId("suppressedViewId").build();

    @Setter
    @Getter
    private JsfEnvironmentHolder environmentHolder;

    @Inject
    @PortalMessageProducer
    private PortalMessageProducerMock messageProducerMock;

    @Inject
    @PortalHistoryManager
    private PortalHistoryManagerMock historyManagerMock;

    @Inject
    @PortalViewRestrictionManager
    private PortalViewRestrictionManagerMock viewRestrictionManagerMock;

    private MockExceptionHandler mockExceptionHandler;

    private JSFPortalExceptionHandlerBridge underTest;

    @BeforeEach
    void initMockExceptionHandler() {
        mockExceptionHandler = new MockExceptionHandler(null);
        underTest = new JSFPortalExceptionHandlerBridge(mockExceptionHandler);
    }

    @Test
    void shouldHandleHappyCase() {
        final var exception = new ViewSuppressedException(DESCRIPTOR_SUPRRESSED_VIEW);
        getRequestConfigDecorator().setViewId(VIEW_PREFERENCES_LOGICAL_VIEW_ID);
        mockExceptionHandler.addUnhandledException(exception);
        underTest.handle();
        assertRedirect(VIEW_HOME_LOGICAL_VIEW_ID);
        messageProducerMock.assertSingleGlobalMessageWithKeyPresent(ViewRelatedExceptionHandler.VIEW_SUPPRESSED_KEY);
        mockExceptionHandler.assertHandleCalled();
        assertTrue(mockExceptionHandler.getUnhandledExceptionQueuedEvents().isEmpty(),
                "Unhandled Excetpions should be empty");
    }
}
