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
package de.cuioss.portal.ui.runtime.application;

import de.cuioss.jsf.api.components.css.ContextState;
import de.cuioss.jsf.api.components.events.ModelPayloadEvent;
import de.cuioss.jsf.api.components.support.DummyComponent;
import de.cuioss.portal.ui.api.message.StickyMessage;
import de.cuioss.portal.ui.test.junit5.EnablePortalUiEnvironment;
import de.cuioss.portal.ui.test.mocks.PortalStickyMessageProducerMock;
import de.cuioss.test.generator.TypedGenerator;
import de.cuioss.test.valueobjects.junit5.contracts.ShouldHandleObjectContracts;
import de.cuioss.uimodel.nameprovider.DisplayName;
import jakarta.inject.Inject;
import lombok.Getter;
import org.jboss.weld.junit5.auto.ExcludeBeanClasses;
import org.junit.jupiter.api.Test;

import static de.cuioss.jsf.api.components.css.ContextState.DANGER;
import static de.cuioss.test.generator.Generators.enumValues;
import static de.cuioss.test.generator.Generators.letterStrings;
import static org.junit.jupiter.api.Assertions.assertTrue;

@EnablePortalUiEnvironment
@ExcludeBeanClasses(PortalStickyMessageProducerMock.class)
class StickyMessageProducerBeanTest implements ShouldHandleObjectContracts<StickyMessageProducerBean> {

    private static final TypedGenerator<ContextState> CONTEXT_STATE_GENERATOR = enumValues(ContextState.class);

    @Inject
    @Getter
    private StickyMessageProducerBean underTest;

    private static ModelPayloadEvent asEvent(final StickyMessage msg) {
        return new ModelPayloadEvent(new DummyComponent(), msg);
    }

    @Test
    void testErrorMessage() {
        final var msgKey = "system.general.error";
        final var expectedMessage = new StickyMessage(true, DANGER, new DisplayName(msgKey));
        assertThatNoMessagesStored();

        underTest.setErrorMessage(msgKey);
        assertTrue(underTest.getMessages().contains(expectedMessage));

        underTest.dismissListener(asEvent(underTest.getMessages().get(0)));
        assertThatNoMessagesStored();
    }

    @Test
    void shouldProvidePossibilityToRemoveMessageByUi() {
        final var addedMessage = addAnyMessageToStorage();
        assertTrue(underTest.getMessages().contains(addedMessage));

        underTest.dismissListener(asEvent(addedMessage));
        assertThatNoMessagesStored();
    }

    /*
     * Helper Methods
     */

    private StickyMessage addAnyMessageToStorage() {
        final var stickyMessage = new StickyMessage(true, CONTEXT_STATE_GENERATOR.next(),
                new DisplayName(letterStrings(10, 20).next()));
        underTest.addMessage(stickyMessage);
        return stickyMessage;
    }

    private void assertThatNoMessagesStored() {
        assertTrue(underTest.getMessages().isEmpty(), "No stored messages expected");
    }

}
