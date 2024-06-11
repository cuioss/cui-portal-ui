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

import java.io.Serial;
import java.util.Collections;
import java.util.List;

import de.cuioss.jsf.api.components.model.widget.ListItem;
import de.cuioss.uimodel.nameprovider.IDisplayNameProvider;
import de.cuioss.uimodel.nameprovider.LabeledKey;
import de.cuioss.uimodel.result.ResultObject;

class TestLazyLoadingListItemWidget extends BaseLazyLoadingListItemWidget<String> {

    @Serial
    private static final long serialVersionUID = 5105699369583115904L;

    @Override
    public LabeledKey getNoItemsMessage() {
        return null;
    }

    @Override
    public boolean isRenderShowMoreButton() {
        return false;
    }

    @Override
    public IDisplayNameProvider<?> getTitle() {
        return null;
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public ResultObject<String> backendRequest() {
        return null;
    }

    @Override
    protected List<ListItem> mapResult(String result) {
        return Collections.emptyList();
    }

}
