package de.cuioss.portal.ui.runtime.application.exception;

import java.util.Iterator;

import javax.faces.FacesException;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;

import de.cuioss.portal.core.cdi.PortalBeanManager;
import de.cuioss.portal.ui.api.exception.ExceptionAsEvent;
import de.cuioss.portal.ui.runtime.exception.PortalUiExceptionHandler;
import de.cuioss.tools.logging.CuiLogger;

/**
 * Bridges the JSf Exception based Exception-Handling to the Portal Ui Based ->
 * {@link de.cuioss.portal.ui.runtime.exception.PortalUiExceptionHandler}
 */
public class JSFPortalExceptionHandlerBridge extends ExceptionHandlerWrapper {

    private static final CuiLogger LOGGER = new CuiLogger(JSFPortalExceptionHandlerBridge.class);

    /**
     * Default constructor to delegate to {@link ExceptionHandlerWrapper}
     * 
     * @param delegate must not be null
     */
    public JSFPortalExceptionHandlerBridge(ExceptionHandler delegate) {
        super(delegate);
    }

    @Override
    public void handle() throws FacesException {
        PortalUiExceptionHandler uiExceptionHandler = PortalBeanManager
                .resolveBean(PortalUiExceptionHandler.class, null)
                .orElseThrow(() -> new IllegalStateException("Unable to access needed PortalUiExceptionHandler"));
        final Iterator<ExceptionQueuedEvent> queue = getUnhandledExceptionQueuedEvents().iterator();
        while (queue.hasNext()) {
            ExceptionQueuedEvent item = queue.next();
            ExceptionQueuedEventContext exceptionQueuedEventContext = (ExceptionQueuedEventContext) item.getSource();

            try {
                Throwable throwable = exceptionQueuedEventContext.getException();
                LOGGER.trace("Handling Throwable '%s'", throwable);

                ExceptionAsEvent exceptionEvent = new ExceptionAsEvent(throwable);
                // We should catch The IllegalStateException eventually to be thrown by the
                // FallBackExceptionHandler and queue it again
                uiExceptionHandler.handle(exceptionEvent);
            } finally {
                queue.remove();
            }
        }
        getWrapped().handle();
    }
}
