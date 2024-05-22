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
package de.cuioss.portal.ui.api.dashboard;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.Produces;
import jakarta.faces.event.AbortProcessingException;
import jakarta.faces.event.ActionEvent;

import de.cuioss.jsf.api.components.css.ContextState;
import de.cuioss.jsf.api.components.model.lazyloading.LazyLoadingThreadModel;
import de.cuioss.jsf.api.components.model.resultContent.ResultErrorHandler;
import de.cuioss.portal.ui.api.ui.lazyloading.LazyLoadingViewController;
import de.cuioss.uimodel.nameprovider.IDisplayNameProvider;
import de.cuioss.uimodel.result.ResultObject;

@ApplicationScoped
public class LazyLoadingTestSupportProducer {

    @Produces
    @Dependent
    private LazyLoadingViewController viewController;

    @Produces
    @Dependent
    private LazyLoadingThreadModel<String> threadModell;

    public LazyLoadingTestSupportProducer() {
        viewController = request -> {
            // TODO Auto-generated method stub

        };
        threadModell = new LazyLoadingThreadModel<>() {

            private static final long serialVersionUID = -4738933238569640453L;

            @Override
            public void processAction(ActionEvent actionEvent) throws AbortProcessingException {
            }

            @Override
            public boolean isRenderContent() {
                return false;
            }

            @Override
            public boolean isInitialized() {
                return false;
            }

            @Override
            public IDisplayNameProvider<?> getNotificationBoxValue() {
                return null;
            }

            @Override
            public ContextState getNotificationBoxState() {
                return ContextState.DANGER;
            }

            @Override
            public void resetNotificationBox() {

            }

            @Override
            public void handleRequestResult(ResultObject<String> result, ResultErrorHandler errorHandler) {

            }

            @Override
            public long getRequestId() {
                return 0;
            }
        };
    }

}
