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

import de.cuioss.jsf.api.application.message.MessageProducer;
import de.cuioss.jsf.api.application.navigation.NavigationUtils;
import de.cuioss.jsf.api.common.view.ViewDescriptor;
import de.cuioss.portal.authentication.AuthenticatedUserInfo;
import de.cuioss.portal.ui.api.authentication.UserNotAuthenticatedException;
import de.cuioss.portal.ui.api.authentication.UserNotAuthorizedException;
import de.cuioss.portal.ui.api.context.CuiCurrentView;
import de.cuioss.portal.ui.api.context.CuiNavigationHandler;
import de.cuioss.portal.ui.api.exception.ExceptionAsEvent;
import de.cuioss.portal.ui.api.exception.HandleOutcome;
import de.cuioss.portal.ui.api.exception.PortalExceptionHandler;
import de.cuioss.portal.ui.api.history.HistoryManager;
import de.cuioss.portal.ui.api.pages.ErrorPage;
import de.cuioss.portal.ui.api.pages.HomePage;
import de.cuioss.portal.ui.api.pages.LoginPage;
import de.cuioss.portal.ui.api.pages.LogoutPage;
import de.cuioss.portal.ui.api.view.PortalViewRestrictionManager;
import de.cuioss.portal.ui.api.view.ViewRestrictionManager;
import de.cuioss.portal.ui.runtime.application.view.ViewSuppressedException;
import de.cuioss.tools.logging.CuiLogger;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.NavigationHandler;
import jakarta.faces.application.ViewExpiredException;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;

/**
 * This {@link PortalExceptionHandler} provides handler methods for dealing with
 * view related exceptions, like {@link ViewSuppressedException},
 * {@link ViewExpiredException}, {@link UserNotAuthenticatedException} and
 * {@link UserNotAuthorizedException}
 *
 * @author Oliver Wolff
 */
@RequestScoped
public class ViewRelatedExceptionHandler implements PortalExceptionHandler {

    private static final String HANDLING_S_AS_S = "Handling '%s' as '%s'";

    private static final CuiLogger LOGGER = new CuiLogger(ViewRelatedExceptionHandler.class);

    /**
     * Key for the message to be displayed on {@link ViewSuppressedException}
     */
    public static final String VIEW_SUPPRESSED_KEY = "system.exception.view.suppressed";

    /**
     * Key for the message to be displayed on {@link ViewExpiredException}
     */
    public static final String VIEW_EXPIRED_KEY = "system.exception.view.expired";

    /**
     * Key for the message to be displayed on {@link UserNotAuthorizedException}
     */
    public static final String VIEW_INSUFFICIENT_PERMISSIONS_KEY = "system.exception.view.insufficient.permissions";

    private static final String PORTAL_103 = "Portal-103: View '{}' requires the roles '{}', but user '{}' only has the roles: '{}'";

    private static final String NAV_LOOP_ERROR_MSG = """
            Portal-505: The view '{}' is suppressed but is the designated navigation target at the same time.\
             This would result in a loop. The error page is displayed therefore instead.\
            """;

    @Inject
    @CuiNavigationHandler
    NavigationHandler navigationHandler;

    @Inject
    FacesContext facesContext;

    @Inject
    MessageProducer messageProducer;

    @Inject
    AuthenticatedUserInfo authenticatedUserInfo;

    @Inject
    HistoryManager historyManager;

    @Inject
    @CuiCurrentView
    ViewDescriptor currentView;

    @Inject
    @PortalViewRestrictionManager
    ViewRestrictionManager viewRestrictionManager;

    @Override
    public void handle(ExceptionAsEvent exceptionEvent) {
        if (exceptionEvent.getException() instanceof ViewSuppressedException) {
            LOGGER.debug(HANDLING_S_AS_S, exceptionEvent, ViewSuppressedException.class);
            handleViewSuppressedException(exceptionEvent);
        } else if (exceptionEvent.getException() instanceof ViewExpiredException) {
            LOGGER.debug(HANDLING_S_AS_S, exceptionEvent, ViewExpiredException.class);
            handleViewExpiredException(exceptionEvent);
        } else if (exceptionEvent.getException() instanceof UserNotAuthenticatedException) {
            LOGGER.debug(HANDLING_S_AS_S, exceptionEvent, UserNotAuthenticatedException.class);
            handleUserNotAuthenticatedException(exceptionEvent);
        } else if (exceptionEvent.getException() instanceof UserNotAuthorizedException) {
            LOGGER.debug(HANDLING_S_AS_S, exceptionEvent, UserNotAuthorizedException.class);
            handleUserNotAuthorizedException(exceptionEvent);
        }

    }

    /**
     * Handles {@link ViewSuppressedException}
     *
     * @param event to be handled
     */
    private void handleViewSuppressedException(ExceptionAsEvent event) {
        messageProducer.setGlobalErrorMessage(VIEW_SUPPRESSED_KEY);
        var outcome = HomePage.OUTCOME;
        var exception = (ViewSuppressedException) event.getException();
        if (!authenticatedUserInfo.isAuthenticated()) {
            outcome = LoginPage.OUTCOME;
        } else if (null != exception.getSuppressedViewDescriptor()
                && null != exception.getSuppressedViewDescriptor().getLogicalViewId()
                && exception.getSuppressedViewDescriptor().getLogicalViewId()
                        .equals(NavigationUtils.lookUpToLogicalViewIdBy(facesContext, outcome))) {
            LOGGER.error(NAV_LOOP_ERROR_MSG, exception.getSuppressedViewDescriptor().getLogicalViewId());
            outcome = ErrorPage.OUTCOME;
        }
        navigationHandler.handleNavigation(facesContext, null, outcome);
        event.handled(HandleOutcome.REDIRECT);
    }

    /**
     * Handles {@link ViewExpiredException}. In case the user is still authenticated,
     * the previous page will be reloaded, and an according message will be
     * displayed. Otherwise, the user will be redirected to the login-page.
     *
     * @param event to be handled
     */
    private void handleViewExpiredException(ExceptionAsEvent event) {
        if (authenticatedUserInfo.isAuthenticated()) {
            messageProducer.setGlobalErrorMessage(VIEW_EXPIRED_KEY);
            historyManager.getCurrentView().redirect(facesContext);
        } else {
            navigationHandler.handleNavigation(facesContext, null, LoginPage.OUTCOME);
        }
        event.handled(HandleOutcome.REDIRECT);

    }

    /**
     * Handles {@link UserNotAuthenticatedException}. The current request URI will
     * be stored with {@link HistoryManager} and the user will be redirected to the
     * login-page.
     *
     * @param event to be handled
     */
    private void handleUserNotAuthenticatedException(final ExceptionAsEvent event) {
        LOGGER.debug("User is not logged in, redirecting to loginPage", event.getException());
        historyManager.addCurrentUriToHistory(currentView);
        navigationHandler.handleNavigation(facesContext, null, LoginPage.OUTCOME);
        event.handled(HandleOutcome.REDIRECT);
    }

    /**
     * Handles {@link UserNotAuthorizedException}. The user will be redirected to
     * the home-page if he has sufficient views, otherwise to the logout-page. In
     * both cases, a corresponding global-error message will be queued for display.
     *
     * @param event to be handled
     */
    private void handleUserNotAuthorizedException(ExceptionAsEvent event) {

        var exception = (UserNotAuthorizedException) event.getException();

        LOGGER.warn(PORTAL_103, exception.getRequestedView().getLogicalViewId(), exception.getRequiredRoles(),
                authenticatedUserInfo.getDisplayName(), exception.getUserRoles());

        messageProducer.setGlobalErrorMessage(VIEW_INSUFFICIENT_PERMISSIONS_KEY);
        if (viewRestrictionManager.isUserAuthorizedForViewOutcome(HomePage.OUTCOME)) {
            navigationHandler.handleNavigation(facesContext, null, HomePage.OUTCOME);
        } else {
            navigationHandler.handleNavigation(facesContext, null, LogoutPage.OUTCOME);
        }
        event.handled(HandleOutcome.REDIRECT);
    }

}
