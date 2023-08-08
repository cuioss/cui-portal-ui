package de.cuioss.portal.ui.runtime.application.exception;

import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerFactory;

import de.cuioss.tools.logging.CuiLogger;

/**
 * Factory for creating instances of {@link JSFPortalExceptionHandlerBridge}
 */
public class JSFPortalExceptionHandlerBridgeFactory extends ExceptionHandlerFactory {

    private static final CuiLogger LOGGER = new CuiLogger(JSFPortalExceptionHandlerBridgeFactory.class);

    public JSFPortalExceptionHandlerBridgeFactory(ExceptionHandlerFactory parent) {
        super(parent);
        LOGGER.debug("Creating JSFPortalExceptionHandlerBridgeFactory with parent = '%s'", parent);
    }

    @Override
    public ExceptionHandler getExceptionHandler() {
        return new JSFPortalExceptionHandlerBridge(super.getWrapped().getExceptionHandler());
    }

}
