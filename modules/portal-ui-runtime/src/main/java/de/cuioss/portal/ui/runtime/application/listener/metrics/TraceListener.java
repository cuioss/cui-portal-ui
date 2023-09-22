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
package de.cuioss.portal.ui.runtime.application.listener.metrics;

import static de.cuioss.portal.configuration.PortalConfigurationKeys.PORTAL_LISTENER_TRACE_ENABLED;

import javax.enterprise.context.RequestScoped;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import de.cuioss.portal.configuration.PortalConfigurationKeys;
import de.cuioss.tools.logging.CuiLogger;

/**
 * This feature is enabled by the configuration of the key
 * {@link PortalConfigurationKeys#PORTAL_LISTENER_TRACE_ENABLED}. In order to
 * materialize at the log the Logger for {@link RequestTracer} must be set to
 * debug.
 *
 * @author Oliver Wolff
 *
 */
@RequestScoped
public class TraceListener implements PhaseListener {

    private static final String DISABLED_BY_CONFIGURATION = "Disabled by configuration";

    private static final long serialVersionUID = -3549927752283027321L;

    private static final CuiLogger LOGGER = new CuiLogger(TraceListener.class);

    @Inject
    @ConfigProperty(name = PORTAL_LISTENER_TRACE_ENABLED)
    private boolean enabled;

    @Inject
    private RequestTracer requestTracer;

    @Override
    public void beforePhase(PhaseEvent event) {
        if (enabled) {
            requestTracer.start(event.getPhaseId());
        } else {
            LOGGER.trace(DISABLED_BY_CONFIGURATION);
        }
    }

    @Override
    public void afterPhase(PhaseEvent event) {
        if (enabled) {
            requestTracer.stop(event.getPhaseId());
            if (PhaseId.RENDER_RESPONSE.equals(event.getPhaseId())) {
                requestTracer.writeStatistics();
            }
        } else {
            LOGGER.trace(DISABLED_BY_CONFIGURATION);
        }

    }

    @Override
    public PhaseId getPhaseId() {
        return PhaseId.ANY_PHASE;
    }

}
