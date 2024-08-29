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
package de.cuioss.portal.ui.errorpages;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import jakarta.inject.Inject;

import org.junit.jupiter.api.Test;

import de.cuioss.portal.configuration.PortalConfigurationKeys;
import de.cuioss.portal.ui.test.junit5.EnablePortalUiEnvironment;
import de.cuioss.portal.ui.test.tests.AbstractPageBeanTest;
import lombok.Getter;

@EnablePortalUiEnvironment
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
    void shouldDetectFacesView() {
        getRequestConfigDecorator().setViewId(FACES_VIEW_JSF).setRequestAttribute(AbstractHttpErrorPage.JAKARTA_SERVLET_ERROR_REQUEST_URI,
                FACES_VIEW_JSF);
        underTest.initView();
        assertTrue(underTest.isRequestUriAvailable());
        assertTrue(underTest.isShouldRedirect());
    }

    @Test
    void shouldDetectEmptyView() {
        getRequestConfigDecorator().setViewId("").setRequestAttribute(AbstractHttpErrorPage.JAKARTA_SERVLET_ERROR_REQUEST_URI, "");
        underTest.initView();
        assertFalse(underTest.isRequestUriAvailable());
        assertFalse(underTest.isShouldRedirect());
    }

    @Test
    void shouldHandleNotSetView() {
        getRequestConfigDecorator().setViewId(null);
        underTest.initView();
        assertFalse(underTest.isRequestUriAvailable());
        assertFalse(underTest.isShouldRedirect());
    }

    /**
     * Paranoia mode. Should not happen, but error-handling must always be defensive
     */
    @Test
    void shouldHandleMissingServletRequest() {
        getRequestConfigDecorator().setViewId(null);
        getExternalContext().setRequest(null);
        underTest.initView();
        assertFalse(underTest.isRequestUriAvailable());
        assertFalse(underTest.isShouldRedirect());
    }

    @Test
    void shouldNotRedirectIfNotConfigured() {
        configuration.update(PortalConfigurationKeys.PAGES_ERROR_404_REDIRECT, "false");

        getRequestConfigDecorator().setViewId(FACES_VIEW_JSF).setRequestAttribute(AbstractHttpErrorPage.JAKARTA_SERVLET_ERROR_REQUEST_URI,
                FACES_VIEW_JSF);
        underTest.initView();
        assertTrue(underTest.isRequestUriAvailable());
        assertFalse(underTest.isShouldRedirect());
    }
}
