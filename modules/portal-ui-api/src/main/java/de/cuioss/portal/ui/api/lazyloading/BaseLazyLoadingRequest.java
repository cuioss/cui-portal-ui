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

import de.cuioss.jsf.api.components.model.lazyloading.LazyLoadingThreadModel;
import jakarta.inject.Inject;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@EqualsAndHashCode
@ToString
public abstract class BaseLazyLoadingRequest<T> implements LazyLoadingRequest<T> {

    @Getter
    @Inject
    LazyLoadingThreadModel<T> viewModel;

    @Override
    public String getRequestId() {
        return viewModel.getRequestId();
    }
}
