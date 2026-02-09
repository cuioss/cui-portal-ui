/**
 * Provides methods and structure user for handling of exceptions.
 * <p>
 * In general it is a simplified variant of deltaspikes exception-handling
 * <p>
 * The preferred way of exception handling is:
 * <ul>
 * <li>Fire an event of type
 * {@link de.cuioss.portal.ui.api.exception.ExceptionAsEvent} with the
 * corresponding {@link java.lang.Throwable} as payload</li>
 * <li>Provide an instance of
 * {@link de.cuioss.portal.ui.api.exception.PortalExceptionHandler} as a
 * {@link jakarta.enterprise.context.RequestScoped} bean</li>
 * <li>The rest will be done by the framework. For examples see implementations
 * within 'portal-ui-runtime', e.g.
 * 'de.cuioss.portal.ui.runtime.exception.ViewRelatedExceptionHandler' or
 * 'de.cuioss.portal.ui.oauth.OauthExceptionHandler' in 'portal-ui-oauth'</li>
 * </ul>
 *
 * @author Oliver Wolff
 */
package de.cuioss.portal.ui.api.exception;
