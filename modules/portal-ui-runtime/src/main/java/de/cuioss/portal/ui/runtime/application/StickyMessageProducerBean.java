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
package de.cuioss.portal.ui.runtime.application;

import de.cuioss.jsf.api.components.css.ContextState;
import de.cuioss.jsf.api.components.events.ModelPayloadEvent;
import de.cuioss.portal.common.bundle.ResourceBundleWrapper;
import de.cuioss.portal.ui.api.message.StickyMessage;
import de.cuioss.portal.ui.api.message.StickyMessageProducer;
import de.cuioss.uimodel.nameprovider.DisplayName;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static de.cuioss.portal.ui.api.PortalCoreBeanNames.STICKY_MESSAGE_BEAN_NAME;
import static de.cuioss.tools.collect.CollectionLiterals.mutableList;
import static java.text.MessageFormat.format;
import static java.util.Objects.requireNonNull;

/**
 * @author Matthias Walliczek
 */
@Named(STICKY_MESSAGE_BEAN_NAME)
@SessionScoped
@EqualsAndHashCode(of = "messageSet", doNotUseGetters = true)
@ToString(of = "messageSet", doNotUseGetters = true)
public class StickyMessageProducerBean implements Serializable, StickyMessageProducer {

    @Serial
    private static final long serialVersionUID = -7985606463263784288L;

    private final Set<StickyMessage> messageSet = new HashSet<>();

    private final ResourceBundleWrapper resourceBundle;

    /**
     * CDI proxy constructor.
     */
    protected StickyMessageProducerBean() {
        this.resourceBundle = null;
    }

    @Inject
    public StickyMessageProducerBean(ResourceBundleWrapper resourceBundle) {
        this.resourceBundle = resourceBundle;
    }

    /**
     * @return the sticky messages as list.
     */
    @Override
    public List<StickyMessage> getMessages() {
        return mutableList(messageSet);
    }

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
        setMessageAsString(resourceBundle.getString(messageKey), severity, parameter);
    }

    @Override
    public void setMessageAsString(final String messageString, final ContextState severity, final Object... parameter) {
        if (mutableList(parameter).isEmpty()) {
            addMessage(new StickyMessage(true, severity, new DisplayName(messageString)));
        } else {
            addMessage(new StickyMessage(true, severity, new DisplayName(format(messageString, parameter))));
        }
    }

    @Override
    public void addMessage(final StickyMessage message) {
        messageSet.add(requireNonNull(message, "StickyMessage must not be null"));
    }

    @Override
    public void removeMessage(final StickyMessage message) {
        messageSet.remove(message);
    }

    /**
     * To be called when the user dismisses the notification box.
     *
     * @param dismissEvent to identify the message to remove
     */
    public void dismissListener(final ModelPayloadEvent dismissEvent) {
        removeMessage((StickyMessage) dismissEvent.getModel());
    }

    /**
     * @return {@code true} if at least one message is available, {@code false}
     * otherwise
     */
    @Override
    public boolean isAnyMessageAvailable() {
        return !messageSet.isEmpty();
    }
}
