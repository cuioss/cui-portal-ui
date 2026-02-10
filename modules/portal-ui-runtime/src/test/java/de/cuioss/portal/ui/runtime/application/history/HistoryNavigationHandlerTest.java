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
package de.cuioss.portal.ui.runtime.application.history;

import de.cuioss.portal.ui.api.history.HistoryManager;
import de.cuioss.portal.ui.api.pages.HomePage;
import de.cuioss.portal.ui.runtime.application.view.matcher.ViewMatcherProducer;
import de.cuioss.portal.ui.test.junit5.EnablePortalUiEnvironment;
import jakarta.faces.application.ConfigurableNavigationHandler;
import jakarta.faces.context.FacesContext;
import jakarta.faces.lifecycle.ClientWindowScoped;
import jakarta.inject.Inject;
import org.apache.myfaces.test.mock.MockHttpServletResponse;
import org.jboss.weld.junit5.auto.ActivateScopes;
import org.jboss.weld.junit5.auto.AddBeanClasses;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static de.cuioss.portal.ui.runtime.application.history.HistoryManagerBeanTest.CURRENT_VIEW;
import static de.cuioss.portal.ui.runtime.application.history.HistoryManagerBeanTest.SECOND_VIEW;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@EnablePortalUiEnvironment
@AddBeanClasses({DefaultHistoryConfiguration.class, HistoryManagerBean.class, ViewMatcherProducer.class})
@ActivateScopes(ClientWindowScoped.class)
class HistoryNavigationHandlerTest {

    public static final String PORTAL_HOME_JSF = "/portal/home.jsf";
    private HistoryNavigationHandler underTest;

    private FacesContext facesContext;

    @Inject
    private HistoryManager historyManager;

    @BeforeEach
    void setupUnderTest() {
        this.facesContext = FacesContext.getCurrentInstance();
        var application = facesContext.getApplication();
        underTest = new HistoryNavigationHandler(
                (ConfigurableNavigationHandler) application.getNavigationHandler());
        historyManager.addCurrentUriToHistory(CURRENT_VIEW);
        historyManager.addCurrentUriToHistory(SECOND_VIEW);
    }

    @Test
    void shouldHandleDefaultNavigation() {
        assertEquals(CURRENT_VIEW.getLogicalViewId(), historyManager.peekPrevious().getViewId());
        underTest.handleNavigation(facesContext, null, HomePage.OUTCOME);
        assertTrue(facesContext.getExternalContext().isResponseCommitted(), "Response should be committed");
        assertEquals(CURRENT_VIEW.getLogicalViewId(), historyManager.peekPrevious().getViewId(),
                "Should not change history");
    }

    @Test
    void shouldHandleBackNavigation() {
        assertEquals(CURRENT_VIEW.getLogicalViewId(), historyManager.peekPrevious().getViewId());
        underTest.handleNavigation(facesContext, null, "back");

        var response = (MockHttpServletResponse) facesContext.getExternalContext().getResponse();
        assertEquals(CURRENT_VIEW.getViewId(), response.getHeader("Location"), "Redirect URL mismatch");
        assertEquals(PORTAL_HOME_JSF, historyManager.peekPrevious().getViewId(), "Should change history");
    }

    @Test
    void shouldHandleDefaultNavigationCase() {
        assertEquals(CURRENT_VIEW.getLogicalViewId(), historyManager.peekPrevious().getViewId());
        var navigationCase = underTest.getNavigationCase(facesContext, null, HomePage.OUTCOME);
        assertNotNull(navigationCase);
        assertEquals(PORTAL_HOME_JSF, navigationCase.getToViewId(facesContext));
        assertEquals(CURRENT_VIEW.getLogicalViewId(), historyManager.peekPrevious().getViewId(),
                "Should not change history");
    }

    @Test
    void shouldHandleBackNavigationCase() {
        assertEquals(CURRENT_VIEW.getLogicalViewId(), historyManager.peekPrevious().getViewId());
        var navigationCase = underTest.getNavigationCase(facesContext, null, "back");
        assertNotNull(navigationCase);
        assertEquals(CURRENT_VIEW.getLogicalViewId(), navigationCase.getToViewId(facesContext));
        assertEquals(CURRENT_VIEW.getLogicalViewId(), historyManager.peekPrevious().getViewId(),
                "Should not change history");
    }
}