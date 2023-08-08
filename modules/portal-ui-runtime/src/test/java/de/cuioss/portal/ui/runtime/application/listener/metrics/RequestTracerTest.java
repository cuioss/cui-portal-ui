package de.cuioss.portal.ui.runtime.application.listener.metrics;

import static de.cuioss.portal.configuration.MetricsConfigKeys.PORTAL_METRICS_ENABLED;
import static de.cuioss.portal.configuration.PortalConfigurationKeys.PORTAL_LISTENER_TRACE_ENABLED;
import static de.cuioss.portal.ui.runtime.application.listener.metrics.RequestTracer.PROCESSING_IDENTIFIER;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.concurrent.TimeUnit;

import javax.faces.event.PhaseId;
import javax.inject.Inject;

import org.jboss.weld.junit5.auto.AddBeanClasses;
import org.junit.jupiter.api.Test;

import de.cuioss.portal.core.test.junit5.EnablePortalConfiguration;
import de.cuioss.portal.core.test.mocks.microprofile.PortalTestMetricRegistry;
import de.cuioss.portal.ui.test.junit5.EnablePortalUiEnvironment;
import de.cuioss.test.juli.LogAsserts;
import de.cuioss.test.juli.TestLogLevel;
import de.cuioss.test.juli.junit5.EnableTestLogger;
import de.cuioss.test.valueobjects.junit5.contracts.ShouldBeNotNull;
import lombok.Getter;

@EnablePortalUiEnvironment
@EnableTestLogger(trace = RequestTracer.class)
@AddBeanClasses(PortalTestMetricRegistry.class)
@EnablePortalConfiguration(configuration = { PORTAL_METRICS_ENABLED + ":true",
        PORTAL_LISTENER_TRACE_ENABLED + ":true" })
class RequestTracerTest implements ShouldBeNotNull<RequestTracer> {

    @Inject
    @Getter
    private RequestTracer underTest;

    @Inject
    private PortalTestMetricRegistry metricsRegistry;

    @Test
    void shouldHandleHappyCase() throws InterruptedException {
        startSleepStop(PhaseId.RESTORE_VIEW);
        startSleepStop(PhaseId.APPLY_REQUEST_VALUES);
        startSleepStop(PhaseId.RENDER_RESPONSE);
        LogAsserts.assertNoLogMessagePresent(TestLogLevel.DEBUG, RequestTracer.class);
        underTest.writeStatistics();

        LogAsserts.assertSingleLogMessagePresentContaining(TestLogLevel.DEBUG, PROCESSING_IDENTIFIER);
        LogAsserts.assertSingleLogMessagePresentContaining(TestLogLevel.DEBUG, PhaseId.RESTORE_VIEW.getName());
        LogAsserts.assertSingleLogMessagePresentContaining(TestLogLevel.DEBUG, PhaseId.APPLY_REQUEST_VALUES.getName());
        LogAsserts.assertSingleLogMessagePresentContaining(TestLogLevel.DEBUG, PhaseId.RENDER_RESPONSE.getName());
    }

    @Test
    void shouldHandleInvalidCallOrder() throws InterruptedException {
        startSleepStop(PhaseId.RESTORE_VIEW);
        underTest.stop(PhaseId.APPLY_REQUEST_VALUES);
        startSleepStop(PhaseId.RENDER_RESPONSE);
        LogAsserts.assertNoLogMessagePresent(TestLogLevel.DEBUG, RequestTracer.class);
        underTest.writeStatistics();
        LogAsserts.assertSingleLogMessagePresentContaining(TestLogLevel.DEBUG, PROCESSING_IDENTIFIER);
        LogAsserts.assertSingleLogMessagePresentContaining(TestLogLevel.DEBUG, PhaseId.RESTORE_VIEW.getName());
        LogAsserts.assertNoLogMessagePresent(TestLogLevel.DEBUG, PhaseId.APPLY_REQUEST_VALUES.getName());
        LogAsserts.assertSingleLogMessagePresentContaining(TestLogLevel.DEBUG, PhaseId.RENDER_RESPONSE.getName());

    }

    @Test
    void shouldHandleInvalidCalls() {
        LogAsserts.assertNoLogMessagePresent(TestLogLevel.DEBUG, RequestTracer.class);
        underTest.writeStatistics();
        LogAsserts.assertSingleLogMessagePresentContaining(TestLogLevel.DEBUG,
                RequestTracer.NO_TRACING_INFORMATION_AVAILABLE);
    }

    @Test
    void shouldOnlyWriteOnDebugEnabled() throws InterruptedException {
        TestLogLevel.INFO.addLogger(RequestTracer.class);
        startSleepStop(PhaseId.RESTORE_VIEW);
        startSleepStop(PhaseId.APPLY_REQUEST_VALUES);
        startSleepStop(PhaseId.RENDER_RESPONSE);
        underTest.writeStatistics();
        LogAsserts.assertNoLogMessagePresent(TestLogLevel.DEBUG, RequestTracer.class);
    }

    @Test
    void shouldIgnoreAnyPhase() throws InterruptedException {
        startSleepStop(PhaseId.ANY_PHASE);
        underTest.writeStatistics();
        LogAsserts.assertSingleLogMessagePresentContaining(TestLogLevel.DEBUG,
                RequestTracer.NO_TRACING_INFORMATION_AVAILABLE);
    }

    @Test
    void registersMetrics() throws InterruptedException {
        startSleepStop(PhaseId.RESTORE_VIEW);
        startSleepStop(PhaseId.APPLY_REQUEST_VALUES);
        startSleepStop(PhaseId.RENDER_RESPONSE);
        final var timer = metricsRegistry.getTimers();
        assertEquals(4, timer.size());
    }

    private void startSleepStop(PhaseId phaseId) throws InterruptedException {
        underTest.start(phaseId);
        TimeUnit.MILLISECONDS.sleep(50);
        underTest.stop(phaseId);
    }
}
