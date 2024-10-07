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

import de.cuioss.jsf.api.components.css.ContextState;
import de.cuioss.jsf.api.components.model.resultContent.ErrorController;
import de.cuioss.jsf.api.components.model.resultContent.ResultErrorHandler;
import de.cuioss.tools.logging.CuiLogger;
import de.cuioss.uimodel.nameprovider.LabeledKey;
import de.cuioss.uimodel.result.ResultDetail;
import de.cuioss.uimodel.result.ResultState;

import java.util.logging.Logger;

/**
 * A default implementation of an error handler for a {@link LazyLoadingRequest}.
 * Writes a log message if the state != {@link ResultState#VALID} and sets the
 * {@link ContextState} for the notification box. If a
 * {@link ResultDetail#getDetail()} is set it will be displayed inside a
 * notification box.
 */
public class LazyLoadingErrorHandler extends ResultErrorHandler {

    private static final LabeledKey requestErrorKey = new LabeledKey("message.error.request");

    /**
     * Handles an error during request execution, e.g. timeout.
     *
     * @param cause           a cause if present
     * @param message         the message to log.
     * @param errorController an {@link ErrorController} to allow setting a
     *                        notification box or a GlobalFacesMessage.
     * @param log             a {@link Logger} to log the cause.
     */
    public void handleRequestError(Throwable cause, String message, ErrorController errorController, CuiLogger log) {
        if (null != cause) {
            log.warn(message, cause);
        } else {
            log.warn(message);
        }
        errorController.addNotificationBox(requestErrorKey, ContextState.DANGER);
        errorController.setRenderContent(false);
    }
}
