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

import static de.cuioss.portal.configuration.PortalConfigurationKeys.PORTAL_LISTENER_VIEW_SUPRESSION;

import javax.annotation.Priority;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import de.cuioss.jsf.api.common.view.ViewDescriptor;
import de.cuioss.portal.common.priority.PortalPriorities;
import de.cuioss.portal.ui.api.exception.ExceptionAsEvent;
import de.cuioss.portal.ui.api.listener.view.PhaseExecution;
import de.cuioss.portal.ui.api.listener.view.PortalRestoreViewListener;
import de.cuioss.portal.ui.api.listener.view.ViewListener;
import de.cuioss.portal.ui.runtime.application.configuration.ViewConfiguration;
import de.cuioss.portal.ui.runtime.application.view.ViewSuppressedException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Checks whether the current page is to be suppressed and fires an
 * {@link ViewSuppressedException} if so.
 *
 * @author Oliver Wolff
 */
@PortalRestoreViewListener(PhaseExecution.AFTER_PHASE)
@Priority(PortalPriorities.PORTAL_CORE_LEVEL + 1)
// Must be called before AuthenticatationListener
@RequestScoped
@EqualsAndHashCode(of = "viewConfiguration")
@ToString(of = "viewConfiguration")
public class ViewSupressionListener implements ViewListener {

    private static final long serialVersionUID = -1757867591272259164L;

    @Inject
    private ViewConfiguration viewConfiguration;

    @Inject
    private Event<ExceptionAsEvent> catchEvent;

    @Getter
    @Inject
    @ConfigProperty(name = PORTAL_LISTENER_VIEW_SUPRESSION)
    private boolean enabled;

    @Override
    public void handleView(final ViewDescriptor viewDescriptor) {
        if (this.viewConfiguration.getSuppressedViewMatcher().match(viewDescriptor)) {
            this.catchEvent.fire(new ExceptionAsEvent(new ViewSuppressedException(viewDescriptor)));
        }
    }

}
