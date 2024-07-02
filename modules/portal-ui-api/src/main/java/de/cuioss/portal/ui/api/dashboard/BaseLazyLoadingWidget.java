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

import jakarta.faces.event.ActionEvent;
import jakarta.inject.Inject;

import de.cuioss.jsf.api.components.css.ContextState;
import de.cuioss.jsf.api.components.model.lazyloading.LazyLoadingThreadModel;
import de.cuioss.jsf.api.components.model.widget.BaseWidget;
import de.cuioss.portal.ui.api.lazyloading.LazyLoadingRequest;
import de.cuioss.portal.ui.api.lazyloading.LazyLoadingViewController;
import de.cuioss.uimodel.nameprovider.IDisplayNameProvider;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serial;

@ToString
@EqualsAndHashCode(callSuper = false)
public abstract class BaseLazyLoadingWidget<T> extends BaseWidget implements LazyLoadingRequest<T> {

    @Serial
    private static final long serialVersionUID = -3234472642651082710L;

    @Inject
    LazyLoadingThreadModel<T> viewModel;

    @Inject
    LazyLoadingViewController viewController;

    @Override
    public void startInitialize() {
        viewController.startRequest(this);
    }

    @Override
    public String getRequestId() {
        return viewModel.getRequestId();
    }

    @Override
    public IDisplayNameProvider<?> getNotificationBoxValue() {
        return viewModel.getNotificationBoxValue();
    }

    @Override
    public ContextState getNotificationBoxState() {
        return viewModel.getNotificationBoxState();
    }

    @Override
    public boolean isInitialized() {
        return viewModel.isInitialized();
    }

    @Override
    public boolean isRenderContent() {
        return viewModel.isRenderContent();
    }

    @Override
    public void processAction(ActionEvent actionEvent) {
        viewModel.processAction(actionEvent);
    }

}
