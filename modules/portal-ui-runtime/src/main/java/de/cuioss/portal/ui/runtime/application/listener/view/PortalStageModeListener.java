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

import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;

import de.cuioss.jsf.api.common.view.ViewDescriptor;

import jakarta.annotation.Priority;
import de.cuioss.portal.common.priority.PortalPriorities;
import de.cuioss.portal.ui.api.configuration.PortalNotConfiguredException;
import de.cuioss.portal.ui.api.exception.ExceptionAsEvent;
import de.cuioss.portal.ui.api.listener.view.PhaseExecution;
import de.cuioss.portal.ui.api.listener.view.PortalRestoreViewListener;
import de.cuioss.portal.ui.api.listener.view.ViewListener;
import de.cuioss.uimodel.application.CuiProjectStage;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * This listener checks the portal stage configuration. In case the portal stage
 * is 'configuration' it will fire an {@link PortalNotConfiguredException}.
 *
 * @author Sven Haag
 */
@PortalRestoreViewListener(PhaseExecution.AFTER_PHASE)
@RequestScoped
@Priority(PortalPriorities.PORTAL_CORE_LEVEL + 5) // Must be called before other listeners
@EqualsAndHashCode(of = "stageProducer")
@ToString(of = "stageProducer")
public class PortalStageModeListener implements ViewListener {

    private static final long serialVersionUID = 8427405526881056257L;

    @Inject
    private CuiProjectStage stageProducer;

    @Inject
    private Event<ExceptionAsEvent> catchEvent;

    @Override
    public void handleView(final ViewDescriptor viewDescriptor) {
        if (stageProducer.isConfiguration()) {
            catchEvent.fire(new ExceptionAsEvent(new PortalNotConfiguredException()));
        }
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
