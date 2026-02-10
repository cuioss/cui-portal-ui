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
package de.cuioss.portal.ui.runtime.exception;

import de.cuioss.portal.core.storage.PortalSessionStorage;
import de.cuioss.portal.core.test.mocks.configuration.PortalTestConfiguration;
import de.cuioss.portal.core.test.mocks.core.PortalSessionStorageMock;
import de.cuioss.portal.ui.api.context.CurrentViewProducer;
import de.cuioss.portal.ui.api.context.NavigationHandlerProducer;
import de.cuioss.portal.ui.api.exception.DefaultErrorMessage;
import de.cuioss.portal.ui.api.exception.ExceptionAsEvent;
import de.cuioss.portal.ui.api.exception.HandleOutcome;
import de.cuioss.portal.ui.test.junit5.EnablePortalUiEnvironment;
import de.cuioss.test.juli.LogAsserts;
import de.cuioss.test.juli.TestLogLevel;
import de.cuioss.test.juli.junit5.EnableTestLogger;
import de.cuioss.test.valueobjects.junit5.contracts.ShouldHandleObjectContracts;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import lombok.Getter;
import org.apache.myfaces.test.mock.MockHttpServletResponse;
import org.jboss.weld.junit5.auto.AddBeanClasses;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static de.cuioss.portal.ui.test.configuration.PortalNavigationConfiguration.VIEW_ERROR_LOGICAL_VIEW_ID;
import static de.cuioss.portal.ui.test.configuration.PortalNavigationConfiguration.VIEW_LOGIN_LOGICAL_VIEW_ID;
import static de.cuioss.test.generator.Generators.throwables;
import static org.junit.jupiter.api.Assertions.*;

@EnablePortalUiEnvironment
@EnableTestLogger
@AddBeanClasses({CurrentViewProducer.class, PortalSessionStorageMock.class, NavigationHandlerProducer.class})
class FallBackExceptionHandlerTest
        implements ShouldHandleObjectContracts<FallBackExceptionHandler> {

    @Inject
    @Getter
    private FallBackExceptionHandler underTest;

    @Inject
    @PortalSessionStorage
    private PortalSessionStorageMock sessionStorage;

    @Inject
    private PortalTestConfiguration configuration;

    private FacesContext facesContext;

    @BeforeEach
    void setUp() {
        this.facesContext = FacesContext.getCurrentInstance();
    }

    @Test
    void shouldHandleThrowable() {
        facesContext.getViewRoot().setViewId(VIEW_LOGIN_LOGICAL_VIEW_ID);

        var exceptionEvent = new ExceptionAsEvent(throwables().next());
        underTest.handleFallBack(exceptionEvent);
        assertEquals(HandleOutcome.REDIRECT, exceptionEvent.getOutcome());

        assertRedirect(facesContext, VIEW_ERROR_LOGICAL_VIEW_ID);
        var message = (DefaultErrorMessage) sessionStorage.get(DefaultErrorMessage.LOOKUP_KEY);
        assertNotNull(message);
    }

    @Test
    void shouldShortcutIfHandled() {

        facesContext.getViewRoot().setViewId(VIEW_LOGIN_LOGICAL_VIEW_ID);

        var exceptionEvent = new ExceptionAsEvent(throwables().next());
        exceptionEvent.handled(HandleOutcome.USER_MESSAGE);
        underTest.handleFallBack(exceptionEvent);
        assertEquals(HandleOutcome.USER_MESSAGE, exceptionEvent.getOutcome());
    }

    @Test
    void shouldWriteTicketId() {

        facesContext.getViewRoot().setViewId(VIEW_LOGIN_LOGICAL_VIEW_ID);

        var exceptionEvent = new ExceptionAsEvent(throwables().next());
        underTest.handleFallBack(exceptionEvent);
        assertEquals(HandleOutcome.REDIRECT, exceptionEvent.getOutcome());

        var message = (DefaultErrorMessage) sessionStorage.get(DefaultErrorMessage.LOOKUP_KEY);
        assertNotNull(message);
        assertNotNull(message.getErrorTicket());

        LogAsserts.assertSingleLogMessagePresentContaining(TestLogLevel.ERROR, message.getErrorTicket());
    }

    @Test
    void shouldHandleInvalidSessionNotRecover() {

        facesContext.getViewRoot().setViewId(VIEW_LOGIN_LOGICAL_VIEW_ID);
        sessionStorage.setThrowIllegalStateOnAccess(true);

        var exceptionEvent = new ExceptionAsEvent(throwables().next());
        underTest.handleFallBack(exceptionEvent);
        assertEquals(HandleOutcome.REDIRECT, exceptionEvent.getOutcome());

        sessionStorage.setThrowIllegalStateOnAccess(false);
        assertNull(sessionStorage.get(DefaultErrorMessage.LOOKUP_KEY));
    }

    @Test
    void shouldHandleInvalidSession() {

        facesContext.getViewRoot().setViewId(VIEW_LOGIN_LOGICAL_VIEW_ID);
        sessionStorage.setThrowIllegalStateOnAccessOnce(true);

        var exceptionEvent = new ExceptionAsEvent(throwables().next());
        underTest.handleFallBack(exceptionEvent);
        assertEquals(HandleOutcome.REDIRECT, exceptionEvent.getOutcome());

        assertNotNull(sessionStorage.get(DefaultErrorMessage.LOOKUP_KEY));
    }

    @Test
    void shouldThrowIfInDevelopmentStage() {

        configuration.development();
        facesContext.getViewRoot().setViewId(VIEW_LOGIN_LOGICAL_VIEW_ID);
        var throwable = throwables().next();
        var exceptionEvent = new ExceptionAsEvent(throwable);
        assertThrows(IllegalStateException.class, () -> underTest.handleFallBack(exceptionEvent));
        assertEquals(HandleOutcome.RE_THROWN, exceptionEvent.getOutcome());
    }

    @Test
    void shouldDetectCallFromErrorPage() {

        facesContext.getViewRoot().setViewId(VIEW_ERROR_LOGICAL_VIEW_ID);
        var throwable = throwables().next();
        var exceptionEvent = new ExceptionAsEvent(throwable);
        underTest.handleFallBack(exceptionEvent);
        assertEquals(HandleOutcome.LOGGED, exceptionEvent.getOutcome());
        LogAsserts.assertSingleLogMessagePresentContaining(TestLogLevel.ERROR, "Portal-130:");
    }

    @Test
    void shouldHandleResponseAlreadyCommitted() {

        facesContext.getViewRoot().setViewId(VIEW_LOGIN_LOGICAL_VIEW_ID);
        facesContext.release();
        var throwable = throwables().next();
        var exceptionEvent = new ExceptionAsEvent(throwable);
        underTest.handleFallBack(exceptionEvent);
        assertEquals(HandleOutcome.LOGGED, exceptionEvent.getOutcome());
        LogAsserts.assertSingleLogMessagePresentContaining(TestLogLevel.ERROR, "Portal-112:");
    }

    private static void assertRedirect(FacesContext facesContext, String url) {
        var response = (MockHttpServletResponse) facesContext.getExternalContext().getResponse();
        assertEquals(url, response.getHeader("Location"), "Redirect URL mismatch");
    }
}
