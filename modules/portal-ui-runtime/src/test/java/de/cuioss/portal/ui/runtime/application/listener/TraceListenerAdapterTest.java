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
package de.cuioss.portal.ui.runtime.application.listener;

import de.cuioss.portal.core.test.junit5.EnablePortalConfiguration;
import de.cuioss.portal.core.test.mocks.microprofile.PortalTestMetricRegistry;
import de.cuioss.portal.ui.runtime.application.listener.metrics.RequestTracer;
import de.cuioss.portal.ui.runtime.application.listener.metrics.TraceListener;
import de.cuioss.portal.ui.test.junit5.EnablePortalUiEnvironment;
import de.cuioss.test.juli.LogAsserts;
import de.cuioss.test.juli.TestLogLevel;
import de.cuioss.test.juli.junit5.EnableTestLogger;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.PhaseEvent;
import jakarta.faces.event.PhaseId;
import org.apache.myfaces.test.mock.lifecycle.MockLifecycle;
import org.jboss.weld.junit5.auto.AddBeanClasses;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static de.cuioss.portal.configuration.PortalConfigurationKeys.PORTAL_LISTENER_TRACE_ENABLED;
import static de.cuioss.portal.ui.runtime.application.listener.metrics.RequestTracer.PROCESSING_IDENTIFIER;
import static org.junit.jupiter.api.Assertions.assertEquals;

@EnablePortalUiEnvironment
@EnableTestLogger(debug = {TraceListener.class, RequestTracer.class})
@AddBeanClasses({RequestTracer.class, PortalTestMetricRegistry.class, TraceListener.class})
@EnablePortalConfiguration(configuration = PORTAL_LISTENER_TRACE_ENABLED + ":true")
class TraceListenerAdapterTest {

    private TraceListenerAdapter underTest;

    @BeforeEach
    void setTraceListener() {
        underTest = new TraceListenerAdapter();
    }

    @Test
    void shouldHandleHappyCase() throws Exception {
        var facesContext = FacesContext.getCurrentInstance();
        startSleepStop(facesContext, PhaseId.RESTORE_VIEW);
        startSleepStop(facesContext, PhaseId.APPLY_REQUEST_VALUES);
        startSleepStop(facesContext, PhaseId.RENDER_RESPONSE);
        LogAsserts.assertSingleLogMessagePresentContaining(TestLogLevel.DEBUG, PROCESSING_IDENTIFIER);
        LogAsserts.assertSingleLogMessagePresentContaining(TestLogLevel.DEBUG, PhaseId.RESTORE_VIEW.getName());
        LogAsserts.assertSingleLogMessagePresentContaining(TestLogLevel.DEBUG, PhaseId.APPLY_REQUEST_VALUES.getName());
        LogAsserts.assertSingleLogMessagePresentContaining(TestLogLevel.DEBUG, PhaseId.RENDER_RESPONSE.getName());
    }

    @Test
    void shouldBindToAnyPhase() {
        assertEquals(PhaseId.ANY_PHASE, underTest.getPhaseId());
    }

    private void startSleepStop(FacesContext facesContext, PhaseId phaseId) throws InterruptedException {
        underTest.beforePhase(phaseEvent(facesContext, phaseId));
        TimeUnit.MILLISECONDS.sleep(50);
        underTest.afterPhase(phaseEvent(facesContext, phaseId));
    }

    private PhaseEvent phaseEvent(FacesContext facesContext, PhaseId id) {
        return new PhaseEvent(facesContext, id, new MockLifecycle());
    }

}
