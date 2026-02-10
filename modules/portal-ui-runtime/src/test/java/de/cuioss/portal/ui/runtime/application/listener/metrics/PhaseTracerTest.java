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
package de.cuioss.portal.ui.runtime.application.listener.metrics;

import de.cuioss.test.juli.LogAsserts;
import de.cuioss.test.juli.TestLogLevel;
import de.cuioss.test.juli.junit5.EnableTestLogger;
import de.cuioss.test.valueobjects.ValueObjectTest;
import de.cuioss.test.valueobjects.api.contracts.VerifyConstructor;
import de.cuioss.test.valueobjects.api.property.PropertyConfig;
import de.cuioss.test.valueobjects.api.property.PropertyReflectionConfig;
import de.cuioss.tools.property.PropertyReadWrite;
import jakarta.faces.event.PhaseId;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static de.cuioss.tools.collect.CollectionLiterals.mutableList;
import static org.junit.jupiter.api.Assertions.*;

@PropertyConfig(name = "phaseId", propertyClass = PhaseId.class, propertyReadWrite = PropertyReadWrite.WRITE_ONLY)
@VerifyConstructor(of = {"phaseId"}, allRequired = true)
@PropertyReflectionConfig(skip = true)
@EnableTestLogger
class PhaseTracerTest extends ValueObjectTest<PhaseTracer> {

    @Test
    void shouldHandleDidRun() {
        var underTest = new PhaseTracer(PhaseId.APPLY_REQUEST_VALUES);
        assertFalse(underTest.isDidRun());
        assertTrue(underTest.toString().contains("did not run"));
        underTest.start();
        assertTrue(underTest.isDidRun());
        underTest.stop();
        assertTrue(underTest.isDidRun());
        assertFalse(underTest.toString().contains("did not run"));
    }

    @Test
    void shouldGracefullyHandleInvalidCallOrder() {
        var underTest = new PhaseTracer(PhaseId.PROCESS_VALIDATIONS);
        assertNotNull(underTest.toString());
        assertFalse(underTest.isDidRun());
        underTest.stop();
        assertFalse(underTest.isDidRun());
        underTest.stop();
        assertFalse(underTest.isDidRun());
        underTest.start();
        assertTrue(underTest.isDidRun());
        underTest.start();
        assertTrue(underTest.isDidRun());
        assertNotNull(underTest.toString());
        // stop() called without prior start() triggers PHASE_ALREADY_STOPPED
        LogAsserts.assertLogMessagePresentContaining(TestLogLevel.WARN, "PORTAL-UI-RT-119");
        // start() called while already running triggers PHASE_ALREADY_STARTED
        LogAsserts.assertLogMessagePresentContaining(TestLogLevel.WARN, "PORTAL-UI-RT-118");
    }

    @Test
    @SuppressWarnings("java:S2925")
    void shouldAugmentToTimerList() throws Exception {
        var phase = PhaseId.PROCESS_VALIDATIONS;
        var outputPrefix = phase.getOrdinal() + "-" + phase.getName() + ": ";
        var underTest = new PhaseTracer(phase);
        List<String> result = mutableList();
        underTest.addTimeToList(result);
        assertEquals(1, result.size());
        assertEquals(outputPrefix + "-1", result.getFirst());

        result.clear();
        underTest.start();
        TimeUnit.MILLISECONDS.sleep(10);
        underTest.stop();
        underTest.addTimeToList(result);
        assertNotEquals(outputPrefix + "-1", result.getFirst());
    }

    @Test
    void shouldComparePhases() {
        var apply = new PhaseTracer(PhaseId.APPLY_REQUEST_VALUES);
        var validation = new PhaseTracer(PhaseId.PROCESS_VALIDATIONS);
        var response = new PhaseTracer(PhaseId.RENDER_RESPONSE);

        List<PhaseTracer> list = mutableList(apply, validation, response);
        Collections.shuffle(list);
        Collections.sort(list);
        assertEquals(apply, list.getFirst());
        assertEquals(validation, list.get(1));
        assertEquals(response, list.get(2));
    }
}
