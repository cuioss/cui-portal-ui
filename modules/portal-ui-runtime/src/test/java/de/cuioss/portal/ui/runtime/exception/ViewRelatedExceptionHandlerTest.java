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

import de.cuioss.jsf.api.common.view.ViewDescriptor;
import de.cuioss.jsf.api.common.view.ViewDescriptorImpl;
import de.cuioss.jsf.test.MessageProducerMock;
import de.cuioss.portal.core.test.mocks.authentication.PortalTestUserProducer;
import de.cuioss.portal.ui.api.authentication.UserNotAuthenticatedException;
import de.cuioss.portal.ui.api.authentication.UserNotAuthorizedException;
import de.cuioss.portal.ui.api.context.CurrentViewProducer;
import de.cuioss.portal.ui.api.context.NavigationHandlerProducer;
import de.cuioss.portal.ui.api.exception.ExceptionAsEvent;
import de.cuioss.portal.ui.api.history.HistoryManager;
import de.cuioss.portal.ui.api.view.PortalViewRestrictionManager;
import de.cuioss.portal.ui.runtime.application.history.DefaultHistoryConfiguration;
import de.cuioss.portal.ui.runtime.application.history.HistoryManagerBean;
import de.cuioss.portal.ui.runtime.application.view.ViewSuppressedException;
import de.cuioss.portal.ui.runtime.application.view.matcher.ViewMatcherProducer;
import de.cuioss.portal.ui.test.junit5.EnablePortalUiEnvironment;
import de.cuioss.portal.ui.test.mocks.PortalViewRestrictionManagerMock;
import de.cuioss.test.juli.LogAsserts;
import de.cuioss.test.juli.TestLogLevel;
import de.cuioss.test.juli.junit5.EnableTestLogger;
import de.cuioss.test.valueobjects.junit5.contracts.ShouldHandleObjectContracts;
import jakarta.faces.application.ViewExpiredException;
import jakarta.faces.context.FacesContext;
import jakarta.faces.lifecycle.ClientWindowScoped;
import jakarta.inject.Inject;
import lombok.Getter;
import org.apache.myfaces.test.mock.MockHttpServletResponse;
import org.jboss.weld.junit5.auto.ActivateScopes;
import org.jboss.weld.junit5.auto.AddBeanClasses;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static de.cuioss.portal.ui.test.configuration.PortalNavigationConfiguration.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@EnablePortalUiEnvironment
@EnableTestLogger
@AddBeanClasses({CurrentViewProducer.class, NavigationHandlerProducer.class, HistoryManagerBean.class,
        DefaultHistoryConfiguration.class, ViewMatcherProducer.class})
@ActivateScopes(ClientWindowScoped.class)
class ViewRelatedExceptionHandlerTest
        implements ShouldHandleObjectContracts<ViewRelatedExceptionHandler> {

    static final ViewDescriptor DESCRIPTOR_SUPRRESSED_VIEW = ViewDescriptorImpl.builder().withViewId("suppressedViewId")
            .withLogicalViewId("suppressedViewId").build();
    private static final String WARNING_KEY_PORTAL_002 = "Portal-103";
    @Inject
    private PortalTestUserProducer portalUserProducerMock;
    @Inject
    @Getter
    private ViewRelatedExceptionHandler underTest;
    @Inject
    private HistoryManager historyManagerMock;
    @Inject
    private MessageProducerMock messageProducerMock;
    @Inject
    @PortalViewRestrictionManager
    private PortalViewRestrictionManagerMock viewRestrictionManagerMock;

    private FacesContext facesContext;

    @BeforeEach
    void setUp() {
        this.facesContext = FacesContext.getCurrentInstance();
    }

    @Test
    void shouldHandleViewSuppressedExceptionForLoggedInUser() {
        final var event = new ExceptionAsEvent(new ViewSuppressedException(DESCRIPTOR_SUPRRESSED_VIEW));

        facesContext.getViewRoot().setViewId(VIEW_PREFERENCES_LOGICAL_VIEW_ID);
        underTest.handle(event);
        assertTrue(event.isHandled());
        assertRedirect(facesContext, VIEW_HOME_LOGICAL_VIEW_ID);
        messageProducerMock.assertSingleGlobalMessageWithKeyPresent(ViewRelatedExceptionHandler.VIEW_SUPPRESSED_KEY);
    }

    @Test
    void shouldHandleViewSuppressedExceptionForLoggedOutUser() {

        final var event = new ExceptionAsEvent(new ViewSuppressedException(DESCRIPTOR_SUPRRESSED_VIEW));
        facesContext.getViewRoot().setViewId(VIEW_PREFERENCES_LOGICAL_VIEW_ID);
        portalUserProducerMock.authenticated(false);
        underTest.handle(event);
        assertTrue(event.isHandled());
        assertRedirect(facesContext, VIEW_LOGIN_LOGICAL_VIEW_ID);
        messageProducerMock.assertSingleGlobalMessageWithKeyPresent(ViewRelatedExceptionHandler.VIEW_SUPPRESSED_KEY);
    }

    @Test
    void shouldHandleViewExpiredExceptionForLoggedInUser() {

        final var event = new ExceptionAsEvent(new ViewExpiredException(VIEW_PREFERENCES_LOGICAL_VIEW_ID));
        // Prepare history manager
        facesContext.getViewRoot().setViewId(VIEW_PREFERENCES_LOGICAL_VIEW_ID);
        // go to another page without notifying the historyManger: Needed because
        // we want to check the history redirect.
        historyManagerMock.addCurrentUriToHistory(DESCRIPTOR_PREFERENCES);

        facesContext.getViewRoot().setViewId(VIEW_HOME_LOGICAL_VIEW_ID);
        underTest.handle(event);
        assertTrue(event.isHandled());
        assertRedirect(facesContext, VIEW_PREFERENCES_LOGICAL_VIEW_ID);
        messageProducerMock.assertSingleGlobalMessageWithKeyPresent(ViewRelatedExceptionHandler.VIEW_EXPIRED_KEY);
    }

    @Test
    void shouldRedirectToLoginOnNotAuthenticatedUser() {

        final var event = new ExceptionAsEvent(new UserNotAuthenticatedException());
        // Prepare history manager
        facesContext.getViewRoot().setViewId(VIEW_PREFERENCES_LOGICAL_VIEW_ID);
        underTest.handle(event);
        assertRedirect(facesContext, VIEW_LOGIN_LOGICAL_VIEW_ID);
        // Preferences should be put on top of the navigation for redirect over
        // login.
        assertEquals(VIEW_PREFERENCES_LOGICAL_VIEW_ID, historyManagerMock.getCurrentView().getViewId());
    }

    @Test
    void shouldHandleNotAuthorizedExceptionWithRedirectToHome() {

        final var event = new ExceptionAsEvent(
                new UserNotAuthorizedException(ViewDescriptorImpl.builder().withLogicalViewId("/").build(),
                        Collections.emptyList(), Collections.emptyList()));
        facesContext.getViewRoot().setViewId(VIEW_PREFERENCES_LOGICAL_VIEW_ID);

        underTest.handle(event);

        LogAsserts.assertLogMessagePresentContaining(TestLogLevel.WARN, WARNING_KEY_PORTAL_002);

        assertTrue(event.isHandled());
        assertRedirect(facesContext, VIEW_HOME_LOGICAL_VIEW_ID);
        messageProducerMock
                .assertSingleGlobalMessageWithKeyPresent(ViewRelatedExceptionHandler.VIEW_INSUFFICIENT_PERMISSIONS_KEY);
    }

    @Test
    void shouldHandleNotAuthorizedExceptionWithRedirectToLogin() {

        viewRestrictionManagerMock.setAuthorized(false);

        final var event = new ExceptionAsEvent(
                new UserNotAuthorizedException(ViewDescriptorImpl.builder().withLogicalViewId("/").build(),
                        Collections.emptyList(), Collections.emptyList()));
        facesContext.getViewRoot().setViewId(VIEW_PREFERENCES_LOGICAL_VIEW_ID);

        underTest.handle(event);

        LogAsserts.assertLogMessagePresentContaining(TestLogLevel.WARN, WARNING_KEY_PORTAL_002);
        assertTrue(event.isHandled());

        assertRedirect(facesContext, VIEW_LOGOUT_LOGICAL_VIEW_ID);
        messageProducerMock
                .assertSingleGlobalMessageWithKeyPresent(ViewRelatedExceptionHandler.VIEW_INSUFFICIENT_PERMISSIONS_KEY);
    }

    @Test
    void shouldHandleViewExpiredExceptionForLoggedOutUser() {

        final var event = new ExceptionAsEvent(new ViewExpiredException(VIEW_PREFERENCES_LOGICAL_VIEW_ID));
        // Prepare history manager
        facesContext.getViewRoot().setViewId(VIEW_PREFERENCES_LOGICAL_VIEW_ID);
        portalUserProducerMock.authenticated(false);
        underTest.handle(event);
        assertTrue(event.isHandled());
        assertRedirect(facesContext, VIEW_LOGIN_LOGICAL_VIEW_ID);
    }

    private static void assertRedirect(FacesContext facesContext, String url) {
        var response = (MockHttpServletResponse) facesContext.getExternalContext().getResponse();
        assertEquals(url, response.getHeader("Location"), "Redirect URL mismatch");
    }
}
