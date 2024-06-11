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

import static de.cuioss.portal.configuration.PortalConfigurationKeys.PORTAL_LISTENER_HISTORYMANAGER;

import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.event.Event;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;

import jakarta.annotation.Priority;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import de.cuioss.jsf.api.application.navigation.NavigationUtils;
import de.cuioss.jsf.api.application.navigation.ViewIdentifier;
import de.cuioss.jsf.api.common.view.ViewDescriptor;
import de.cuioss.portal.common.priority.PortalPriorities;
import de.cuioss.portal.ui.api.events.PageRefreshEvent;
import de.cuioss.portal.ui.api.history.HistoryManager;
import de.cuioss.portal.ui.api.listener.view.PhaseExecution;
import de.cuioss.portal.ui.api.listener.view.PortalRestoreViewListener;
import de.cuioss.portal.ui.api.listener.view.ViewListener;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.io.Serial;

/**
 * Listener that calls
 * {@link HistoryManager#addCurrentUriToHistory(ViewDescriptor)}
 *
 * @author Oliver Wolff
 */
@PortalRestoreViewListener(PhaseExecution.AFTER_PHASE_EXCLUDE_POSTBACK)
@RequestScoped
@Priority(PortalPriorities.PORTAL_CORE_LEVEL)
@EqualsAndHashCode
@ToString
public class HistoryManagerListener implements ViewListener {

    @Serial
    private static final long serialVersionUID = 6342018758236517336L;

    @Inject
    private HistoryManager historyManager;

    @Inject
    private Event<PageRefreshEvent> event;

    @Getter
    @Inject
    @ConfigProperty(name = PORTAL_LISTENER_HISTORYMANAGER)
    private boolean enabled;

    @Override
    public void handleView(final ViewDescriptor viewDescriptor) {

        if (requestWasPageReload()) {
            historyManager.setPageReload(true);
            event.fire(new PageRefreshEvent(viewDescriptor.getViewId()));
        } else {
            historyManager.setPageReload(false);
        }
        historyManager.addCurrentUriToHistory(viewDescriptor);
    }

    private boolean requestWasPageReload() {
        final var context = FacesContext.getCurrentInstance();
        var reload = false;
        if (!context.isPostback()) {

            final var current = ViewIdentifier.getFromViewDesciptor(NavigationUtils.getCurrentView(context),
                    historyManager.getParameterFilter());
            if (null != current) {
                reload = current.equals(historyManager.getCurrentView());
            }
        }
        return reload;
    }

}
