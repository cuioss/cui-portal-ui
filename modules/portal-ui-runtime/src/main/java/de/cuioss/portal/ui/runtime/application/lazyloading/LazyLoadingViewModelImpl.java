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
package de.cuioss.portal.ui.runtime.application.lazyloading;

import de.cuioss.jsf.api.application.message.DisplayNameMessageProducer;
import de.cuioss.jsf.api.components.css.ContextState;
import de.cuioss.jsf.api.components.model.lazyloading.LazyLoadingThreadModel;
import de.cuioss.jsf.api.components.model.result_content.ErrorController;
import de.cuioss.jsf.api.components.model.result_content.ResultErrorHandler;
import de.cuioss.portal.configuration.PortalConfigurationKeys;
import de.cuioss.portal.ui.api.lazyloading.LazyLoadingErrorHandler;
import de.cuioss.portal.ui.api.lazyloading.LazyLoadingRequest;
import de.cuioss.portal.ui.api.message.StickyMessageProducer;
import de.cuioss.tools.logging.CuiLogger;
import de.cuioss.uimodel.nameprovider.IDisplayNameProvider;
import de.cuioss.uimodel.result.ResultObject;
import jakarta.enterprise.context.Dependent;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.event.ActionEvent;
import jakarta.inject.Inject;
import jakarta.inject.Provider;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static de.cuioss.portal.configuration.PortalConfigurationKeys.PORTAL_LAZY_LOADING_REQUEST_RETRIEVE_TIMEOUT;

/**
 * Implementation of a {@link LazyLoadingThreadModel} using the
 * {@link ThreadManager} to retrieve the result and errors from the request.
 * <p>
 * Will wait max
 * {@link PortalConfigurationKeys#PORTAL_LAZY_LOADING_REQUEST_RETRIEVE_TIMEOUT}
 * for the request to return, otherwise it will be terminated.
 *
 * @param <T> should implement {@link Serializable}
 */
@EqualsAndHashCode(of = {"requestId"})
@ToString(of = {"requestId", "initialized", "renderContent", "notificationBoxValue"})
@Dependent
public class LazyLoadingViewModelImpl<T> implements LazyLoadingThreadModel<T>, ErrorController {

    @Serial
    private static final long serialVersionUID = -3343380539839996245L;

    private static final CuiLogger LOGGER = new CuiLogger(LazyLoadingViewModelImpl.class);
    @Getter
    private final String requestId = UUID.randomUUID().toString();
    @Inject
    ThreadManager threadManager;
    @Inject
    @ConfigProperty(name = PORTAL_LAZY_LOADING_REQUEST_RETRIEVE_TIMEOUT)
    int requestRetrieveTimeout;
    @Inject
    Provider<DisplayNameMessageProducer> displayNameMessageProducer;
    @Inject
    StickyMessageProducer stickyMessageProducer;
    @Getter
    private IDisplayNameProvider<?> notificationBoxValue;
    @Getter
    private ContextState notificationBoxState;
    @Getter
    @Setter
    private boolean renderContent;
    @Getter
    private boolean initialized;

    /**
     * Will be called by the ajax call to update the lazy loading content.
     * <p>
     * Tries to retrieve the result of the backend call and handles errors.
     *
     * @param actionEvent dummy.
     */
    @SuppressWarnings("unchecked")
    @Override
    public void processAction(ActionEvent actionEvent) {
        LOGGER.debug("retrieveRequest '%s'", requestId);
        var handle = threadManager.retrieve(requestId);
        if (null == handle) {
            LOGGER.debug("No request found requestId='%s'", requestId);
            new LazyLoadingErrorHandler().handleRequestError(null, "The request handle could not be found.", this, LOGGER);
            initialized = true;
            return;
        }
        var request = (LazyLoadingRequest<T>) handle.context();
        try {
            var result = (ResultObject<T>) handle.future().get(requestRetrieveTimeout, TimeUnit.SECONDS);
            LOGGER.debug("result of , requestId='%s': result='%s'", requestId, result);
            handleRequestResult(result, request.getErrorHandler());
            request.handleResult(result.getResult());
            initialized = true;
        } catch (ExecutionException | TimeoutException | CancellationException e) {
            LOGGER.debug(e, "Run into Exception, requestId='%s'", requestId);
            request.getErrorHandler().handleRequestError(e, "The request failed", this, LOGGER);
            initialized = true;
        } catch (InterruptedException ie) {
            LOGGER.debug(ie, "Run into InterruptedException, requestId='%s'", requestId);
            request.getErrorHandler().handleRequestError(ie, "The request was interrupted", this, LOGGER);
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
        displayNameMessageProducer.get().showAsGlobalMessage(value, severity);
    }

    @Override
    public void addStickyMessage(IDisplayNameProvider<?> value, ContextState state) {
        stickyMessageProducer.setMessageAsString(DisplayNameMessageProducer.resolve(value), state);
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
                    this, LOGGER);
        } else {
            setRenderContent(true);
        }
    }
}
