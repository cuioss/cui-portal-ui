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
package de.cuioss.portal.ui.runtime.application.listener.view;

import de.cuioss.jsf.api.common.util.CheckContextState;
import de.cuioss.jsf.api.common.view.ViewDescriptor;
import de.cuioss.portal.common.priority.PortalPriorities;
import de.cuioss.portal.ui.api.context.CuiCurrentView;
import de.cuioss.portal.ui.api.listener.view.PhaseExecution;
import de.cuioss.portal.ui.api.listener.view.PortalRestoreViewListener;
import de.cuioss.portal.ui.api.listener.view.ViewListener;
import de.cuioss.tools.logging.CuiLogger;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.inject.Instance;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.PhaseEvent;
import jakarta.faces.event.PhaseId;
import jakarta.faces.event.PhaseListener;
import jakarta.inject.Inject;
import jakarta.inject.Provider;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serial;
import java.util.List;

import static de.cuioss.portal.ui.runtime.PortalUiRuntimeLogMessages.*;
import static de.cuioss.tools.collect.CollectionLiterals.mutableList;

/**
 * Collects instances of {@link ViewListener}, sorts them and calls them with
 * the requested view id.
 *
 * @author Oliver Wolff
 */
@RequestScoped
@EqualsAndHashCode(of = {"sortedAfterListeners", "sortedAfterNonPostbackListeners", "sortedBeforeListeners"})
@ToString(of = {"sortedAfterListeners", "sortedAfterNonPostbackListeners", "sortedBeforeListeners"})
public class PortalCDIViewListener implements PhaseListener {

    @Serial
    private static final long serialVersionUID = 7620545331100921567L;

    private static final CuiLogger LOGGER = new CuiLogger(PortalCDIViewListener.class);

    @Inject
    @PortalRestoreViewListener(PhaseExecution.BEFORE_PHASE)
    private Instance<ViewListener> beforeListeners;

    @Inject
    @CuiCurrentView
    private Provider<ViewDescriptor> currentViewProvider;

    private List<ViewListener> sortedBeforeListeners;

    @Inject
    @PortalRestoreViewListener(PhaseExecution.AFTER_PHASE)
    private Instance<ViewListener> afterListeners;

    private List<ViewListener> sortedAfterListeners;

    @Inject
    @PortalRestoreViewListener(PhaseExecution.AFTER_PHASE_EXCLUDE_POSTBACK)
    private Instance<ViewListener> afterListenersNonPostback;

    private List<ViewListener> sortedAfterNonPostbackListeners;

    /**
     * Sorts and filters the injected listeners
     */
    @PostConstruct
    public void init() {
        sortedBeforeListeners = PortalPriorities.sortByPriority(mutableList(beforeListeners));
        sortedAfterListeners = PortalPriorities.sortByPriority(mutableList(afterListeners));
        sortedAfterNonPostbackListeners = PortalPriorities.sortByPriority(mutableList(afterListenersNonPostback));
        LOGGER.debug("sortedBeforeListeners='%s'", sortedBeforeListeners);
        LOGGER.debug("sortedAfterListeners='%s'", sortedAfterListeners);
        LOGGER.debug("sortedAfterNonPostbackListeners='%s'", sortedAfterNonPostbackListeners);

    }

    @Override
    public void afterPhase(final PhaseEvent event) {
        if (sortedAfterListeners.isEmpty() && sortedAfterNonPostbackListeners.isEmpty()) {
            LOGGER.debug("No afterPhaseListener present");
            return;
        }
        final var context = event.getFacesContext();
        final var currentView = currentViewProvider.get();
        if (currentView.isViewDefined()) {
            handleListener(sortedAfterListeners, context, currentView);
            if (!event.getFacesContext().isPostback()) {
                handleListener(sortedAfterNonPostbackListeners, context, currentView);
            }
        } else {
            LOGGER.warn(WARN.UNABLE_TO_DETERMINE_VIEW);
        }
    }

    private void handleListener(List<ViewListener> listeners, FacesContext context, ViewDescriptor currentView) {
        for (final ViewListener listener : listeners) {
            if (CheckContextState.isResponseNotComplete(context) && listener.isEnabled()) {
                LOGGER.trace("Executing Listener '%s' on view '%s'", listener, currentView);
                listener.handleView(currentView);
            }
        }
    }

    @Override
    public void beforePhase(final PhaseEvent event) {
        if (!sortedBeforeListeners.isEmpty()) {
            final var currentView = currentViewProvider.get();
            final var context = event.getFacesContext();
            if (currentView.isViewDefined()) {
                for (final ViewListener listener : sortedBeforeListeners) {
                    if (CheckContextState.isResponseNotComplete(context) && listener.isEnabled()) {
                        LOGGER.trace("Executing Listener '%s' on view '%s'", listener, currentView);
                        listener.handleView(currentView);
                    }
                }
            } else {
                LOGGER.warn(WARN.UNABLE_TO_DETERMINE_VIEW);
            }
        }
    }

    @Override
    public PhaseId getPhaseId() {
        return PhaseId.RESTORE_VIEW;
    }

}
