/**
 * The
 * {@link de.cuioss.portal.ui.runtime.application.listener.view.PortalCDIViewListener}
 * is the central element of this package. For some checks, like authentication
 * and page suppression it is needed to be run before the phase
 * {@link javax.faces.event.PhaseId#RESTORE_VIEW} in order to prevent building
 * of that views. Instead of creating single
 * {@link javax.faces.event.PhaseListener} the
 * {@link de.cuioss.portal.ui.api.listener.view.PortalRestoreViewListener} will
 * pick up each implementation of
 * {@link de.cuioss.portal.ui.api.listener.view.ViewListener} annotated with
 * {@link de.cuioss.portal.ui.api.listener.view.PortalRestoreViewListener} and
 * calls the method
 * {@link de.cuioss.portal.ui.api.listener.view.ViewListener#handleView(String)}
 * with the currently requested viewID. The ordering of this listener will be
 * done the {@link javax.annotation.Priority} annotation: The higher the number
 * the earlier the corresponding listener will be called. The concrete listener
 * methods are void and need therefore the eventing / exception mechanism to
 * interact. See
 * {@link de.icw.cui.portal.configuration.application.listener.view.ViewAuthenticationListener}
 * for an example.
 *
 * @author Oliver Wolff
 */
package de.cuioss.portal.ui.runtime.application.listener.view;
