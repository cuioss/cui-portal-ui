package de.cuioss.portal.ui.runtime.application.listener.view;

import static de.cuioss.portal.configuration.PortalConfigurationKeys.PORTAL_LISTENER_STICKYMESSAGES;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.Priority;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import de.cuioss.jsf.api.common.view.ViewDescriptor;
import de.cuioss.portal.configuration.common.PortalPriorities;
import de.cuioss.portal.ui.api.listener.view.PhaseExecution;
import de.cuioss.portal.ui.api.listener.view.PortalRestoreViewListener;
import de.cuioss.portal.ui.api.listener.view.ViewListener;
import de.cuioss.portal.ui.api.message.PortalStickyMessageProducer;
import de.cuioss.portal.ui.api.message.StickyMessage;
import de.cuioss.portal.ui.api.message.StickyMessageProducer;
import de.cuioss.portal.ui.api.message.StickyMessageProvider;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * View Listener act on each page. The listener collect new available sticky
 * messages for all registered StickyMessageProviders and put them to
 * StickyMessageProducer. Expected is that StickyMessageProducer is a session
 * scoped bean.
 *
 * @author i000576
 */
@PortalRestoreViewListener(PhaseExecution.BEFORE_PHASE)
@RequestScoped
@Priority(PortalPriorities.PORTAL_CORE_LEVEL)
@ToString
@EqualsAndHashCode
public class StickyMessageCollectorViewListener implements ViewListener {

    private static final long serialVersionUID = 1106398052992964929L;

    @Inject
    private Instance<StickyMessageProvider> providers;

    @Inject
    @PortalStickyMessageProducer
    private StickyMessageProducer stickyMessageProducer;

    @Getter
    @Inject
    @ConfigProperty(name = PORTAL_LISTENER_STICKYMESSAGES)
    private boolean enabled;

    @Override
    public void handleView(final ViewDescriptor viewDescriptor) {
        final var collected = collectAndCleanup();
        collected.forEach(sMessage -> stickyMessageProducer.addMessage(sMessage));
    }

    private Set<StickyMessage> collectAndCleanup() {
        final Set<StickyMessage> collected = new HashSet<>(0);
        for (final StickyMessageProvider provider : providers) {
            collected.addAll(provider.retrieveMessages());
        }
        return collected;
    }
}
