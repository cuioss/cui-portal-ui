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

import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

import de.cuioss.portal.core.cdi.PortalBeanManager;
import de.cuioss.portal.ui.runtime.application.listener.metrics.TraceListener;

/**
 * Replacement for deltaspike phase listener. In essence it routes the
 * corresponding calls to {@link TraceListener}
 */
public class TraceListenerAdapter implements PhaseListener {

    private static final long serialVersionUID = -7081872463546867289L;

    private static final String UNABLE_TO_ACCESS_TRACE_LISTENER = "Unable to access TraceListener";

    @Override
    public void afterPhase(PhaseEvent event) {
        PortalBeanManager.resolveBean(TraceListener.class, null)
                .orElseThrow(() -> new IllegalStateException(UNABLE_TO_ACCESS_TRACE_LISTENER)).afterPhase(event);

    }

    @Override
    public void beforePhase(PhaseEvent event) {
        PortalBeanManager.resolveBean(TraceListener.class, null)
                .orElseThrow(() -> new IllegalStateException(UNABLE_TO_ACCESS_TRACE_LISTENER)).beforePhase(event);

    }

    @Override
    public PhaseId getPhaseId() {
        return PhaseId.ANY_PHASE;
    }

}
