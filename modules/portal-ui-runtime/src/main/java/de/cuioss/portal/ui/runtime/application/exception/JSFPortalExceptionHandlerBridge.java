/*
 * Copyright © 2025 CUI-OpenSource-Software (info@cuioss.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.cuioss.portal.ui.runtime.application.exception;

import de.cuioss.portal.common.cdi.PortalBeanManager;
import de.cuioss.portal.ui.api.exception.ExceptionAsEvent;
import de.cuioss.portal.ui.runtime.exception.PortalUiExceptionHandler;
import de.cuioss.tools.logging.CuiLogger;
import jakarta.faces.FacesException;
import jakarta.faces.context.ExceptionHandler;
import jakarta.faces.context.ExceptionHandlerWrapper;
import jakarta.faces.event.ExceptionQueuedEventContext;

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
        var uiExceptionHandler = PortalBeanManager.resolveBean(PortalUiExceptionHandler.class, null)
                .orElseThrow(() -> new IllegalStateException("Unable to access needed PortalUiExceptionHandler"));
        final var queue = getUnhandledExceptionQueuedEvents().iterator();
        while (queue.hasNext()) {
            var item = queue.next();
            var exceptionQueuedEventContext = (ExceptionQueuedEventContext) item.getSource();

            try {
                var throwable = exceptionQueuedEventContext.getException();
                // cui-rewrite:disable CuiLoggerStandardsRecipe
                LOGGER.trace("Handling Throwable '%s'", throwable);

                var exceptionEvent = new ExceptionAsEvent(throwable);
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
