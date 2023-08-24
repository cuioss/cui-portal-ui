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
package de.cuioss.portal.ui.api.ui.lazyloading;

import javax.faces.context.FacesContext;

import de.cuioss.uimodel.result.ResultDetail;
import de.cuioss.uimodel.result.ResultObject;

/**
 * A request to a backend service, which can be run in a separate thread and
 * will return a {@link ResultObject}. On success the result will be handled in
 * UI context.
 *
 * @param <T> should be serializable
 */
public interface LazyLoadingRequest<T> {

    /**
     * Trigger a backend request. This request will be started during initialization
     * of the view and run in a separate thread without UI context. It must not try
     * to access any session specific attributes or parameters or session scoped
     * beans and must not try to access the {@link FacesContext}.
     *
     * @return a {@link ResultObject}
     */
    ResultObject<T> backendRequest();

    /**
     * Handle the result of the {@link #backendRequest()}. Will be run in UI
     * context. Will always be called, independent from the {@link ResultObject}
     *
     * @param result the result of the operation.
     */
    void handleResult(T result);

    /**
     * @return A unique identifier to store the request and allow retrieving of the
     *         result later.
     */
    long getRequestId();

    /**
     * @return an instance of a {@link LazyLoadingErrorHandler} to be called if a
     *         {@link ResultDetail} was provided in the {@link #backendRequest()}.
     */
    default LazyLoadingErrorHandler getErrorHandler() {
        return new LazyLoadingErrorHandler();
    }

}
