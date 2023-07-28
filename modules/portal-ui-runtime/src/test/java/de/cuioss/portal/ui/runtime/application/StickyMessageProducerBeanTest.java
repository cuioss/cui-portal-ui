package de.cuioss.portal.ui.runtime.application;

import static de.cuioss.jsf.api.components.css.ContextState.DANGER;
import static de.cuioss.test.generator.Generators.enumValues;
import static de.cuioss.test.generator.Generators.letterStrings;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.inject.Inject;

import org.jboss.weld.junit5.auto.ExcludeBeanClasses;
import org.junit.jupiter.api.Test;

import de.cuioss.jsf.api.components.css.ContextState;
import de.cuioss.jsf.api.components.events.ModelPayloadEvent;
import de.cuioss.jsf.api.components.support.DummyComponent;
import de.cuioss.portal.ui.api.message.PortalStickyMessageProducer;
import de.cuioss.portal.ui.api.message.StickyMessage;
import de.cuioss.portal.ui.test.junit5.EnablePortalUiEnvironment;
import de.cuioss.portal.ui.test.mocks.PortalStickyMessageProducerMock;
import de.cuioss.test.generator.TypedGenerator;
import de.cuioss.test.valueobjects.junit5.contracts.ShouldHandleObjectContracts;
import de.cuioss.uimodel.nameprovider.DisplayName;
import lombok.Getter;

@EnablePortalUiEnvironment
@ExcludeBeanClasses(PortalStickyMessageProducerMock.class)
class StickyMessageProducerBeanTest implements ShouldHandleObjectContracts<StickyMessageProducerBean> {

    private static final TypedGenerator<ContextState> CONTEXT_STATE_GENERATOR = enumValues(ContextState.class);

    @PortalStickyMessageProducer
    @Inject
    @Getter
    private StickyMessageProducerBean underTest;

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
    void shouldProvidePosibilityToRemoveMessageByUi() {
        final var addedMessage = addAnyMessageToStorage();
        assertTrue(underTest.getMessages().contains(addedMessage));

        underTest.dismissListener(asEvent(addedMessage));
        assertThatNoMessagesStored();
    }

    @Test
    void shouldSupportToRemoveMessages() {
        final var messageKey = letterStrings(10, 20).next();
        final var parameter = letterStrings(10, 20).next();
        final var expectedInfoMessage = new StickyMessage(true, ContextState.INFO, new DisplayName(messageKey));
        final var expectedWarningMessage = new StickyMessage(true, ContextState.WARNING, new DisplayName(messageKey));
        assertThatNoMessagesStored();

        underTest.setInfoMessage(messageKey, parameter);
        underTest.setWarningMessage(messageKey, parameter);

        assertTrue(underTest.getMessages().contains(expectedInfoMessage));
        assertTrue(underTest.getMessages().contains(expectedWarningMessage));

        underTest.clearStoredMessages();
        assertThatNoMessagesStored();
    }

    private StickyMessage addAnyMessageToStorage() {
        final var stickyMessage = new StickyMessage(true, CONTEXT_STATE_GENERATOR.next(),
                new DisplayName(letterStrings(10, 20).next()));
        underTest.addMessage(stickyMessage);
        return stickyMessage;
    }

    /*
     * Helper Methods
     */

    private static ModelPayloadEvent asEvent(final StickyMessage msg) {
        return new ModelPayloadEvent(new DummyComponent(), msg);
    }

    private void assertThatNoMessagesStored() {
        assertTrue(underTest.getMessages().isEmpty(), "No stored messages expected");
    }

}
