package de.cuioss.portal.ui.api.exception;

/**
 * Defines the possible outcome of the concrete {@link PortalExceptionHandler}
 */
public enum HandleOutcome {

    /**
     * Indicates, that the event was solely logged
     */
    LOGGED,

    /**
     * Indicates, that the exception was thrown again, usually in context of
     * development
     */
    RE_THROWN,

    /**
     * Indicates, that a user-message was created / added
     */
    USER_MESSAGE,

    /**
     * Indicates, that the handler did nothing. Default for {@link ExceptionAsEvent}
     */
    NO_OP,

    /**
     * The handler executed a redirect
     */
    REDIRECT;

}
