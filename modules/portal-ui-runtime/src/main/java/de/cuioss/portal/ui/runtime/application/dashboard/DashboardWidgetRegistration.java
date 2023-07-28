package de.cuioss.portal.ui.runtime.application.dashboard;

import static de.cuioss.portal.configuration.PortalConfigurationKeys.DASHBOARD_WIDGET;
import static de.cuioss.tools.collect.CollectionLiterals.mutableList;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import de.cuioss.jsf.api.components.model.widget.DashboardWidgetModel;
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
                widgetsInstances.stream().filter(widget -> widget.isRendered() && getOrder(widget).isPresent())
                        .sorted(Comparator.comparing((DashboardWidgetModel o) -> getOrder(o).get()))
                        .collect(Collectors.toList()));
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
