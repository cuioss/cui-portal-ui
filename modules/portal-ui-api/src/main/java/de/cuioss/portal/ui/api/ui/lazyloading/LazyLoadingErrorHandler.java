package de.cuioss.portal.ui.api.ui.lazyloading;

import java.util.logging.Logger;

import de.cuioss.jsf.api.components.css.ContextState;
import de.cuioss.jsf.api.components.model.resultContent.ErrorController;
import de.cuioss.jsf.api.components.model.resultContent.ResultErrorHandler;
import de.cuioss.tools.logging.CuiLogger;
import de.cuioss.uimodel.nameprovider.LabeledKey;
import de.cuioss.uimodel.result.ResultDetail;
import de.cuioss.uimodel.result.ResultState;

/**
 * A default implementation of a error handler for a {@link LazyLoadingRequest}.
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
