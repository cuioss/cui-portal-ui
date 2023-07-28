package de.cuioss.portal.ui.api.exceptions;

import java.io.Serializable;

import de.cuioss.portal.core.storage.MapStorage;
import de.cuioss.portal.core.storage.SessionStorage;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * Simple Container for communicating and displaying exception information at
 * the ui.
 *
 * @author Oliver Wolff
 */
@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public class DefaultErrorMessage implements Serializable {

    /**
     * The lookup key for identifying error-messages within portal specific
     * {@link SessionStorage}
     */
    public static final String LOOKUP_KEY = "defaultErrorMessage";

    /** Serial version UID. */
    private static final long serialVersionUID = 5486339618618279597L;

    /** The error code. */
    @Getter
    private final String errorCode;

    /** The error code. */
    @Getter
    private final String errorTicket;

    /** The error code. */
    @Getter
    private final String errorMessage;

    /** The page that raised the error. */
    @Getter
    private final String pageId;

    @Getter
    private String stackTrace;

    /**
     * Store a given {@link DefaultErrorMessage} into the given {@link MapStorage}
     *
     * @param errorMessage must not be null
     * @param storage      m,sut not be null
     */
    public static final void addErrorMessageToSessionStorage(final DefaultErrorMessage errorMessage,
            final MapStorage<Serializable, Serializable> storage) {
        storage.put(LOOKUP_KEY, errorMessage);
    }

}
