/**
 * Provides the deltaspike based exception handler for the portal, dealing with
 * the following exceptions:
 * {@link de.cuioss.portal.ui.runtime.exception.ViewRelatedExceptionHandler}:
 * <ul>
 * <li>{@link jakarta.faces.application.ViewExpiredException}</li>
 * <li>{@link de.cuioss.portal.ui.api.authentication.UserNotAuthenticatedException}</li>
 * <li>{@link de.cuioss.portal.ui.runtime.application.view.ViewSuppressedException}</li>
 * </ul>
 * Dealing means redirecting to
 * {@link de.cuioss.portal.ui.api.ui.pages.HomePage#OUTCOME} or
 * {@link de.cuioss.portal.ui.api.ui.pages.LoginPage#OUTCOME} depending on
 * {@link de.cuioss.portal.authentication.AuthenticatedUserInfo#isAuthenticated()}
 * <p>
 * {@link de.cuioss.portal.ui.runtime.exception.FallBackExceptionHandler}: Last line
 * of defense displaying the error page for all exceptions that are not handled.
 * It is deactivated on
 * {@link jakarta.faces.application.ProjectStage#Development}, because the default
 * facelets error page provides more default information for debugging.
 * </p>
 */
package de.cuioss.portal.ui.runtime.exception;
