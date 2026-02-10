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
package de.cuioss.portal.ui.api.dashboard;

import de.cuioss.jsf.api.components.model.widget.DashboardWidgetModel;
import de.cuioss.jsf.api.components.model.widget.ListItem;
import de.cuioss.jsf.api.components.model.widget.ListItemWidgetModel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.io.Serial;
import java.util.Collections;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@ToString
public abstract class BaseLazyLoadingListItemWidget<T> extends BaseLazyLoadingWidget<T>
        implements ListItemWidgetModel, DashboardWidgetModel {

    @Serial
    private static final long serialVersionUID = -9216862082387228019L;
    /**
     * The id of the within this module defined composite component that should be used
     * as default for this implementation of this abstract widget class.
     * May be overridden by a different id of a more specific composite component.
     */
    private static final String COMPOSITE_COMPONENT_ID = "cui-composite:listItemWidget";
    @Getter
    private List<ListItem> items = Collections.emptyList();

    @Override
    public String getCompositeComponentId() {
        return COMPOSITE_COMPONENT_ID;
    }

    protected abstract List<ListItem> mapResult(T result);

    @Override
    public void handleResult(T result) {
        items = mapResult(result);
    }
}
