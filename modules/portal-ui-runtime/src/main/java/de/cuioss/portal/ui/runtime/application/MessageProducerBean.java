package de.cuioss.portal.ui.runtime.application;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.annotation.Priority;
import javax.enterprise.context.Dependent;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;

import de.cuioss.jsf.api.application.message.MessageProducer;
import de.cuioss.jsf.api.application.message.MessageProducerImpl;
import de.cuioss.portal.common.bundle.UnifiedResourceBundle;
import de.cuioss.portal.common.priority.PortalPriorities;
import de.cuioss.portal.ui.api.message.PortalMessageProducer;
import de.cuioss.tools.collect.MoreCollections;
import de.cuioss.tools.string.TextSplitter;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;

/**
 * Portal version of MessageProducer.
 * <h3>Implementation Note</h3>
 * <p>
 * All message-strings will be post-processed using {@link TextSplitter} prior
 * to displaying. This will smoothen possible ui glitches on the growl display
 * element, but may be problematic regarding unit-tests that will check the
 * result using {@link String#equals(Object)}. Especially the insertion of
 * invisible spaces: '\u200B' on the elements ".,;+-!?_" may be problematic. See
 * the unit-test of this class
 * </p>
 *
 * @author Oliver Wolff
 */
@Named(MessageProducerImpl.BEAN_NAME)
@Dependent
@PortalMessageProducer
@Priority(PortalPriorities.PORTAL_CORE_LEVEL)
@EqualsAndHashCode(doNotUseGetters = true)
@ToString
public class MessageProducerBean implements MessageProducer {

    private static final int ABBRIDGE_SIZE = 256;

    private static final int FORCE_BREAK_COUNT = 35;

    /**
     * Used for gracefully react on not existing message-keys.
     */
    public static final String MISSING_KEY_PREFIX = "Missing key : ";

    private static final long serialVersionUID = 4405826619024002836L;

    @Inject
    @UnifiedResourceBundle
    private ResourceBundle resourceBundle;

    @Inject
    private Provider<FacesContext> facesContextProvider;

    @Override
    public FacesMessage getMessageFor(final String messageKey, final Severity severity, final Object... parameter) {
        try {
            var resultingMessage = resourceBundle.getString(messageKey);
            if (parameter.length > 0) {
                resultingMessage = MessageFormat.format(resultingMessage, parameter);
            }
            var splitter = new TextSplitter(resultingMessage, FORCE_BREAK_COUNT, ABBRIDGE_SIZE);
            var cleaned = splitter.getTextWithEnforcedLineBreaks();
            return new FacesMessage(severity, cleaned, cleaned);
        } catch (final MissingResourceException e) {
            final var missingKey = MISSING_KEY_PREFIX + messageKey;
            return new FacesMessage(severity, missingKey, missingKey);
        }
    }

    @Override
    public FacesMessage getErrorMessageFor(final String messageKey, final Object... parameter) {
        return getMessageFor(messageKey, FacesMessage.SEVERITY_ERROR, parameter);
    }

    @Override
    public FacesMessage getInfoMessageFor(final String messageKey, final Object... parameter) {
        return getMessageFor(messageKey, FacesMessage.SEVERITY_INFO, parameter);
    }

    @Override
    public void setFacesMessage(final String messagekey, final Severity severity, final String componentId,
            final Object... parameter) {
        facesContextProvider.get().addMessage(componentId, getMessageFor(messagekey, severity, parameter));
    }

    @Override
    public void addMessage(String message, Severity severity, String componentId, Object... parameter) {
        var resultingMessage = message;
        if (!MoreCollections.isEmpty(parameter)) {
            resultingMessage = MessageFormat.format(resultingMessage, parameter);
        }
        var splitter = new TextSplitter(resultingMessage, FORCE_BREAK_COUNT, ABBRIDGE_SIZE);
        var cleaned = splitter.getTextWithEnforcedLineBreaks();
        facesContextProvider.get().addMessage(componentId, new FacesMessage(severity, cleaned, cleaned));
    }

    @Override
    public void setGlobalMessage(final String messagekey, final Severity severity, final Object... parameter) {
        setFacesMessage(messagekey, severity, null, parameter);
    }

    @Override
    public void addGlobalMessage(@NonNull final String message, @NonNull final Severity severity,
            final Object... parameter) {
        addMessage(message, severity, null, parameter);
    }

    @Override
    public void setGlobalInfoMessage(final String messagekey, final Object... parameter) {
        setGlobalMessage(messagekey, FacesMessage.SEVERITY_INFO, parameter);
    }

    @Override
    public void setGlobalErrorMessage(final String messagekey, final Object... parameter) {
        setGlobalMessage(messagekey, FacesMessage.SEVERITY_ERROR, parameter);
    }

    @Override
    public void setGlobalWarningMessage(final String messagekey, final Object... parameter) {
        setGlobalMessage(messagekey, FacesMessage.SEVERITY_WARN, parameter);
    }
}
