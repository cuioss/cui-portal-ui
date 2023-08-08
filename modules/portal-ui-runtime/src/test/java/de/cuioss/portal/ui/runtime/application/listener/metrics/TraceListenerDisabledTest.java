package de.cuioss.portal.ui.runtime.application.listener.metrics;

import static de.cuioss.portal.configuration.PortalConfigurationKeys.PORTAL_LISTENER_TRACE_ENABLED;

import java.util.concurrent.TimeUnit;

import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.inject.Inject;

import org.apache.myfaces.test.mock.lifecycle.MockLifecycle;
import org.jboss.weld.junit5.auto.AddBeanClasses;
import org.junit.jupiter.api.Test;

import de.cuioss.portal.core.test.junit5.EnablePortalConfiguration;
import de.cuioss.portal.ui.test.junit5.EnablePortalUiEnvironment;
import de.cuioss.test.jsf.util.JsfEnvironmentConsumer;
import de.cuioss.test.jsf.util.JsfEnvironmentHolder;
import de.cuioss.test.juli.LogAsserts;
import de.cuioss.test.juli.TestLogLevel;
import de.cuioss.test.juli.junit5.EnableTestLogger;
import de.cuioss.test.valueobjects.junit5.contracts.ShouldBeNotNull;
import lombok.Getter;
import lombok.Setter;

@EnablePortalUiEnvironment
@EnableTestLogger(debug = RequestTracer.class)
@AddBeanClasses({ RequestTracer.class })
@EnablePortalConfiguration(configuration = PORTAL_LISTENER_TRACE_ENABLED + ":false")
class TraceListenerDisabledTest implements ShouldBeNotNull<TraceListener>, JsfEnvironmentConsumer {

    @Setter
    @Getter
    private JsfEnvironmentHolder environmentHolder;

    @Inject
    @Getter
    private TraceListener underTest;

    @Test
    void shouldHonorDisabled() throws InterruptedException {
        startSleepStop(PhaseId.RESTORE_VIEW);
        startSleepStop(PhaseId.APPLY_REQUEST_VALUES);
        startSleepStop(PhaseId.RENDER_RESPONSE);
        LogAsserts.assertNoLogMessagePresent(TestLogLevel.DEBUG, RequestTracer.class);
    }

    private void startSleepStop(PhaseId phaseId) throws InterruptedException {
        getUnderTest().beforePhase(phaseEvent(phaseId));
        TimeUnit.MILLISECONDS.sleep(50);
        getUnderTest().afterPhase(phaseEvent(phaseId));
    }

    private PhaseEvent phaseEvent(PhaseId id) {
        return new PhaseEvent(getFacesContext(), id, new MockLifecycle());
    }
}
