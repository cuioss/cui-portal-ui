package de.cuioss.portal.ui.runtime.exception;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import de.cuioss.portal.ui.api.exception.ExceptionAsEvent;
import de.cuioss.portal.ui.api.exception.HandleOutcome;
import de.cuioss.portal.ui.api.exception.PortalExceptionHandler;
import de.cuioss.portal.ui.runtime.application.exception.JSFPortalExceptionHandlerBridge;
import de.cuioss.tools.logging.CuiLogger;

/**
 * CDI side of exceptionHandler, see {@link JSFPortalExceptionHandlerBridge} for
 * the other side.
 * 
 * The handle method is defined for being directly called by said bridge and
 * being listener, {@link Observes}, for {@link ExceptionAsEvent}
 */
@RequestScoped
public class PortalUiExceptionHandler {

    private static final CuiLogger LOGGER = new CuiLogger(PortalUiExceptionHandler.class);

    @Inject
    private Instance<PortalExceptionHandler> handler;

    @Inject
    private FallBackExceptionHandler fallBackExceptionHandler;

    /**
     * 
     * @param exceptionEvent the exception that may be handled. If this Handler
     *                       handles a contained {@link Throwable} it will notify
     *                       using the given {@link ExceptionAsEvent} by calling
     *                       {@link ExceptionAsEvent#handled(HandleOutcome)}
     * @throws @throws IllegalStateException in cased of projectStage being
     *                 Development
     */
    public void handle(@Observes ExceptionAsEvent exceptionEvent) {
        if (exceptionEvent.isHandled()) {
            LOGGER.debug("Given event '%s' already handled, nothing to do here", exceptionEvent);
            return;
        }
        for (PortalExceptionHandler singleExceptionHandler : handler) {
            singleExceptionHandler.handle(exceptionEvent);
            if (exceptionEvent.isHandled()) {
                LOGGER.debug("Exception '%s' was handled by '%s'", exceptionEvent, singleExceptionHandler);
                return;
            }
        }
        LOGGER.debug("Given event '%s' could not be handled by concrete handler, calling FallBackExceptionHandler",
                exceptionEvent);
        fallBackExceptionHandler.handleFallBack(exceptionEvent);

    }
}