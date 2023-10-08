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

import static de.cuioss.portal.ui.test.configuration.PortalNavigationConfiguration.DESCRIPTOR_HOME;
import static de.cuioss.portal.ui.test.configuration.PortalNavigationConfiguration.DESCRIPTOR_LOGIN;
import static de.cuioss.portal.ui.test.configuration.PortalNavigationConfiguration.DESCRIPTOR_PREFERENCES;
import static de.cuioss.portal.ui.test.configuration.PortalNavigationConfiguration.VIEW_PREFERENCES_LOGICAL_VIEW_ID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Alternative;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.jboss.weld.exceptions.WeldException;
import org.jboss.weld.junit5.auto.AddBeanClasses;
import org.jboss.weld.junit5.auto.EnableAlternatives;
import org.junit.jupiter.api.Test;

import de.cuioss.jsf.api.common.view.ViewDescriptor;
import de.cuioss.portal.authentication.facade.PortalAuthenticationFacade;
import de.cuioss.portal.authentication.oauth.LoginPagePath;
import de.cuioss.portal.configuration.PortalConfigurationSource;
import de.cuioss.portal.core.test.mocks.configuration.PortalTestConfiguration;
import de.cuioss.portal.ui.api.ui.context.CuiCurrentView;
import de.cuioss.portal.ui.runtime.application.view.HttpHeaderFilterImpl;
import de.cuioss.portal.ui.runtime.application.view.matcher.ViewMatcherProducer;
import de.cuioss.portal.ui.test.junit5.EnablePortalUiEnvironment;
import de.cuioss.portal.ui.test.mocks.PortalHistoryManagerMock;
import de.cuioss.portal.ui.test.tests.AbstractPageBeanTest;
import de.cuioss.test.jsf.producer.ServletObjectsFromJSFContextProducer;
import lombok.Getter;

@EnablePortalUiEnvironment
@AddBeanClasses({ Oauth2AuthenticationFacadeMock.class, WrappedOauthFacadeImpl.class, HttpHeaderFilterImpl.class,
        ViewMatcherProducer.class, Oauth2ConfigurationProducerMock.class, ServletObjectsFromJSFContextProducer.class })
@EnableAlternatives(OauthLoginPageBeanTest.class)
class OauthLoginPageBeanTest extends AbstractPageBeanTest<OauthLoginPageBean> {

    @Inject
    @Getter
    private OauthLoginPageBean underTest;

    @Inject
    @PortalAuthenticationFacade
    private Oauth2AuthenticationFacadeMock oauth2AuthenticationFacadeMock;

    @Inject
    private Oauth2ConfigurationProducerMock oauth2ConfigurationProducerMock;

    @Inject
    private PortalHistoryManagerMock portalHistoryManagerMock;

    @Produces
    @CuiCurrentView
    @RequestScoped
    @Alternative
    ViewDescriptor getCurrentView() {
        return DESCRIPTOR_LOGIN;
    }

    @Inject
    @PortalConfigurationSource
    private PortalTestConfiguration configuration;

    @Produces
    @LoginPagePath
    private String loginUrl = "login.jsf";

    @Test
    void testUnauthorizedShouldCauseRedirect() {
        oauth2AuthenticationFacadeMock.setAuthenticated(false);
        var result = underTest.testLoginAndRedirectViewAction();
        assertNull(result);
        assertEquals("login.jsf", oauth2AuthenticationFacadeMock.getRedirectUrl());
    }

    @Test
    void testAuthenticatedShouldRedirectCorrect() {
        portalHistoryManagerMock.addCurrentUriToHistory(DESCRIPTOR_PREFERENCES);
        oauth2AuthenticationFacadeMock.setAuthenticated(false);
        underTest.testLoginAndRedirectViewAction();
        assertNotNull(oauth2AuthenticationFacadeMock.getRedirectUrl());
        oauth2AuthenticationFacadeMock.resetRedirectUrl();
        oauth2AuthenticationFacadeMock.setAuthenticated(true);
        var result = underTest.testLoginAndRedirectViewAction();
        assertNull(result);
        assertNull(oauth2AuthenticationFacadeMock.getRedirectUrl());
        assertRedirect(VIEW_PREFERENCES_LOGICAL_VIEW_ID);
    }

    @Test
    void testAuthenticatedShouldRedirectCorrectAtDeepLinkingAndLandingPage() {
        portalHistoryManagerMock.addCurrentUriToHistory(DESCRIPTOR_PREFERENCES);
        oauth2AuthenticationFacadeMock.setAuthenticated(false);
        underTest.testLoginViewAction();
        assertNotNull(oauth2AuthenticationFacadeMock.getRedirectUrl());
        oauth2AuthenticationFacadeMock.resetRedirectUrl();
        oauth2AuthenticationFacadeMock.setAuthenticated(true);
        var result = underTest.testLoginViewAction();
        assertNull(result);
        assertNull(oauth2AuthenticationFacadeMock.getRedirectUrl());
        assertRedirect(VIEW_PREFERENCES_LOGICAL_VIEW_ID);
    }

    @Test
    void testAuthenticatedShouldRedirectCorrectAtLandingPage() {
        portalHistoryManagerMock.addCurrentUriToHistory(DESCRIPTOR_HOME);
        oauth2AuthenticationFacadeMock.setAuthenticated(false);
        underTest.testLoginViewAction();
        assertNull(oauth2AuthenticationFacadeMock.getRedirectUrl());
    }

    @Test
    void testLoginTarget() {
        assertNotNull(underTest.loginTarget());
    }

    @Test
    void testNoConfig() {
        oauth2ConfigurationProducerMock.setConfiguration(null);
        assertThrows(WeldException.class, () -> {
            underTest.testLoginViewAction();
        });
    }
}
