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
package de.cuioss.portal.ui.api.exception;

import de.cuioss.test.valueobjects.junit5.contracts.ShouldHandleObjectContracts;
import org.junit.jupiter.api.Test;

import static de.cuioss.test.generator.Generators.throwables;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ExceptionAsEventTest implements ShouldHandleObjectContracts<ExceptionAsEvent> {

    @Override
    public ExceptionAsEvent getUnderTest() {
        return new ExceptionAsEvent(throwables().next());
    }

    @Test
    void shouldImplementHandleContract() {
        assertFalse(getUnderTest().isHandled());
        for (HandleOutcome outcome : HandleOutcome.values()) {
            if (HandleOutcome.NO_OP == outcome) {
                assertFalse(getUnderTest().handled(outcome).isHandled());
            } else {
                assertTrue(getUnderTest().handled(outcome).isHandled());
            }
        }
    }
}
