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

import java.io.Serial;
import java.io.Serializable;

import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;

import de.cuioss.portal.configuration.initializer.PortalInitializer;
import de.cuioss.portal.ui.api.ui.lazyloading.LazyLoadingRequest;
import de.cuioss.portal.ui.api.ui.lazyloading.LazyLoadingViewController;
import de.cuioss.tools.logging.CuiLogger;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Implementation of {@link LazyLoadingViewController} using the
 * {@link ThreadManager} to start backend requests.
 */
@Dependent
@EqualsAndHashCode(exclude = { "threadManager" })
@ToString(exclude = { "threadManager" })
public class LazyLoadingViewControllerImpl implements LazyLoadingViewController, Serializable {

    @Serial
    private static final long serialVersionUID = 4698361355745984370L;

    private static final CuiLogger log = new CuiLogger(LazyLoadingViewControllerImpl.class);

    @Inject
    @PortalInitializer
    private ThreadManager threadManager;

    @Override
    public void startRequest(LazyLoadingRequest<?> request) {
        log.trace("startRequest {}", request);
        threadManager.store(request.getRequestId(), request::backendRequest, request);
    }
}
