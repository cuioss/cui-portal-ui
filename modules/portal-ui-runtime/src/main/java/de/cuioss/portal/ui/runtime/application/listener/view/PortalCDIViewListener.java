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
package de.cuioss.portal.ui.runtime.application.listener.view;

import static de.cuioss.tools.collect.CollectionLiterals.mutableList;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Instance;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.inject.Inject;
import javax.inject.Provider;

import de.cuioss.jsf.api.common.util.CheckContextState;
import de.cuioss.jsf.api.common.view.ViewDescriptor;
import de.cuioss.portal.configuration.common.PortalPriorities;
import de.cuioss.portal.ui.api.listener.view.PhaseExecution;
import de.cuioss.portal.ui.api.listener.view.PortalRestoreViewListener;
import de.cuioss.portal.ui.api.listener.view.ViewListener;
import de.cuioss.portal.ui.api.ui.context.CuiCurrentView;
import de.cuioss.tools.logging.CuiLogger;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Collects instances of {@link ViewListener}, sorts them and calls them with
 * the requested view id.
 *
 * @author Oliver Wolff
 */
@RequestScoped
@EqualsAndHashCode(of = { "sortedAfterListeners", "sortedAfterNonPostbackListeners", "sortedBeforeListeners" })
@ToString(of = { "sortedAfterListeners", "sortedAfterNonPostbackListeners", "sortedBeforeListeners" })
public class PortalCDIViewListener implements PhaseListener {

    private static final long serialVersionUID = 7620545331100921567L;

    private static final CuiLogger log = new CuiLogger(PortalCDIViewListener.class);

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

    }

    @Override
    public void afterPhase(final PhaseEvent event) {
        if (sortedAfterListeners.isEmpty() && sortedAfterNonPostbackListeners.isEmpty()) {
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
            log.warn("Unable to determine view.");
        }
    }

    private void handleListener(List<ViewListener> listeners, FacesContext context, ViewDescriptor currentView) {
        for (final ViewListener listener : listeners) {
            if (CheckContextState.isResponseNotComplete(context) && listener.isEnabled()) {
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
                        listener.handleView(currentView);
                    }
                }
            } else {
                log.warn("Unable to determine view.");
            }
        }
    }

    @Override
    public PhaseId getPhaseId() {
        return PhaseId.RESTORE_VIEW;
    }

}
