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

import javax.enterprise.context.Dependent;

import de.cuioss.uimodel.nameprovider.IDisplayNameProvider;
import de.cuioss.uimodel.result.ResultObject;

@Dependent
class TestLazyLoadingWidget extends BaseLazyLoadingWidget<String> {

    private static final long serialVersionUID = -8322319620282555449L;

    @Override
    public ResultObject<String> backendRequest() {
        return null;
    }

    @Override
    public void handleResult(String result) {

    }

    @Override
    public IDisplayNameProvider<?> getTitle() {
        return null;
    }

    @Override
    public String getId() {
        return null;
    }

}
