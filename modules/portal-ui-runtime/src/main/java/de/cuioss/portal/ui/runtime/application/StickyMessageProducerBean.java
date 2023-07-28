package de.cuioss.portal.ui.runtime.application;

import static com.google.common.collect.Lists.newArrayList;
import static de.cuioss.portal.ui.api.PortalCoreBeanNames.STICKY_MESSAGE_BEAN_NAME;
import static de.cuioss.tools.collect.CollectionLiterals.mutableList;
import static java.text.MessageFormat.format;
import static java.util.Objects.requireNonNull;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import de.cuioss.jsf.api.components.css.ContextState;
import de.cuioss.jsf.api.components.events.ModelPayloadEvent;
import de.cuioss.portal.core.bundle.PortalResourceBundle;
import de.cuioss.portal.ui.api.message.PortalStickyMessageProducer;
import de.cuioss.portal.ui.api.message.StickyMessage;
import de.cuioss.portal.ui.api.message.StickyMessageProducer;
import de.cuioss.uimodel.nameprovider.DisplayName;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author Matthias Walliczek
 */
@Named(STICKY_MESSAGE_BEAN_NAME)
@SessionScoped
@PortalStickyMessageProducer
@EqualsAndHashCode(of = "messageSet", doNotUseGetters = true)
@ToString(of = "messageSet", doNotUseGetters = true)
public class StickyMessageProducerBean implements Serializable, StickyMessageProducer {

    private static final long serialVersionUID = -7985606463263784288L;

    private final Set<StickyMessage> messageSet = new HashSet<>();

    @Inject
    @PortalResourceBundle
    private ResourceBundle resourceBundle;

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
        if (newArrayList(parameter).isEmpty()) {
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

    @Override
    public void clearStoredMessages() {
        messageSet.clear();
    }

    /**
     * @return {@code true} if at least one message is available, {@code false}
     *         otherwise
     */
    @Override
    public boolean isAnyMessageAvailable() {
        return !messageSet.isEmpty();
    }
}
