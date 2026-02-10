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

import de.cuioss.jsf.api.common.view.ViewDescriptor;
import de.cuioss.portal.common.priority.PortalPriorities;
import de.cuioss.portal.ui.api.exception.ExceptionAsEvent;
import de.cuioss.portal.ui.api.listener.view.PhaseExecution;
import de.cuioss.portal.ui.api.listener.view.PortalRestoreViewListener;
import de.cuioss.portal.ui.api.listener.view.ViewListener;
import de.cuioss.portal.ui.runtime.application.configuration.ViewConfiguration;
import de.cuioss.portal.ui.runtime.application.view.ViewSuppressedException;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.io.Serial;

import static de.cuioss.portal.configuration.PortalConfigurationKeys.PORTAL_LISTENER_VIEW_SUPPRESSION;

/**
 * Checks whether the current page is to be suppressed and fires an
 * {@link ViewSuppressedException} if so.
 *
 * @author Oliver Wolff
 */
@PortalRestoreViewListener(PhaseExecution.AFTER_PHASE)
@Priority(PortalPriorities.PORTAL_CORE_LEVEL + 1)
// Must be called before AuthenticationListener
@RequestScoped
@EqualsAndHashCode(of = "viewConfiguration")
@ToString(of = "viewConfiguration")
public class ViewSuppressionListener implements ViewListener {

    @Serial
    private static final long serialVersionUID = -1757867591272259164L;

    @Inject
    private ViewConfiguration viewConfiguration;

    @Inject
    private Event<ExceptionAsEvent> catchEvent;

    @Getter
    @Inject
    @ConfigProperty(name = PORTAL_LISTENER_VIEW_SUPPRESSION)
    private boolean enabled;

    @Override
    public void handleView(final ViewDescriptor viewDescriptor) {
        if (this.viewConfiguration.getSuppressedViewMatcher().match(viewDescriptor)) {
            this.catchEvent.fire(new ExceptionAsEvent(new ViewSuppressedException(viewDescriptor)));
        }
    }

}
