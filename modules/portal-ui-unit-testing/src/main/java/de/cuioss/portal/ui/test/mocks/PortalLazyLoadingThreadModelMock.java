package de.cuioss.portal.ui.test.mocks;

import javax.enterprise.context.Dependent;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;

import de.cuioss.jsf.api.components.css.ContextState;
import de.cuioss.jsf.api.components.model.lazyloading.LazyLoadingThreadModel;
import de.cuioss.jsf.api.components.model.resultContent.ResultErrorHandler;
import de.cuioss.portal.ui.api.ui.lazyloading.LazyLoadingRequest;
import de.cuioss.tools.logging.CuiLogger;
import de.cuioss.uimodel.nameprovider.IDisplayNameProvider;
import de.cuioss.uimodel.result.ResultObject;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Oliver Wolff
 * @param <T>
 *
 */
@Dependent
public class PortalLazyLoadingThreadModelMock<T> implements LazyLoadingThreadModel<T> {

    private static final long serialVersionUID = 8611619042199216440L;

    private static final CuiLogger log = new CuiLogger(PortalLazyLoadingThreadModelMock.class);

    @Inject
    private PortalLazyLoadingViewControllerMock viewControllerMock;

    @Getter
    @Setter
    private IDisplayNameProvider<?> notificationBoxValue;

    @Getter
    @Setter
    private ContextState notificationBoxState = ContextState.DEFAULT;

    @Getter
    @Setter
    private boolean renderContent;

    @Getter
    @Setter
    private boolean initialized;

    @Getter
    @Setter
    private long requestId;

    @Getter
    private ActionEvent event;

    @Getter
    private boolean notificationBoxResetted;

    @Getter
    private ResultObject<T> handledResult;

    @Override
    @SuppressWarnings("squid:S3655") // owolff: implicitly checked with isValid
    public void processAction(ActionEvent event) {
        this.event = event;
        if (!viewControllerMock.getStarted().isEmpty()) {
            @SuppressWarnings("unchecked") // owolff: ok for the unit-test context
            var started = (LazyLoadingRequest<T>) viewControllerMock.getStarted().iterator().next();
            var requestResult = started.backendRequest();
            if (!requestResult.isValid()) {
                notificationBoxValue = requestResult.getResultDetail().get().getDetail();
            }
            requestResult.logDetail("mock", log);
            started.handleResult(requestResult.getResult());
            viewControllerMock.getStarted().remove(started);
        }
    }

    @Override
    public void resetNotificationBox() {
        this.notificationBoxResetted = true;
    }

    @Override
    @SuppressWarnings("squid:S3655") // owolff: implicitly checked with isValid
    public void handleRequestResult(ResultObject<T> result, ResultErrorHandler errorHandler) {
        if (!result.isValid()) {
            notificationBoxValue = result.getResultDetail().get().getDetail();
        }
        result.logDetail("mock", log);
        this.handledResult = result;
    }

}
