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
package de.cuioss.portal.ui.test.mocks;

import static de.cuioss.portal.ui.api.PortalCoreBeanNames.STICKY_MESSAGE_BEAN_NAME;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import de.cuioss.jsf.api.components.css.ContextState;
import de.cuioss.jsf.api.components.events.ModelPayloadEvent;
import de.cuioss.portal.ui.api.message.PortalStickyMessageProducer;
import de.cuioss.portal.ui.api.message.StickyMessage;
import de.cuioss.portal.ui.api.message.StickyMessageProducer;
import de.cuioss.uimodel.nameprovider.DisplayName;
import de.cuioss.uimodel.nameprovider.IDisplayNameProvider;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Matthias Walliczek
 */
@Named(STICKY_MESSAGE_BEAN_NAME)
@ApplicationScoped
@EqualsAndHashCode
@PortalStickyMessageProducer
@ToString
public class PortalStickyMessageProducerMock implements Serializable, StickyMessageProducer {

    private static final long serialVersionUID = -7985606463263784288L;

    @Getter
    @Setter
    private List<StickyMessage> messages = new ArrayList<>();

    @Getter
    @Setter
    private String messageAsString;

    @Override
    public void setInfoMessage(final String messageKey, final Object... parameter) {
        setMessage(messageKey, ContextState.INFO, parameter);
    }

    @Override
    public void setErrorMessage(final String messageKey, final Object... parameter) {
        setMessage(messageKey, ContextState.DANGER, parameter);
    }

    @Override
    public void setWarningMessage(final String messageKey, final Object... parameter) {
        setMessage(messageKey, ContextState.WARNING, parameter);
    }

    @Override
    public void setMessage(final String messageKey, final ContextState severity, final Object... parameter) {
        messages.add(new StickyMessage(true, severity, new DisplayName(messageKey)));
    }

    /**
     * To be called when the user dismisses the notification box.
     *
     * @param dismissEvent to identify the message to remove
     */
    public void dismissListener(final ModelPayloadEvent dismissEvent) {
        messages.remove(dismissEvent.getModel());
    }

    @Override
    public void setMessageAsString(final String messageString, final ContextState severity, final Object... parameter) {
        messageAsString = messageString;
    }

    @Override
    public void addMessage(final StickyMessage message) {
        messages.add(message);
    }

    @Override
    public void removeMessage(final StickyMessage message) {
        messages.remove(message);
    }

    /**
     * @param messageKey key that should be present in the added
     *                   {@linkplain StickyMessage#getMessage()}s
     *                   {@linkplain IDisplayNameProvider#getContent()} string.
     */
    public void assertThatKeyIsPresent(final String messageKey) {
        assertTrue(messages.stream().anyMatch(sticky -> sticky.getMessage().getContent().equals(messageKey)));
    }
}
