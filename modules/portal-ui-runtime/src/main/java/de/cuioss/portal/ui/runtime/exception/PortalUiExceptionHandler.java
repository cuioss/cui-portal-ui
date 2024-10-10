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
package de.cuioss.portal.ui.runtime.exception;

import de.cuioss.portal.ui.api.exception.ExceptionAsEvent;
import de.cuioss.portal.ui.api.exception.HandleOutcome;
import de.cuioss.portal.ui.api.exception.PortalExceptionHandler;
import de.cuioss.portal.ui.runtime.application.exception.JSFPortalExceptionHandlerBridge;
import de.cuioss.tools.logging.CuiLogger;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;

/**
 * CDI side of exceptionHandler, see {@link JSFPortalExceptionHandlerBridge} for
 * the other side.
 * <p>
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
     * @param exceptionEvent the exception that may be handled. If this Handler
     *                       handles a contained {@link Throwable} it will notify
     *                       using the given {@link ExceptionAsEvent} by calling
     *                       {@link ExceptionAsEvent#handled(HandleOutcome)}
     * @throws IllegalStateException in cased of projectStage being
     *                               Development
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
