package de.cuioss.portal.ui.runtime.application.exception;

import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerFactory;

import de.cuioss.tools.logging.CuiLogger;

public class MockExceptionHandlerFactory extends ExceptionHandlerFactory {

    private static final CuiLogger LOGGER = new CuiLogger(JSFPortalExceptionHandlerBridgeFactory.class);

    public MockExceptionHandlerFactory(ExceptionHandlerFactory parent) {
        super(parent);
        LOGGER.debug("Creating MockExceptionHandlerFactory with parent = '%s'", parent);
    }

    @Override
    public ExceptionHandler getExceptionHandler() {
        return new MockExceptionHandler(null);
    }

}
