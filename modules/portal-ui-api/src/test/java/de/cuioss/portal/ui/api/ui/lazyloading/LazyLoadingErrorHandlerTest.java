package de.cuioss.portal.ui.api.ui.lazyloading;

import static de.cuioss.test.generator.Generators.letterStrings;

import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

import de.cuioss.jsf.api.components.model.resultContent.ErrorController;
import de.cuioss.test.generator.Generators;
import de.cuioss.test.juli.LogAsserts;
import de.cuioss.test.juli.TestLogLevel;
import de.cuioss.test.juli.junit5.EnableTestLogger;
import de.cuioss.test.valueobjects.junit5.contracts.ShouldBeNotNull;
import de.cuioss.tools.logging.CuiLogger;

@EnableTestLogger
class LazyLoadingErrorHandlerTest implements ShouldBeNotNull<LazyLoadingErrorHandler> {

    private static final CuiLogger LOGGER = new CuiLogger(LazyLoadingErrorHandlerTest.class);

    @Override
    public LazyLoadingErrorHandler getUnderTest() {
        return new LazyLoadingErrorHandler();
    }

    @Test
    void shouldHandleWithCause() {
        var underTest = getUnderTest();
        var message = letterStrings().next();
        ErrorController controllerMock = EasyMock.createNiceMock(ErrorController.class);
        underTest.handleRequestError(Generators.throwables().next(), message, controllerMock, LOGGER);
        LogAsserts.assertSingleLogMessagePresentContaining(TestLogLevel.WARN, message);
    }

    @Test
    void shouldHandleWithoutCause() {
        var underTest = getUnderTest();
        var message = letterStrings().next();
        ErrorController controllerMock = EasyMock.createNiceMock(ErrorController.class);
        underTest.handleRequestError(null, message, controllerMock, LOGGER);
        LogAsserts.assertSingleLogMessagePresentContaining(TestLogLevel.WARN, message);
    }

}
