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
package de.cuioss.portal.ui.runtime.application.dashboard;

import static de.cuioss.portal.configuration.PortalConfigurationKeys.DASHBOARD_WIDGET;
import static de.cuioss.tools.collect.CollectionLiterals.mutableList;

import java.io.Serial;
import java.io.Serializable;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import jakarta.enterprise.inject.Instance;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import de.cuioss.jsf.api.components.model.widget.DashboardWidgetModel;

import jakarta.annotation.PostConstruct;
import de.cuioss.jsf.api.components.model.widget.WidgetModel;
import de.cuioss.portal.configuration.types.ConfigAsFilteredMap;
import de.cuioss.portal.ui.api.dashboard.PortalDashboardWidget;
import de.cuioss.tools.string.MoreStrings;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@ViewScoped
@Named
@ToString
@EqualsAndHashCode
public class DashboardWidgetRegistration implements Serializable {

    @Serial
    private static final long serialVersionUID = -8229255432062229719L;

    @Getter
    private List<DashboardWidgetModel> widgets;

    @Inject
    @PortalDashboardWidget
    private Instance<DashboardWidgetModel> widgetsInstances;

    @Inject
    @ConfigAsFilteredMap(startsWith = DASHBOARD_WIDGET, stripPrefix = true)
    private Map<String, String> widgetConfig;

    @PostConstruct
    public void init() {
        widgets = mutableList(
            widgetsInstances.stream()
                .filter(DashboardWidgetModel::isRendered)
                .filter(widget -> getOrder(widget).isPresent())
                .sorted(Comparator.comparing((DashboardWidgetModel o) -> getOrder(o).get()))
                .toList());
    }

    private Optional<Integer> getOrder(WidgetModel widget) {
        var orderKey = widget.getId() + ".order";
        var enabledKey = widget.getId() + ".enabled";
        if (MoreStrings.isEmpty(widget.getId()) || !widgetConfig.containsKey(orderKey)
                || widgetConfig.containsKey(enabledKey) && !Boolean.parseBoolean(widgetConfig.get(enabledKey))) {
            return Optional.empty();
        }
        return Optional.of(Integer.valueOf(widgetConfig.get(orderKey)));
    }
}
