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
package de.cuioss.portal.ui.test.mocks;

import de.cuioss.jsf.api.components.css.ContextState;
import de.cuioss.jsf.api.components.model.lazyloading.LazyLoadingThreadModel;
import de.cuioss.jsf.api.components.model.result_content.ResultErrorHandler;
import de.cuioss.portal.ui.api.lazyloading.LazyLoadingRequest;
import de.cuioss.tools.logging.CuiLogger;
import de.cuioss.uimodel.nameprovider.IDisplayNameProvider;
import de.cuioss.uimodel.result.ResultObject;
import jakarta.enterprise.context.Dependent;
import jakarta.faces.event.ActionEvent;
import jakarta.inject.Inject;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

/**
 * @param <T>
 * @author Oliver Wolff
 */
@Dependent
public class PortalLazyLoadingThreadModelMock<T> implements LazyLoadingThreadModel<T> {

    @Serial
    private static final long serialVersionUID = 8611619042199216440L;

    private static final CuiLogger LOGGER = new CuiLogger(PortalLazyLoadingThreadModelMock.class);

    @Inject
    private PortalLazyLoadingViewControllerMock viewControllerMock;

    @Getter
    @Setter
    private IDisplayNameProvider<?> notificationBoxValue;

    @Getter
    @Setter
    private ContextState notificationBoxState = ContextState.DEFAULT;

    @Getter
    @Setter
    private boolean renderContent;

    @Getter
    @Setter
    private boolean initialized;

    @Getter
    @Setter
    private String requestId;

    @Getter
    private ActionEvent event;

    @Getter
    private boolean notificationBoxResetted;

    @Getter
    private ResultObject<T> handledResult;

    @Override
    public void processAction(ActionEvent event) {
        this.event = event;
        if (!viewControllerMock.getStarted().isEmpty()) {
            @SuppressWarnings("unchecked") // owolff: ok for the unit-test context
            var started = (LazyLoadingRequest<T>) viewControllerMock.getStarted().getFirst();
            var requestResult = started.backendRequest();
            if (!requestResult.isValid()) {
                requestResult.getResultDetail()
                        .ifPresent(detail -> notificationBoxValue = detail.getDetail());
            }
            requestResult.logDetail("mock", LOGGER);
            started.handleResult(requestResult.getResult());
            viewControllerMock.getStarted().remove(started);
        }
    }

    @Override
    public void resetNotificationBox() {
        this.notificationBoxResetted = true;
    }

    @Override
    public void handleRequestResult(ResultObject<T> result, ResultErrorHandler errorHandler) {
        if (!result.isValid()) {
            result.getResultDetail()
                    .ifPresent(detail -> notificationBoxValue = detail.getDetail());
        }
        result.logDetail("mock", LOGGER);
        this.handledResult = result;
    }

}
