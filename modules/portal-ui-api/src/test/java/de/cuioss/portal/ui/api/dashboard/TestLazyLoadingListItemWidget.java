package de.cuioss.portal.ui.api.dashboard;

import java.util.Collections;
import java.util.List;

import de.cuioss.jsf.api.components.model.widget.ListItem;
import de.cuioss.uimodel.nameprovider.IDisplayNameProvider;
import de.cuioss.uimodel.nameprovider.LabeledKey;
import de.cuioss.uimodel.result.ResultObject;

class TestLazyLoadingListItemWidget extends BaseLazyLoadingListItemWidget<String> {

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
