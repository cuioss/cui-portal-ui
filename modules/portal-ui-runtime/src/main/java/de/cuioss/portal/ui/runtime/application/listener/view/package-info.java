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
