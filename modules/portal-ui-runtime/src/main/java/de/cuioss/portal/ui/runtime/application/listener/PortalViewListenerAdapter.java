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
package de.cuioss.portal.ui.runtime.application.listener;

import jakarta.faces.event.PhaseEvent;
import jakarta.faces.event.PhaseId;
import jakarta.faces.event.PhaseListener;

import de.cuioss.portal.common.cdi.PortalBeanManager;
import de.cuioss.portal.ui.api.listener.view.PortalRestoreViewListener;
import de.cuioss.portal.ui.runtime.application.listener.view.PortalCDIViewListener;

import java.io.Serial;

/**
 * Replacement for deltaspike phase listener. In essence, it routes the
 * corresponding calls to {@link PortalRestoreViewListener}
 */
public class PortalViewListenerAdapter implements PhaseListener {

    private static final String UNABLE_TO_ACCESS_PORTAL_CDI_VIEW_LISTENER = "Unable to access PortalCDIViewListener";
    @Serial
    private static final long serialVersionUID = -4417626731612443835L;

    @Override
    public void afterPhase(PhaseEvent event) {
        PortalBeanManager.resolveBean(PortalCDIViewListener.class, null)
                .orElseThrow(() -> new IllegalStateException(UNABLE_TO_ACCESS_PORTAL_CDI_VIEW_LISTENER))
                .afterPhase(event);

    }

    @Override
    public void beforePhase(PhaseEvent event) {
        PortalBeanManager.resolveBean(PortalCDIViewListener.class, null)
                .orElseThrow(() -> new IllegalStateException(UNABLE_TO_ACCESS_PORTAL_CDI_VIEW_LISTENER))
                .beforePhase(event);

    }

    @Override
    public PhaseId getPhaseId() {
        return PhaseId.RESTORE_VIEW;
    }

}
