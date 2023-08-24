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
            if (cause instanceof MissingScopesException exception) {
                wrappedOauthFacade.handleMissingScopesException(exception, Collections.emptyMap());
            } else {
                super.handleRequestError(cause, detail.getDetail().toString(), errorController, log);
            }
        }
    }

}
