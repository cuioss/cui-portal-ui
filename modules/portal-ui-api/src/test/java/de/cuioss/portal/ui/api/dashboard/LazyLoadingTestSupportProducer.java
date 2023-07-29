package de.cuioss.portal.ui.api.dashboard;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;

import de.cuioss.jsf.api.components.css.ContextState;
import de.cuioss.jsf.api.components.model.lazyloading.LazyLoadingThreadModel;
import de.cuioss.jsf.api.components.model.resultContent.ResultErrorHandler;
import de.cuioss.portal.ui.api.ui.lazyloading.LazyLoadingViewController;
import de.cuioss.uimodel.nameprovider.IDisplayNameProvider;
import de.cuioss.uimodel.result.ResultObject;

@ApplicationScoped
public class LazyLoadingTestSupportProducer {

    @Produces
    @Dependent
    private LazyLoadingViewController viewController;

    @Produces
    @Dependent
    private LazyLoadingThreadModel<String> threadModell;

    public LazyLoadingTestSupportProducer() {
        viewController = request -> {
            // TODO Auto-generated method stub

        };
        threadModell = new LazyLoadingThreadModel<>() {

            private static final long serialVersionUID = -4738933238569640453L;

            @Override
            public void processAction(ActionEvent actionEvent) throws AbortProcessingException {
            }

            @Override
            public boolean isRenderContent() {
                return false;
            }

            @Override
            public boolean isInitialized() {
                return false;
            }

            @Override
            public IDisplayNameProvider<?> getNotificationBoxValue() {
                return null;
            }

            @Override
            public ContextState getNotificationBoxState() {
                return ContextState.DANGER;
            }

            @Override
            public void resetNotificationBox() {

            }

            @Override
            public void handleRequestResult(ResultObject<String> result, ResultErrorHandler errorHandler) {

            }

            @Override
            public long getRequestId() {
                return 0;
            }
        };
    }

}
