package de.cuioss.portal.ui.runtime.exception;

import static de.cuioss.portal.ui.test.configuration.PortalNavigationConfiguration.VIEW_ERROR_LOGICAL_VIEW_ID;
import static de.cuioss.portal.ui.test.configuration.PortalNavigationConfiguration.VIEW_LOGIN_LOGICAL_VIEW_ID;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.inject.Inject;

import org.jboss.weld.junit5.auto.AddBeanClasses;
import org.junit.jupiter.api.Test;

import de.cuioss.portal.configuration.PortalConfigurationSource;
import de.cuioss.portal.core.storage.PortalSessionStorage;
import de.cuioss.portal.core.test.mocks.configuration.PortalTestConfiguration;
import de.cuioss.portal.core.test.mocks.core.PortalSessionStorageMock;
import de.cuioss.portal.ui.api.exception.DefaultErrorMessage;
import de.cuioss.portal.ui.api.ui.context.CurrentViewProducer;
import de.cuioss.portal.ui.api.ui.context.NavigationHandlerProducer;
import de.cuioss.portal.ui.runtime.support.MockExceptionEvent;
import de.cuioss.portal.ui.test.junit5.EnablePortalUiEnvironment;
import de.cuioss.test.jsf.util.JsfEnvironmentConsumer;
import de.cuioss.test.jsf.util.JsfEnvironmentHolder;
import de.cuioss.test.juli.LogAsserts;
import de.cuioss.test.juli.TestLogLevel;
import de.cuioss.test.juli.junit5.EnableTestLogger;
import de.cuioss.test.valueobjects.junit5.contracts.ShouldHandleObjectContracts;
import lombok.Getter;
import lombok.Setter;

@EnablePortalUiEnvironment
@EnableTestLogger
@AddBeanClasses({ CurrentViewProducer.class, PortalSessionStorageMock.class, NavigationHandlerProducer.class })
class FallBackExceptionHandlerTest
        implements ShouldHandleObjectContracts<FallBackExceptionHandler>, JsfEnvironmentConsumer {

    @Setter
    @Getter
    private JsfEnvironmentHolder environmentHolder;

    @Inject
    @Getter
    private FallBackExceptionHandler underTest;

    @Inject
    @PortalSessionStorage
    private PortalSessionStorageMock sessionStorage;

    @Inject
    @PortalConfigurationSource
    private PortalTestConfiguration configuration;

    @Test
    void shouldHandleThrowable() {
        getRequestConfigDecorator().setViewId(VIEW_LOGIN_LOGICAL_VIEW_ID);

        var exceptionEvent = new MockExceptionEvent<>(new Throwable());
        underTest.handleFallBack(exceptionEvent);
        assertTrue(exceptionEvent.isHandled());

        assertRedirect(VIEW_ERROR_LOGICAL_VIEW_ID);
        var message = (DefaultErrorMessage) sessionStorage.get(DefaultErrorMessage.LOOKUP_KEY);
        assertNotNull(message);
    }

    @Test
    void shouldWriteTicketId() {
        getRequestConfigDecorator().setViewId(VIEW_LOGIN_LOGICAL_VIEW_ID);

        var exceptionEvent = new MockExceptionEvent<>(new Throwable());
        underTest.handleFallBack(exceptionEvent);
        assertTrue(exceptionEvent.isHandled());

        var message = (DefaultErrorMessage) sessionStorage.get(DefaultErrorMessage.LOOKUP_KEY);
        assertNotNull(message);
        assertNotNull(message.getErrorTicket());

        LogAsserts.assertSingleLogMessagePresentContaining(TestLogLevel.ERROR, message.getErrorTicket());
    }

    @Test
    void shouldHandleInvalidSessionNotRecover() {
        getRequestConfigDecorator().setViewId(VIEW_LOGIN_LOGICAL_VIEW_ID);
        sessionStorage.setThrowIllegalStateOnAccess(true);

        var exceptionEvent = new MockExceptionEvent<>(new Throwable());
        underTest.handleFallBack(exceptionEvent);
        assertTrue(exceptionEvent.isHandled());

        sessionStorage.setThrowIllegalStateOnAccess(false);
        assertNull(sessionStorage.get(DefaultErrorMessage.LOOKUP_KEY));
    }

    @Test
    void shouldHandleInvalidSession() {
        getRequestConfigDecorator().setViewId(VIEW_LOGIN_LOGICAL_VIEW_ID);
        sessionStorage.setThrowIllegalStateOnAccessOnce(true);

        var exceptionEvent = new MockExceptionEvent<>(new Throwable());
        underTest.handleFallBack(exceptionEvent);
        assertTrue(exceptionEvent.isHandled());

        assertNotNull(sessionStorage.get(DefaultErrorMessage.LOOKUP_KEY));
    }

    @Test
    void shoulddoNothingIfInDevelopmentStage() {
        configuration.development();
        getRequestConfigDecorator().setViewId(VIEW_LOGIN_LOGICAL_VIEW_ID);
        var exceptionEvent = new MockExceptionEvent<>(new Throwable());
        underTest.handleFallBack(exceptionEvent);
        assertFalse(exceptionEvent.isHandled());
    }
}
