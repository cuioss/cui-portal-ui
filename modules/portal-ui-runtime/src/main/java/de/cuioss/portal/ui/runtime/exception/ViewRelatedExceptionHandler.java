package de.cuioss.portal.ui.runtime.exception;

import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.NavigationHandler;
import javax.faces.application.ViewExpiredException;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.deltaspike.core.api.exception.control.ExceptionHandler;
import org.apache.deltaspike.core.api.exception.control.Handles;
import org.apache.deltaspike.core.api.exception.control.event.ExceptionEvent;

import de.cuioss.jsf.api.application.history.HistoryManager;
import de.cuioss.jsf.api.application.message.MessageProducer;
import de.cuioss.jsf.api.application.navigation.NavigationUtils;
import de.cuioss.jsf.api.common.view.ViewDescriptor;
import de.cuioss.portal.authentication.AuthenticatedUserInfo;
import de.cuioss.portal.authentication.PortalUser;
import de.cuioss.portal.ui.api.authentication.UserNotAuthenticatedException;
import de.cuioss.portal.ui.api.authentication.UserNotAuthorizedException;
import de.cuioss.portal.ui.api.history.PortalHistoryManager;
import de.cuioss.portal.ui.api.message.PortalMessageProducer;
import de.cuioss.portal.ui.api.ui.context.CuiCurrentView;
import de.cuioss.portal.ui.api.ui.context.CuiNavigationHandler;
import de.cuioss.portal.ui.api.ui.pages.ErrorPage;
import de.cuioss.portal.ui.api.ui.pages.HomePage;
import de.cuioss.portal.ui.api.ui.pages.LoginPage;
import de.cuioss.portal.ui.api.ui.pages.LogoutPage;
import de.cuioss.portal.ui.api.view.PortalViewRestrictionManager;
import de.cuioss.portal.ui.api.view.ViewRestrictionManager;
import de.cuioss.portal.ui.runtime.application.view.ViewSuppressedException;
import de.cuioss.tools.logging.CuiLogger;

/**
 * This {@link ExceptionHandler} provides handler methods for dealing with view
 * related exceptions, like {@link ViewSuppressedException},
 * {@link ViewExpiredException}, {@link UserNotAuthenticatedException} and
 * {@link UserNotAuthorizedException}
 *
 * @author Oliver Wolff
 */
@ExceptionHandler
@Named
@RequestScoped
public class ViewRelatedExceptionHandler implements Serializable {

    private static final CuiLogger log = new CuiLogger(ViewRelatedExceptionHandler.class);

    /**
     * Key for message to be displayed on {@link ViewSuppressedException}
     */
    public static final String VIEW_SUPPRESSED_KEY = "system.exception.view.suppressed";

    /**
     * Key for message to be displayed on {@link ViewExpiredException}
     */
    public static final String VIEW_EXPIRED_KEY = "system.exception.view.expired";

    /**
     * Key for message to be displayed on {@link UserNotAuthorizedException}
     */
    public static final String VIEW_INSUFFICIENT_PERMISSIONS_KEY = "system.exception.view.insufficient.permissions";

    private static final String PORTAL_103 = "Portal-103: View '{}' requires the roles '{}', but user '{}' only has the roles: '{}'";

    private static final String NAV_LOOP_ERROR_MSG = "Portal-505: The view '{}' is suppressed but is the designated navigation target at the same time."
            + " This would result in a loop. The error page is displayed therefore instead.";

    private static final long serialVersionUID = 2286427875463095109L;

    @Inject
    @CuiNavigationHandler
    private NavigationHandler navigationHandler;

    @Inject
    private FacesContext facesContext;

    @Inject
    @PortalMessageProducer
    private MessageProducer messageProducer;

    @Inject
    @PortalUser
    private AuthenticatedUserInfo authenticatedUserInfo;

    @Inject
    @PortalHistoryManager
    private HistoryManager historyManager;

    @Inject
    @CuiCurrentView
    private ViewDescriptor currentView;

    @Inject
    @PortalViewRestrictionManager
    private ViewRestrictionManager viewRestrictionManager;

    /**
     * Handles {@link ViewSuppressedException}
     *
     * @param event to be handled
     */
    void handleViewSupressedException(@Handles final ExceptionEvent<ViewSuppressedException> event) {
        messageProducer.setGlobalErrorMessage(VIEW_SUPPRESSED_KEY);
        navigationHandler.handleNavigation(facesContext, null, getFallbackNavigationOutcome(event));
        event.handled();
    }

    /**
     * Handles {@link ViewExpiredException}. In case the user is still authenticated
     * the previous page will be reloaded and an according message will be
     * displayed. Otherwise the user will be redirected to the login-page.
     *
     * @param event to be handled
     */
    void handleViewExpiredException(@Handles final ExceptionEvent<ViewExpiredException> event) {
        if (authenticatedUserInfo.isAuthenticated()) {
            messageProducer.setGlobalErrorMessage(VIEW_EXPIRED_KEY);
            historyManager.getCurrentView().redirect(facesContext);
        } else {
            navigationHandler.handleNavigation(facesContext, null, LoginPage.OUTCOME);
        }
        event.handled();

    }

    /**
     * Handles {@link UserNotAuthenticatedException}. The current request URI will
     * be stored with {@link HistoryManager} and the user will be redirected to the
     * login-page.
     *
     * @param event to be handled
     */
    void handleUserNotAuthenticatedException(@Handles final ExceptionEvent<UserNotAuthenticatedException> event) {
        log.debug("User is not logged in, redirecting to loginPage", event.getException());
        historyManager.addCurrentUriToHistory(currentView);
        navigationHandler.handleNavigation(facesContext, null, LoginPage.OUTCOME);
        event.handled();
    }

    /**
     * Handles {@link UserNotAuthorizedException}. The user will be redirected to
     * the home-page if he has sufficient views, otherwise to the logout-page. In
     * both cases a corresponding global-error message will be queued for display.
     *
     * @param event to be handled
     */
    void handleUserNotAuthorizedException(@Handles final ExceptionEvent<UserNotAuthorizedException> event) {

        log.warn(PORTAL_103, event.getException().getRequestedView().getLogicalViewId(),
                event.getException().getRequiredRoles(), authenticatedUserInfo.getDisplayName(),
                event.getException().getUserRoles());

        messageProducer.setGlobalErrorMessage(VIEW_INSUFFICIENT_PERMISSIONS_KEY);
        if (viewRestrictionManager.isUserAuthorizedForViewOutcome(HomePage.OUTCOME)) {
            navigationHandler.handleNavigation(facesContext, null, HomePage.OUTCOME);
        } else {
            navigationHandler.handleNavigation(facesContext, null, LogoutPage.OUTCOME);
        }
        event.handled();
    }

    /**
     * Helper methods that redirects to home or login, depending on authentication
     * status.
     */
    private String getFallbackNavigationOutcome(final ExceptionEvent<ViewSuppressedException> event) {
        var outcome = HomePage.OUTCOME;

        if (!authenticatedUserInfo.isAuthenticated()) {
            outcome = LoginPage.OUTCOME;
        } else if (null != event.getException() && null != event.getException().getSuppressedViewDescriptor()
                && null != event.getException().getSuppressedViewDescriptor().getLogicalViewId()
                && event.getException().getSuppressedViewDescriptor().getLogicalViewId()
                        .equals(NavigationUtils.lookUpToLogicalViewIdBy(facesContext, outcome))) {
            log.error(NAV_LOOP_ERROR_MSG, event.getException().getSuppressedViewDescriptor().getLogicalViewId());
            outcome = ErrorPage.OUTCOME;
        }

        return outcome;
    }
}
