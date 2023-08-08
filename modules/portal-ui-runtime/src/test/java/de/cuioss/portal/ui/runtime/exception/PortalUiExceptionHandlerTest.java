package de.cuioss.portal.ui.runtime.exception;

import static de.cuioss.portal.ui.test.configuration.PortalNavigationConfiguration.VIEW_ERROR_LOGICAL_VIEW_ID;
import static de.cuioss.portal.ui.test.configuration.PortalNavigationConfiguration.VIEW_LOGIN_LOGICAL_VIEW_ID;
import static de.cuioss.portal.ui.test.configuration.PortalNavigationConfiguration.VIEW_PREFERENCES_LOGICAL_VIEW_ID;
import static de.cuioss.test.generator.Generators.throwables;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.jboss.weld.junit5.auto.AddBeanClasses;
import org.junit.jupiter.api.Test;

import de.cuioss.portal.configuration.PortalConfigurationSource;
import de.cuioss.portal.core.storage.PortalSessionStorage;
import de.cuioss.portal.core.test.mocks.authentication.PortalTestUserProducer;
import de.cuioss.portal.core.test.mocks.configuration.PortalTestConfiguration;
import de.cuioss.portal.core.test.mocks.core.PortalSessionStorageMock;
import de.cuioss.portal.ui.api.exception.DefaultErrorMessage;
import de.cuioss.portal.ui.api.exception.ExceptionAsEvent;
import de.cuioss.portal.ui.api.exception.HandleOutcome;
import de.cuioss.portal.ui.api.history.PortalHistoryManager;
import de.cuioss.portal.ui.api.message.PortalMessageProducer;
import de.cuioss.portal.ui.api.ui.context.CurrentViewProducer;
import de.cuioss.portal.ui.api.ui.context.NavigationHandlerProducer;
import de.cuioss.portal.ui.api.view.PortalViewRestrictionManager;
import de.cuioss.portal.ui.runtime.application.view.ViewSuppressedException;
import de.cuioss.portal.ui.test.junit5.EnablePortalUiEnvironment;
import de.cuioss.portal.ui.test.mocks.PortalHistoryManagerMock;
import de.cuioss.portal.ui.test.mocks.PortalMessageProducerMock;
import de.cuioss.portal.ui.test.mocks.PortalViewRestrictionManagerMock;
import de.cuioss.test.jsf.util.JsfEnvironmentConsumer;
import de.cuioss.test.jsf.util.JsfEnvironmentHolder;
import de.cuioss.test.juli.junit5.EnableTestLogger;
import de.cuioss.test.valueobjects.junit5.contracts.ShouldBeNotNull;
import lombok.Getter;
import lombok.Setter;

@EnablePortalUiEnvironment
@EnableTestLogger
@AddBeanClasses({ CurrentViewProducer.class, NavigationHandlerProducer.class, ViewRelatedExceptionHandler.class,
        PortalTestUserProducer.class })
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
    @PortalConfigurationSource
    private PortalTestConfiguration configuration;

    @Inject
    @PortalHistoryManager
    private PortalHistoryManagerMock historyManagerMock;

    @Inject
    @PortalMessageProducer
    private PortalMessageProducerMock messageProducerMock;

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