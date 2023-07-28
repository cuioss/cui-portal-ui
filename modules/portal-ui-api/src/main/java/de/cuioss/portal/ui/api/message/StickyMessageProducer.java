package de.cuioss.portal.ui.api.message;

import java.util.List;

import de.cuioss.jsf.api.components.css.ContextState;

/**
 * Produces sticky messages to be displayed until the user dismisses them.
 *
 * @author Matthias Walliczek
 */
public interface StickyMessageProducer {

    /**
     * Convenience Method for setting sticky info messages
     *
     * @param messageKey must no be null
     * @param parameter  Ellipses of Object Parameter for MessageFormat
     */
    void setInfoMessage(String messageKey, Object... parameter);

    /**
     * Convenience Method for setting sticky error messages
     *
     * @param messageKey must no be null
     * @param parameter  Ellipses of Object Parameter for MessageFormat
     */
    void setErrorMessage(String messageKey, Object... parameter);

    /**
     * Convenience Method for setting sticky warning messages
     *
     * @param messageKey must no be null
     * @param parameter  Ellipses of Object Parameter for MessageFormat
     */
    void setWarningMessage(String messageKey, Object... parameter);

    /**
     * Stores and displays a sticky message with given severity and messageKey.
     *
     * @param messageKey must no be null
     * @param severity   The Severity level of the Message, must not be null.
     * @param parameter  Ellipses of Object Parameter for MessageFormat
     */
    void setMessage(String messageKey, ContextState severity, Object... parameter);

    /**
     * Stores and displays a sticky message with given severity and messageString.
     *
     * @param messageString must no be null
     * @param severity      The Severity level of the Message, must not be null.
     * @param parameter     Ellipses of Object Parameter for MessageFormat
     */
    void setMessageAsString(final String messageString, final ContextState severity, final Object... parameter);

    /**
     * Add complete StickyMessage to list
     *
     * @param message {@linkplain StickyMessage} must not be {@code null}
     */
    void addMessage(final StickyMessage message);

    /**
     * Remove StickyMessage from list if still available in list
     *
     * @param message {@linkplain StickyMessage} must not be {@code null}
     */
    void removeMessage(final StickyMessage message);

    /**
     * @return the contained messages
     */
    List<StickyMessage> getMessages();

    /**
     * @return {@code true} if at least one message is available, {@code false}
     *         otherwise
     */
    default boolean isAnyMessageAvailable() {
        return !getMessages().isEmpty();
    }

    /**
     * Remove all stored StickyMessages
     *
     * @Deprecated because it will delete all messages without filtering. Please
     *             rethink usage of the {@link StickyMessageProducer} and consider
     *             using {@link StickyMessageProvider}
     */
    @Deprecated
    void clearStoredMessages();
}
