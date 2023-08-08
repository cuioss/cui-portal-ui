package de.cuioss.portal.ui.runtime.application.listener.view;

import static de.cuioss.portal.configuration.PortalConfigurationKeys.PORTAL_LISTENER_HISTORYMANAGER;

import javax.annotation.Priority;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Event;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import de.cuioss.jsf.api.application.history.HistoryManager;
import de.cuioss.jsf.api.application.navigation.NavigationUtils;
import de.cuioss.jsf.api.application.navigation.ViewIdentifier;
import de.cuioss.jsf.api.common.view.ViewDescriptor;
import de.cuioss.portal.configuration.common.PortalPriorities;
import de.cuioss.portal.ui.api.events.PageRefreshEvent;
import de.cuioss.portal.ui.api.history.PortalHistoryManager;
import de.cuioss.portal.ui.api.listener.view.PhaseExecution;
import de.cuioss.portal.ui.api.listener.view.PortalRestoreViewListener;
import de.cuioss.portal.ui.api.listener.view.ViewListener;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

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

    private static final long serialVersionUID = 6342018758236517336L;

    @Inject
    @PortalHistoryManager
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
