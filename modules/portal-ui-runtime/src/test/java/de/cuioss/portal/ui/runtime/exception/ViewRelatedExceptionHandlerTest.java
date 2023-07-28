package de.cuioss.portal.ui.runtime.exception;

import static de.cuioss.portal.ui.test.configuration.PortalNavigationConfiguration.DESCRIPTOR_PREFERENCES;
import static de.cuioss.portal.ui.test.configuration.PortalNavigationConfiguration.VIEW_HOME_LOGICAL_VIEW_ID;
import static de.cuioss.portal.ui.test.configuration.PortalNavigationConfiguration.VIEW_LOGIN_LOGICAL_VIEW_ID;
import static de.cuioss.portal.ui.test.configuration.PortalNavigationConfiguration.VIEW_LOGOUT_LOGICAL_VIEW_ID;
import static de.cuioss.portal.ui.test.configuration.PortalNavigationConfiguration.VIEW_PREFERENCES_LOGICAL_VIEW_ID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;

import javax.faces.application.ViewExpiredException;
import javax.inject.Inject;

import org.jboss.weld.junit5.auto.AddBeanClasses;
import org.junit.jupiter.api.Test;

import de.cuioss.jsf.api.common.view.ViewDescriptor;
import de.cuioss.jsf.api.common.view.ViewDescriptorImpl;
import de.cuioss.portal.core.test.mocks.authentication.PortalTestUserProducer;
import de.cuioss.portal.ui.api.authentication.UserNotAuthenticatedException;
import de.cuioss.portal.ui.api.authentication.UserNotAuthorizedException;
import de.cuioss.portal.ui.api.history.PortalHistoryManager;
import de.cuioss.portal.ui.api.message.PortalMessageProducer;
import de.cuioss.portal.ui.api.ui.context.CurrentViewProducer;
import de.cuioss.portal.ui.api.ui.context.NavigationHandlerProducer;
import de.cuioss.portal.ui.api.view.PortalViewRestrictionManager;
import de.cuioss.portal.ui.runtime.application.view.ViewSuppressedException;
import de.cuioss.portal.ui.runtime.support.MockExceptionEvent;
import de.cuioss.portal.ui.test.junit5.EnablePortalUiEnvironment;
import de.cuioss.portal.ui.test.mocks.PortalHistoryManagerMock;
import de.cuioss.portal.ui.test.mocks.PortalMessageProducerMock;
import de.cuioss.portal.ui.test.mocks.PortalViewRestrictionManagerMock;
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
@AddBeanClasses({ CurrentViewProducer.class, NavigationHandlerProducer.class })
class ViewRelatedExceptionHandlerTest
        implements ShouldHandleObjectContracts<ViewRelatedExceptionHandler>, JsfEnvironmentConsumer {

    private static final String WARNING_KEY_PORTAL_002 = "Portal-103";

    @Setter
    @Getter
    private JsfEnvironmentHolder environmentHolder;

    @Inject
    private PortalTestUserProducer portalUserProducerMock;

    @Inject
    @Getter
    private ViewRelatedExceptionHandler underTest;

    @Inject
    @PortalHistoryManager
    private PortalHistoryManagerMock historyManagerMock;

    @Inject
    @PortalMessageProducer
    private PortalMessageProducerMock messageProducerMock;

    @Inject
    @PortalViewRestrictionManager
    private PortalViewRestrictionManagerMock viewRestrictionManagerMock;

    private static final ViewDescriptor DESCRIPTOR_SUPRRESSED_VIEW = ViewDescriptorImpl.builder()
            .withViewId("suppressedViewId").withLogicalViewId("suppressedViewId").build();

    @Test
    void shouldHandleViewSuppressedExceptionForLoggedInUser() {
        final var event = new MockExceptionEvent<>(
                new ViewSuppressedException(DESCRIPTOR_SUPRRESSED_VIEW));

        getRequestConfigDecorator().setViewId(VIEW_PREFERENCES_LOGICAL_VIEW_ID);
        underTest.handleViewSupressedException(event);
        assertTrue(event.isHandled());
        assertRedirect(VIEW_HOME_LOGICAL_VIEW_ID);
        messageProducerMock.assertSingleGlobalMessageWithKeyPresent(ViewRelatedExceptionHandler.VIEW_SUPPRESSED_KEY);
    }

    @Test
    void shouldHandleViewSuppressedExceptionForLoggedOutUser() {
        final var event = new MockExceptionEvent<>(
                new ViewSuppressedException(DESCRIPTOR_SUPRRESSED_VIEW));
        getRequestConfigDecorator().setViewId(VIEW_PREFERENCES_LOGICAL_VIEW_ID);
        portalUserProducerMock.authenticated(false);
        underTest.handleViewSupressedException(event);
        assertTrue(event.isHandled());
        assertRedirect(VIEW_LOGIN_LOGICAL_VIEW_ID);
        messageProducerMock.assertSingleGlobalMessageWithKeyPresent(ViewRelatedExceptionHandler.VIEW_SUPPRESSED_KEY);
    }

    @Test
    void shouldHandleViewExpiredExceptionForLoggedInUser() {
        final var event = new MockExceptionEvent<>(
                new ViewExpiredException(VIEW_PREFERENCES_LOGICAL_VIEW_ID));
        // Prepare history manager
        getRequestConfigDecorator().setViewId(VIEW_PREFERENCES_LOGICAL_VIEW_ID);
        // go to another page without notifying the historManger: Needed because
        // we want to check the history redirect.
        historyManagerMock.addCurrentUriToHistory(DESCRIPTOR_PREFERENCES);

        getRequestConfigDecorator().setViewId(VIEW_HOME_LOGICAL_VIEW_ID);
        underTest.handleViewExpiredException(event);
        assertTrue(event.isHandled());
        assertRedirect(VIEW_PREFERENCES_LOGICAL_VIEW_ID);
        messageProducerMock.assertSingleGlobalMessageWithKeyPresent(ViewRelatedExceptionHandler.VIEW_EXPIRED_KEY);
    }

    @Test
    void shouldRedirectToLoginOnNotAuthenticatedUser() {
        final var event = new MockExceptionEvent<>(new UserNotAuthenticatedException());
        // Prepare history manager
        getRequestConfigDecorator().setViewId(VIEW_PREFERENCES_LOGICAL_VIEW_ID);
        underTest.handleUserNotAuthenticatedException(event);
        assertRedirect(VIEW_LOGIN_LOGICAL_VIEW_ID);
        // Preferences should be put on top of the navigation for redirect over
        // login.
        assertEquals(VIEW_PREFERENCES_LOGICAL_VIEW_ID, historyManagerMock.getCurrentView().getViewId());
    }

    @Test
    void shouldHandleNotAuthorizedExceptionWithRedirectToHome() {
        final var event = new MockExceptionEvent<>(
                new UserNotAuthorizedException(ViewDescriptorImpl.builder().withLogicalViewId("/").build(),
                        Collections.emptyList(), Collections.emptyList()));
        getRequestConfigDecorator().setViewId(VIEW_PREFERENCES_LOGICAL_VIEW_ID);

        underTest.handleUserNotAuthorizedException(event);

        LogAsserts.assertLogMessagePresentContaining(TestLogLevel.WARN, WARNING_KEY_PORTAL_002);

        assertTrue(event.isHandled());
        assertRedirect(VIEW_HOME_LOGICAL_VIEW_ID);
        messageProducerMock
                .assertSingleGlobalMessageWithKeyPresent(ViewRelatedExceptionHandler.VIEW_INSUFFICIENT_PERMISSIONS_KEY);
    }

    @Test
    void shouldHandleNotAuthorizedExceptionWithRedirectToLogin() {

        viewRestrictionManagerMock.setAuthorized(false);

        final var event = new MockExceptionEvent<>(
                new UserNotAuthorizedException(ViewDescriptorImpl.builder().withLogicalViewId("/").build(),
                        Collections.emptyList(), Collections.emptyList()));
        getRequestConfigDecorator().setViewId(VIEW_PREFERENCES_LOGICAL_VIEW_ID);

        underTest.handleUserNotAuthorizedException(event);

        LogAsserts.assertLogMessagePresentContaining(TestLogLevel.WARN, WARNING_KEY_PORTAL_002);
        assertTrue(event.isHandled());

        assertRedirect(VIEW_LOGOUT_LOGICAL_VIEW_ID);
        messageProducerMock
                .assertSingleGlobalMessageWithKeyPresent(ViewRelatedExceptionHandler.VIEW_INSUFFICIENT_PERMISSIONS_KEY);
    }

    @Test
    void shouldHandleViewExpiredExceptionForLoggedOutUser() {
        final var event = new MockExceptionEvent<>(
                new ViewExpiredException(VIEW_PREFERENCES_LOGICAL_VIEW_ID));
        // Prepare history manager
        getRequestConfigDecorator().setViewId(VIEW_PREFERENCES_LOGICAL_VIEW_ID);
        portalUserProducerMock.authenticated(false);
        underTest.handleViewExpiredException(event);
        assertTrue(event.isHandled());
        assertRedirect(VIEW_LOGIN_LOGICAL_VIEW_ID);
    }
}
