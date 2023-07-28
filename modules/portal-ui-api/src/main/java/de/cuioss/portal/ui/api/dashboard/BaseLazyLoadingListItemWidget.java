package de.cuioss.portal.ui.api.dashboard;

import java.util.Collections;
import java.util.List;

import de.cuioss.jsf.api.components.model.widget.DashboardWidgetModel;
import de.cuioss.jsf.api.components.model.widget.ListItem;
import de.cuioss.jsf.api.components.model.widget.ListItemWidgetModel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@ToString
public abstract class BaseLazyLoadingListItemWidget<T> extends BaseLazyLoadingWidget<T>
        implements ListItemWidgetModel, DashboardWidgetModel {

    private static final long serialVersionUID = -9216862082387228019L;

    @Getter
    private List<ListItem> items = Collections.emptyList();

    /**
     * @return the id of the in this module defined composite component that should
     *         be used as default for this implementations of this abstract widget
     *         class. May be overridden by a different id of a more specific
     *         composite component.
     */
    @Override
    public String getCompositeComponentId() {
        return "cui-composite:listItemWidget";
    }

    protected abstract List<ListItem> mapResult(T result);

    @Override
    public void handleResult(T result) {
        items = mapResult(result);
    }
}
