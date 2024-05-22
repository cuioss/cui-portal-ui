/**
 * The
 * {@link de.cuioss.portal.ui.runtime.application.listener.view.PortalCDIViewListener}
 * is the central element of this package. For some checks, like authentication
 * and page suppression it is needed to be run before the phase
 * {@link jakarta.faces.event.PhaseId#RESTORE_VIEW} in order to prevent building
 * of that views. Instead of creating single
 * {@link jakarta.faces.event.PhaseListener} the
 * {@link de.cuioss.portal.ui.api.listener.view.PortalRestoreViewListener} will
 * pick up each implementation of
 * {@link de.cuioss.portal.ui.api.listener.view.ViewListener} annotated with
 * {@link de.cuioss.portal.ui.api.listener.view.PortalRestoreViewListener} and
 * calls the method
 * {@link de.cuioss.portal.ui.api.listener.view.ViewListener#handleView(de.cuioss.jsf.api.common.view.ViewDescriptor)}
 * with the currently requested viewID. The ordering of this listener will be
 * done the {@link jakarta.annotation.Priority} annotation: The higher the number
 * the earlier the corresponding listener will be called. The concrete listener
 * methods are void and need therefore the eventing / exception mechanism to
 * interact. See
 * {@link de.cuioss.portal.ui.runtime.application.listener.view.ViewAuthenticationListener}
 * for example.
 *
 * @author Oliver Wolff
 */
package de.cuioss.portal.ui.runtime.application.listener.view;
