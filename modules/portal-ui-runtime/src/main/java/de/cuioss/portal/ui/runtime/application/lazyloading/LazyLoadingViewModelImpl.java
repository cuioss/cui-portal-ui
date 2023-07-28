package de.cuioss.portal.ui.runtime.application.lazyloading;

import static de.cuioss.portal.configuration.PortalConfigurationKeys.PORTAL_LAZYLOADING_REQUEST_RETRIEVE_TIMEOUT;

import java.io.Serializable;
import java.util.Random;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.enterprise.context.Dependent;
import javax.faces.application.FacesMessage;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import de.cuioss.jsf.api.application.message.DisplayNameProviderMessageProducer;
import de.cuioss.jsf.api.application.message.MessageProducer;
import de.cuioss.jsf.api.components.css.ContextState;
import de.cuioss.jsf.api.components.model.lazyloading.LazyLoadingThreadModel;
import de.cuioss.jsf.api.components.model.resultContent.ErrorController;
import de.cuioss.jsf.api.components.model.resultContent.ResultErrorHandler;
import de.cuioss.portal.configuration.PortalConfigurationKeys;
import de.cuioss.portal.configuration.initializer.PortalInitializer;
import de.cuioss.portal.ui.api.message.PortalMessageProducer;
import de.cuioss.portal.ui.api.message.PortalStickyMessageProducer;
import de.cuioss.portal.ui.api.message.StickyMessageProducer;
import de.cuioss.portal.ui.api.ui.lazyloading.LazyLoadingErrorHandler;
import de.cuioss.portal.ui.api.ui.lazyloading.LazyLoadingRequest;
import de.cuioss.tools.logging.CuiLogger;
import de.cuioss.uimodel.nameprovider.IDisplayNameProvider;
import de.cuioss.uimodel.result.ResultObject;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Implementation of a {@link LazyLoadingThreadModel <T>} using the
 * {@link ThreadManager} to retrieve the result and errors from the request.
 *
 * Will wait max
 * {@link PortalConfigurationKeys#PORTAL_LAZYLOADING_REQUEST_RETRIEVE_TIMEOUT}
 * for the request to return, otherwise it will be terminated.
 *
 * @param <T> should implement {@link Serializable}
 */
@EqualsAndHashCode(of = { "requestId" })
@ToString(of = { "requestId", "initialized", "renderContent", "notificationBoxValue" })
@Dependent
public class LazyLoadingViewModelImpl<T> implements LazyLoadingThreadModel<T>, ErrorController {

    private static final Random RANDOM = new Random();

    private static final long serialVersionUID = -3343380539839996245L;

    private static final CuiLogger log = new CuiLogger(LazyLoadingViewModelImpl.class);

    @Inject
    @PortalInitializer
    private ThreadManager threadManager;

    @Inject
    @ConfigProperty(name = PORTAL_LAZYLOADING_REQUEST_RETRIEVE_TIMEOUT)
    private int requestRetrieveTimeout;

    @PortalMessageProducer
    @Inject
    private MessageProducer messageProducer;

    @Inject
    @PortalStickyMessageProducer
    private StickyMessageProducer stickyMessageProducer;

    @Getter
    private IDisplayNameProvider<?> notificationBoxValue;

    @Getter
    private ContextState notificationBoxState;

    @Getter
    @Setter
    private boolean renderContent;

    @Getter
    private boolean initialized;

    @Getter
    private long requestId = RANDOM.nextLong();

    /**
     * Will be called by the ajax call to update the lazy loading content.
     *
     * Tries to retrieve the result of the backend call and handles errors.
     *
     * @param actionEvent dummy.
     */
    @SuppressWarnings("unchecked")
    @Override
    public void processAction(ActionEvent actionEvent) {
        log.trace("retrieveRequest {}", requestId);
        var handle = threadManager.retrieve(requestId);
        if (null == handle) {
            new LazyLoadingErrorHandler().handleRequestError(null, "The request handle could not be found.", this, log);
            initialized = true;
            return;
        }
        var request = (LazyLoadingRequest<T>) handle.getContext();
        try {
            var result = (ResultObject<T>) handle.getFuture().get(requestRetrieveTimeout, TimeUnit.SECONDS);
            log.trace("result of {}: {}", requestId, result);
            handleRequestResult(result, request.getErrorHandler());
            request.handleResult(result.getResult());
            initialized = true;
        } catch (ExecutionException | TimeoutException | CancellationException e) {
            request.getErrorHandler().handleRequestError(e, "The request failed", this, log);
            initialized = true;
        } catch (InterruptedException ie) {
            request.getErrorHandler().handleRequestError(ie, "The request was interrupted", this, log);
            initialized = true;
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void addNotificationBox(IDisplayNameProvider<?> value, ContextState state) {
        notificationBoxValue = value;
        notificationBoxState = state;
    }

    @Override
    public void addGlobalFacesMessage(IDisplayNameProvider<?> value, FacesMessage.Severity severity) {
        new DisplayNameProviderMessageProducer(messageProducer).showAsGlobalMessage(value, severity);
    }

    @Override
    public void addStickyMessage(IDisplayNameProvider<?> value, ContextState state) {
        stickyMessageProducer.setMessageAsString(DisplayNameProviderMessageProducer.resolve(value), state);
    }

    @Override
    public void resetNotificationBox() {
        notificationBoxValue = null;
        renderContent = false;
    }

    @Override
    public void handleRequestResult(ResultObject<T> result, ResultErrorHandler errorHandler) {
        var resultDetail = result.getResultDetail();
        if (resultDetail.isPresent()) {
            errorHandler.handleResultDetail(result.getState(), resultDetail.get(), result.getErrorCode().orElse(null),
                    this, log);
        } else {
            setRenderContent(true);
        }
    }
}
