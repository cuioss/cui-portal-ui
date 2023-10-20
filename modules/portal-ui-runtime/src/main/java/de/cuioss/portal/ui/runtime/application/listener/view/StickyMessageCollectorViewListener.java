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

import static de.cuioss.portal.configuration.PortalConfigurationKeys.PORTAL_LISTENER_STICKYMESSAGES;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.Priority;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import de.cuioss.jsf.api.common.view.ViewDescriptor;
import de.cuioss.portal.common.priority.PortalPriorities;
import de.cuioss.portal.ui.api.listener.view.PhaseExecution;
import de.cuioss.portal.ui.api.listener.view.PortalRestoreViewListener;
import de.cuioss.portal.ui.api.listener.view.ViewListener;
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
