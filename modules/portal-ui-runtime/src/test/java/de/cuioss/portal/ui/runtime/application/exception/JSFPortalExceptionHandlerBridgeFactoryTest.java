package de.cuioss.portal.ui.runtime.application.exception;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import javax.faces.context.ExceptionHandlerWrapper;

import org.junit.jupiter.api.Test;

import de.cuioss.test.jsf.junit5.EnableJsfEnvironment;

@EnableJsfEnvironment
class JSFPortalExceptionHandlerBridgeFactoryTest {

    @Test
    void shouldCreateFactory() {
        var parent = new MockExceptionHandlerFactory(null);
        var underTest = new JSFPortalExceptionHandlerBridgeFactory(parent);
        assertInstanceOf(JSFPortalExceptionHandlerBridge.class, underTest.getExceptionHandler());
        assertInstanceOf(MockExceptionHandler.class,
                ((ExceptionHandlerWrapper) underTest.getExceptionHandler()).getWrapped());
    }

}
