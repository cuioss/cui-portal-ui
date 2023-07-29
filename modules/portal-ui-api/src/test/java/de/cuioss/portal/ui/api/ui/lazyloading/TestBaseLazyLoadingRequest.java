package de.cuioss.portal.ui.api.ui.lazyloading;

import de.cuioss.uimodel.result.ResultObject;

public class TestBaseLazyLoadingRequest extends BaseLazyLoadingRequest<String> {

    @Override
    public ResultObject<String> backendRequest() {
        return null;
    }

    @Override
    public void handleResult(String result) {

    }

}
