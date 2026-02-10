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
package de.cuioss.portal.ui.errorpages;

import de.cuioss.portal.configuration.PortalConfigurationKeys;
import de.cuioss.portal.ui.test.junit5.EnablePortalUiEnvironment;
import de.cuioss.portal.ui.test.tests.AbstractPageBeanTest;
import de.cuioss.test.jsf.config.decorator.RequestConfigDecorator;
import de.cuioss.test.juli.LogAsserts;
import de.cuioss.test.juli.TestLogLevel;
import de.cuioss.test.juli.junit5.EnableTestLogger;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import lombok.Getter;
import org.apache.myfaces.test.mock.MockExternalContext;
import org.jboss.weld.junit5.ExplicitParamInjection;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@EnablePortalUiEnvironment
@ExplicitParamInjection
@EnableTestLogger
class Http404PageBeanTest extends AbstractPageBeanTest<Http404PageBean> {

    private static final String FACES_VIEW_JSF = "/view.jsf";

    @Inject
    @Getter
    private Http404PageBean underTest;

    @Test
    void shouldProvideCorrectCode() {
        assertEquals(404, underTest.getErrorCode());
    }

    @Test
    void shouldDetectFacesView(RequestConfigDecorator requestConfig) {
        requestConfig.setViewId(FACES_VIEW_JSF);
        requestConfig.setRequestAttribute(AbstractHttpErrorPage.JAKARTA_SERVLET_ERROR_REQUEST_URI, FACES_VIEW_JSF);
        underTest.initView();
        assertTrue(underTest.isRequestUriAvailable());
        assertTrue(underTest.isShouldRedirect());
        LogAsserts.assertLogMessagePresentContaining(TestLogLevel.WARN, "PORTAL-UI-ERR-100");
    }

    @Test
    void shouldDetectEmptyView(RequestConfigDecorator requestConfig) {
        requestConfig.setViewId("");
        requestConfig.setRequestAttribute(AbstractHttpErrorPage.JAKARTA_SERVLET_ERROR_REQUEST_URI, "");
        underTest.initView();
        assertFalse(underTest.isRequestUriAvailable());
        assertFalse(underTest.isShouldRedirect());
        LogAsserts.assertLogMessagePresentContaining(TestLogLevel.WARN, "PORTAL-UI-ERR-100");
    }

    @Test
    void shouldHandleNotSetView(RequestConfigDecorator requestConfig) {
        requestConfig.setViewId(null);
        underTest.initView();
        assertFalse(underTest.isRequestUriAvailable());
        assertFalse(underTest.isShouldRedirect());
        LogAsserts.assertLogMessagePresentContaining(TestLogLevel.WARN, "PORTAL-UI-ERR-100");
    }

    /**
     * Paranoia mode. Should not happen, but error-handling must always be defensive
     */
    @Test
    void shouldHandleMissingServletRequest(RequestConfigDecorator requestConfig) {
        requestConfig.setViewId(null);
        ((MockExternalContext) FacesContext.getCurrentInstance().getExternalContext()).setRequest(null);
        underTest.initView();
        assertFalse(underTest.isRequestUriAvailable());
        assertFalse(underTest.isShouldRedirect());
        LogAsserts.assertLogMessagePresentContaining(TestLogLevel.WARN, "PORTAL-UI-ERR-100");
    }

    @Test
    void shouldNotRedirectIfNotConfigured(RequestConfigDecorator requestConfig) {
        configuration.update(PortalConfigurationKeys.PAGES_ERROR_404_REDIRECT, "false");

        requestConfig.setViewId(FACES_VIEW_JSF);
        requestConfig.setRequestAttribute(AbstractHttpErrorPage.JAKARTA_SERVLET_ERROR_REQUEST_URI, FACES_VIEW_JSF);
        underTest.initView();
        assertTrue(underTest.isRequestUriAvailable());
        assertFalse(underTest.isShouldRedirect());
        LogAsserts.assertLogMessagePresentContaining(TestLogLevel.WARN, "PORTAL-UI-ERR-100");
    }
}
