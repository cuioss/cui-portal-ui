package de.cuioss.portal.ui.oauth;

import java.util.Collections;

import de.cuioss.jsf.api.components.model.resultContent.ErrorController;
import de.cuioss.portal.ui.api.ui.lazyloading.LazyLoadingErrorHandler;
import de.cuioss.tools.logging.CuiLogger;
import de.cuioss.uimodel.result.ResultDetail;
import de.cuioss.uimodel.result.ResultState;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OauthMissingScopesErrorHandler extends LazyLoadingErrorHandler {

    private final WrappedOauthFacade wrappedOauthFacade;

    @Override
    public void handleResultDetail(final ResultState state, final ResultDetail detail, final Enum<?> errorCode,
            final ErrorController errorController, final CuiLogger log) {

        log.trace("OauthMissingScopesErrorHandler handleRequestError");

        if (null != detail && detail.getCause().isPresent()) {
            @SuppressWarnings("squid:S3655") // isPresent is called well
            final var cause = detail.getCause().get();
            if (cause instanceof MissingScopesException) {
                wrappedOauthFacade.handleMissingScopesException((MissingScopesException) cause, Collections.emptyMap());
            } else {
                super.handleRequestError(cause, detail.getDetail().toString(), errorController, log);
            }
        }
    }

}
