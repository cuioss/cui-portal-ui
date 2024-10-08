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
package de.cuioss.portal.ui.test.mocks;

import de.cuioss.portal.ui.api.lazyloading.LazyLoadingRequest;
import de.cuioss.portal.ui.api.lazyloading.LazyLoadingViewController;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Oliver Wolff
 */
@ApplicationScoped
public class PortalLazyLoadingViewControllerMock implements LazyLoadingViewController {

    @Getter
    private final List<LazyLoadingRequest<?>> started = new ArrayList<>();

    @Override
    public void startRequest(LazyLoadingRequest<?> request) {
        started.add(request);
    }

}
