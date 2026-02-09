/*
 * Copyright 2023 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * https://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.cuioss.portal.ui.api.lazyloading;

import de.cuioss.jsf.api.components.model.result_content.ErrorController;
import de.cuioss.test.generator.Generators;
import de.cuioss.test.juli.LogAsserts;
import de.cuioss.test.juli.TestLogLevel;
import de.cuioss.test.juli.junit5.EnableTestLogger;
import de.cuioss.test.valueobjects.junit5.contracts.ShouldBeNotNull;
import de.cuioss.tools.logging.CuiLogger;
import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

import static de.cuioss.test.generator.Generators.letterStrings;

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
