package de.cuioss.portal.ui.api.dashboard;

import javax.faces.event.ActionEvent;
import javax.inject.Inject;

import de.cuioss.jsf.api.components.css.ContextState;
import de.cuioss.jsf.api.components.model.lazyloading.LazyLoadingThreadModel;
import de.cuioss.jsf.api.components.model.widget.BaseWidget;
import de.cuioss.portal.ui.api.ui.lazyloading.LazyLoadingRequest;
import de.cuioss.portal.ui.api.ui.lazyloading.LazyLoadingViewController;
import de.cuioss.uimodel.nameprovider.IDisplayNameProvider;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode(callSuper = false)
public abstract class BaseLazyLoadingWidget<T> extends BaseWidget implements LazyLoadingRequest<T> {

    private static final long serialVersionUID = -3234472642651082710L;

    @Inject
    private LazyLoadingThreadModel<T> viewModel;

    @Inject
    private LazyLoadingViewController viewController;

    @Override
    public void startInitialize() {
        viewController.startRequest(this);
    }

    @Override
    public long getRequestId() {
        return viewModel.getRequestId();
    }

    @Override
    public IDisplayNameProvider<?> getNotificationBoxValue() {
        return viewModel.getNotificationBoxValue();
    }

    @Override
    public ContextState getNotificationBoxState() {
        return viewModel.getNotificationBoxState();
    }

    @Override
    public boolean isInitialized() {
        return viewModel.isInitialized();
    }

    @Override
    public boolean isRenderContent() {
        return viewModel.isRenderContent();
    }

    @Override
    public void processAction(ActionEvent actionEvent) {
        viewModel.processAction(actionEvent);
    }

}
