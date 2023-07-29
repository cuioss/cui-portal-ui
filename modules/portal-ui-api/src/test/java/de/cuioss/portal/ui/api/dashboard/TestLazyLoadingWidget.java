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
