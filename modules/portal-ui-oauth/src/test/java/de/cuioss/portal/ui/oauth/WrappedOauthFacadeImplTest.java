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

import de.cuioss.jsf.api.application.navigation.ViewIdentifier;
import de.cuioss.jsf.api.common.view.ViewDescriptor;
import de.cuioss.portal.authentication.facade.PortalAuthenticationFacade;
import de.cuioss.portal.authentication.oauth.LoginPagePath;
import de.cuioss.portal.core.test.mocks.configuration.PortalTestConfiguration;
import de.cuioss.portal.ui.api.context.CuiCurrentView;
import de.cuioss.portal.ui.runtime.application.view.HttpHeaderFilterImpl;
import de.cuioss.portal.ui.runtime.application.view.matcher.ViewMatcherProducer;
import de.cuioss.portal.ui.test.junit5.EnablePortalUiEnvironment;
import de.cuioss.portal.ui.test.mocks.PortalHistoryManagerMock;
import de.cuioss.test.valueobjects.junit5.contracts.ShouldBeNotNull;
import de.cuioss.tools.net.ParameterFilter;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.inject.Alternative;
import jakarta.enterprise.inject.Produces;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import lombok.Getter;
import org.apache.myfaces.test.mock.MockHttpServletResponse;
import org.jboss.weld.junit5.auto.AddBeanClasses;
import org.jboss.weld.junit5.auto.EnableAlternatives;
import org.junit.jupiter.api.Test;

import jakarta.servlet.http.HttpSession;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static de.cuioss.portal.ui.test.configuration.PortalNavigationConfiguration.*;
import static org.junit.jupiter.api.Assertions.*;

@EnablePortalUiEnvironment
@AddBeanClasses({Oauth2AuthenticationFacadeMock.class, HttpHeaderFilterImpl.class, ViewMatcherProducer.class,
        Oauth2ConfigurationProducerMock.class})
@EnableAlternatives(WrappedOauthFacadeImplTest.class)
class WrappedOauthFacadeImplTest implements ShouldBeNotNull<WrappedOauthFacadeImpl> {

    @Getter
    @PortalWrappedOauthFacade
    @Inject
    private WrappedOauthFacadeImpl underTest;

    @Inject
    @Getter
    private OauthLoginPageBean loginPage;

    @Produces
    @LoginPagePath
    private String loginUrl = "login.jsf";

    @Inject
    @PortalAuthenticationFacade
    private Oauth2AuthenticationFacadeMock oauth2AuthenticationFacadeMock;

    @Inject
    private PortalHistoryManagerMock portalHistoryManagerMock;

    @Inject
    private PortalTestConfiguration configuration;

    @Test
    void retrieveTokenShouldPass() {
        oauth2AuthenticationFacadeMock.setTokenToRetrieve("token");
        assertEquals("token", underTest.retrieveToken("abc"));
    }

    @Produces
    @CuiCurrentView
    @RequestScoped
    @Alternative
    ViewDescriptor getCurrentView() {
        return DESCRIPTOR_PREFERENCES;
    }

    @Test
    void retrieveTokenShouldTriggerNew() {
        var facesContext = FacesContext.getCurrentInstance();
        oauth2AuthenticationFacadeMock.setTokenToRetrieve(null);
        oauth2AuthenticationFacadeMock.setAuthenticated(true);
        assertNull(underTest.retrieveToken("abc"));
        assertNull(loginPage.testLoginViewAction());
        var response = (MockHttpServletResponse) facesContext.getExternalContext().getResponse();
        assertEquals(VIEW_PREFERENCES_LOGICAL_VIEW_ID, response.getHeader("Location"), "Redirect URL mismatch");
    }

    @Test
    void retrieveViewParametersShouldReturnEmptyMapWhenNoSession() {
        var result = underTest.retrieveViewParameters();
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void retrieveViewParametersShouldReturnStoredParametersAndRestoreMessages() {
        // Store parameters and messages in session
        Map<String, Serializable> params = new HashMap<>();
        params.put("key1", "value1");
        var session = getSession();
        session.setAttribute("oauthViewparameter", params);
        session.setAttribute("oauthMessages",
                List.of(new FacesMessage(FacesMessage.SEVERITY_WARN, "test warning", "detail")));

        var result = underTest.retrieveViewParameters();
        assertEquals("value1", result.get("key1"));
        // Parameters should be removed from session
        assertNull(session.getAttribute("oauthViewparameter"));
        // Messages should be removed from session
        assertNull(session.getAttribute("oauthMessages"));
    }

    @Test
    void handleMissingScopesExceptionShouldStoreParametersAndRequestToken() {
        oauth2AuthenticationFacadeMock.setTokenToRetrieve(null);
        oauth2AuthenticationFacadeMock.setAuthenticated(true);
        var exception = new MissingScopesException("extra_scope");
        Map<String, Serializable> params = new HashMap<>();
        params.put("param1", "val1");

        underTest.handleMissingScopesException(exception, params);

        // Verify parameters were stored in session
        var session = getSession();
        @SuppressWarnings("unchecked") var stored = (Map<String, Serializable>) session.getAttribute("oauthViewparameter");
        assertNotNull(stored);
        assertEquals("val1", stored.get("param1"));
    }

    @Test
    void handleMissingScopesExceptionWithInitialScopesShouldStoreAndRetrieve() {
        oauth2AuthenticationFacadeMock.setTokenToRetrieve(null);
        oauth2AuthenticationFacadeMock.setAuthenticated(true);
        var exception = new MissingScopesException("extra_scope");
        Map<String, Serializable> params = new HashMap<>();
        params.put("key", "value");

        underTest.handleMissingScopesException(exception, "openid profile", params);

        var session = getSession();
        assertNotNull(session.getAttribute("oauthViewparameter"));
    }

    @Test
    void preserveCurrentViewShouldNotOverwriteExisting() {
        // First call stores the view
        underTest.preserveCurrentView();
        var session = getSession();
        assertNotNull(session.getAttribute("oauthViewIdentifier"));

        // Set a different history and call again - should NOT overwrite
        portalHistoryManagerMock.addCurrentUriToHistory(DESCRIPTOR_HOME);
        underTest.preserveCurrentView();
        // The view identifier should still be the original one
        assertNotNull(session.getAttribute("oauthViewIdentifier"));
    }

    private HttpSession getSession() {
        return (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
    }

    @Test
    void retrieveTargetView() {
        // Create ViewIdentifier for Preferences page
        var preferences = ViewIdentifier.getFromViewDesciptor(DESCRIPTOR_PREFERENCES,
                new ParameterFilter(Collections.emptyList(), false));
        // Add to history manager
        portalHistoryManagerMock.addCurrentUriToHistory(DESCRIPTOR_PREFERENCES);
        // preserve the current (=preferences page) view
        underTest.preserveCurrentView();
        // Add home to history manager
        portalHistoryManagerMock.addCurrentUriToHistory(DESCRIPTOR_HOME);
        // retrieveTargetView should return preferences page
        assertEquals(preferences, underTest.retrieveTargetView());
        // Add home to history manager
        portalHistoryManagerMock.addCurrentUriToHistory(DESCRIPTOR_HOME);
        // retrieveTargetView should return home page at second call
        assertEquals(ViewIdentifier.getFromViewDesciptor(DESCRIPTOR_HOME,
                new ParameterFilter(Collections.emptyList(), false)), underTest.retrieveTargetView());
    }

}
