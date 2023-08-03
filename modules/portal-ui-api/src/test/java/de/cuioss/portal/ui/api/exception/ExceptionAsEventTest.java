package de.cuioss.portal.ui.api.exception;

import static de.cuioss.test.generator.Generators.throwables;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import de.cuioss.test.valueobjects.junit5.contracts.ShouldHandleObjectContracts;

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
