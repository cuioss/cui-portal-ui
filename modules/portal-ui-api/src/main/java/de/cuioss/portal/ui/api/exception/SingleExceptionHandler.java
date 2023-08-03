package de.cuioss.portal.ui.api.exception;

/**
 * A Successor of deltaspikes ExcpetionHandler. Not that elegant but works
 * anyway. In essence it defines a handler for specific / single exception;
 */
public interface SingleExceptionHandler {

    /**
     * @param exceptionEvent the exception that may be handled. If this Handler
     *                       handles a contained {@link Throwable} it will notify
     *                       using the given {@link ExceptionAsEvent} by calling
     *                       {@link ExceptionAsEvent#handled(HandleOutcome)}
     */
    void handle(ExceptionAsEvent exceptionEvent);

}
