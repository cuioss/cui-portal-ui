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
package de.cuioss.portal.ui.runtime.exception;

import de.cuioss.jsf.test.MessageProducerMock;
import de.cuioss.portal.core.storage.PortalSessionStorage;
import de.cuioss.portal.core.test.mocks.authentication.PortalTestUserProducer;
import de.cuioss.portal.core.test.mocks.configuration.PortalTestConfiguration;
import de.cuioss.portal.core.test.mocks.core.PortalSessionStorageMock;
import de.cuioss.portal.ui.api.context.CurrentViewProducer;
import de.cuioss.portal.ui.api.context.NavigationHandlerProducer;
import de.cuioss.portal.ui.api.exception.DefaultErrorMessage;
import de.cuioss.portal.ui.api.exception.ExceptionAsEvent;
import de.cuioss.portal.ui.api.exception.HandleOutcome;
import de.cuioss.portal.ui.api.view.PortalViewRestrictionManager;
import de.cuioss.portal.ui.runtime.application.view.ViewSuppressedException;
import de.cuioss.portal.ui.test.junit5.EnablePortalUiEnvironment;
import de.cuioss.portal.ui.test.mocks.PortalHistoryManagerMock;
import de.cuioss.portal.ui.test.mocks.PortalViewRestrictionManagerMock;
import de.cuioss.test.jsf.util.JsfEnvironmentConsumer;
import de.cuioss.test.jsf.util.JsfEnvironmentHolder;
import de.cuioss.test.juli.junit5.EnableTestLogger;
import de.cuioss.test.valueobjects.junit5.contracts.ShouldBeNotNull;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;
import lombok.Getter;
import lombok.Setter;
import org.jboss.weld.junit5.auto.AddBeanClasses;
import org.junit.jupiter.api.Test;

import static de.cuioss.portal.ui.test.configuration.PortalNavigationConfiguration.VIEW_ERROR_LOGICAL_VIEW_ID;
import static de.cuioss.portal.ui.test.configuration.PortalNavigationConfiguration.VIEW_LOGIN_LOGICAL_VIEW_ID;
import static de.cuioss.portal.ui.test.configuration.PortalNavigationConfiguration.VIEW_PREFERENCES_LOGICAL_VIEW_ID;
import static de.cuioss.test.generator.Generators.throwables;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@EnablePortalUiEnvironment
@EnableTestLogger
@AddBeanClasses({CurrentViewProducer.class, NavigationHandlerProducer.class, ViewRelatedExceptionHandler.class,
        PortalTestUserProducer.class, PortalHistoryManagerMock.class})
class PortalUiExceptionHandlerTest implements ShouldBeNotNull<PortalUiExceptionHandler>, JsfEnvironmentConsumer {

    @Setter
    @Getter
    private JsfEnvironmentHolder environmentHolder;

    @Inject
    @Getter
    private PortalUiExceptionHandler underTest;

    @Inject
    @PortalSessionStorage
    private PortalSessionStorageMock sessionStorage;

    @Inject
    private PortalTestConfiguration configuration;

    @Inject
    private MessageProducerMock messageProducerMock;

    @Inject
    @PortalViewRestrictionManager
    private PortalViewRestrictionManagerMock viewRestrictionManagerMock;

    @Inject
    private PortalTestUserProducer portalUserProducerMock;

    @Inject
    private Event<ExceptionAsEvent> eventBridge;

    @Test
    void shouldCallViewRelatedExceptionHandler() {
        final var event = new ExceptionAsEvent(
                new ViewSuppressedException(ViewRelatedExceptionHandlerTest.DESCRIPTOR_SUPRRESSED_VIEW));
        getRequestConfigDecorator().setViewId(VIEW_PREFERENCES_LOGICAL_VIEW_ID);
        portalUserProducerMock.authenticated(false);
        underTest.handle(event);
        assertTrue(event.isHandled());
        assertRedirect(VIEW_LOGIN_LOGICAL_VIEW_ID);
        messageProducerMock.assertSingleGlobalMessageWithKeyPresent(ViewRelatedExceptionHandler.VIEW_SUPPRESSED_KEY);
    }

    @Test
    void shouldCallViewRelatedExceptionHandlerAsEvent() {
        final var event = new ExceptionAsEvent(
                new ViewSuppressedException(ViewRelatedExceptionHandlerTest.DESCRIPTOR_SUPRRESSED_VIEW));
        getRequestConfigDecorator().setViewId(VIEW_PREFERENCES_LOGICAL_VIEW_ID);
        portalUserProducerMock.authenticated(false);
        eventBridge.fire(event);
        assertTrue(event.isHandled());
        assertRedirect(VIEW_LOGIN_LOGICAL_VIEW_ID);
        messageProducerMock.assertSingleGlobalMessageWithKeyPresent(ViewRelatedExceptionHandler.VIEW_SUPPRESSED_KEY);
    }

    @Test
    void shouldHandleThrowable() {
        getRequestConfigDecorator().setViewId(VIEW_LOGIN_LOGICAL_VIEW_ID);

        var exceptionEvent = new ExceptionAsEvent(throwables().next());
        underTest.handle(exceptionEvent);
        assertEquals(HandleOutcome.REDIRECT, exceptionEvent.getOutcome());

        assertRedirect(VIEW_ERROR_LOGICAL_VIEW_ID);
        var message = (DefaultErrorMessage) sessionStorage.get(DefaultErrorMessage.LOOKUP_KEY);
        assertNotNull(message);
    }

    @Test
    void shouldShortcutIfHandled() {
        getRequestConfigDecorator().setViewId(VIEW_LOGIN_LOGICAL_VIEW_ID);

        var exceptionEvent = new ExceptionAsEvent(throwables().next());
        exceptionEvent.handled(HandleOutcome.USER_MESSAGE);
        underTest.handle(exceptionEvent);
        assertEquals(HandleOutcome.USER_MESSAGE, exceptionEvent.getOutcome());
    }

}
