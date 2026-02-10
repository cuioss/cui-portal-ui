/*
 * Copyright © 2025 CUI-OpenSource-Software (info@cuioss.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.cuioss.portal.ui.oauth;

import de.cuioss.jsf.api.components.model.result_content.ErrorController;
import de.cuioss.tools.logging.CuiLogger;
import de.cuioss.uimodel.nameprovider.DisplayName;
import de.cuioss.uimodel.result.ResultDetail;
import de.cuioss.uimodel.result.ResultState;
import org.easymock.EasyMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.easymock.EasyMock.*;

class OauthMissingScopesErrorHandlerTest {

    private static final CuiLogger LOGGER = new CuiLogger(OauthMissingScopesErrorHandlerTest.class);

    private WrappedOauthFacade wrappedOauthFacadeMock;
    private OauthMissingScopesErrorHandler underTest;
    private ErrorController errorControllerMock;

    @BeforeEach
    void setUp() {
        wrappedOauthFacadeMock = EasyMock.createNiceMock(WrappedOauthFacade.class);
        errorControllerMock = EasyMock.createNiceMock(ErrorController.class);
        underTest = new OauthMissingScopesErrorHandler(wrappedOauthFacadeMock);
    }

    @Test
    void shouldHandleNullDetail() {
        replay(wrappedOauthFacadeMock, errorControllerMock);
        underTest.handleResultDetail(ResultState.ERROR, null, null, errorControllerMock, LOGGER);
        verify(wrappedOauthFacadeMock);
    }

    @Test
    void shouldHandleDetailWithoutCause() {
        var detail = new ResultDetail(new DisplayName("some error"));
        replay(wrappedOauthFacadeMock, errorControllerMock);
        underTest.handleResultDetail(ResultState.ERROR, detail, null, errorControllerMock, LOGGER);
        verify(wrappedOauthFacadeMock);
    }

    @Test
    void shouldHandleMissingScopesException() {
        var exception = new MissingScopesException("read write");
        var detail = new ResultDetail(new DisplayName("missing scopes"), exception);

        wrappedOauthFacadeMock.handleMissingScopesException(eq(exception), eq(Collections.emptyMap()));
        expectLastCall().once();
        replay(wrappedOauthFacadeMock, errorControllerMock);

        underTest.handleResultDetail(ResultState.ERROR, detail, null, errorControllerMock, LOGGER);
        verify(wrappedOauthFacadeMock);
    }

    @Test
    void shouldDelegateNonMissingScopesExceptionToSuper() {
        var exception = /*~~(TODO: Use specific not RuntimeException. Suppress: // cui-rewrite:disable InvalidExceptionUsageRecipe)~~>*/new RuntimeException("other error");
        var detail = new ResultDetail(new DisplayName("other error"), exception);

        replay(wrappedOauthFacadeMock, errorControllerMock);
        underTest.handleResultDetail(ResultState.ERROR, detail, null, errorControllerMock, LOGGER);
        verify(wrappedOauthFacadeMock);
    }
}
