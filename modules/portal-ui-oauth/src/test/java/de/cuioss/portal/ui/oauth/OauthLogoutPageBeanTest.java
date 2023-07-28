package de.cuioss.portal.ui.oauth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.jboss.weld.junit5.auto.AddBeanClasses;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.cuioss.portal.authentication.facade.PortalAuthenticationFacade;
import de.cuioss.portal.authentication.oauth.LoginPagePath;
import de.cuioss.portal.authentication.oauth.impl.Oauth2ConfigurationImpl;
import de.cuioss.portal.configuration.PortalConfigurationKeys;
import de.cuioss.portal.configuration.PortalConfigurationSource;
import de.cuioss.portal.core.test.mocks.authentication.PortalTestUserProducer;
import de.cuioss.portal.core.test.mocks.configuration.PortalTestConfiguration;
import de.cuioss.portal.ui.api.ui.pages.PortalCorePagesLogout;
import de.cuioss.portal.ui.runtime.application.view.matcher.ViewMatcherProducer;
import de.cuioss.portal.ui.test.junit5.EnablePortalUiEnvironment;
import de.cuioss.portal.ui.test.tests.AbstractPageBeanTest;
import de.cuioss.test.jsf.producer.ServletObjectsFromJSFContextProducers;
import lombok.Getter;

@EnablePortalUiEnvironment
@AddBeanClasses({ Oauth2AuthenticationFacadeMock.class, ViewMatcherProducer.class,
        Oauth2ConfigurationProducerMock.class, PortalTestUserProducer.class,
        ServletObjectsFromJSFContextProducers.class })
class OauthLogoutPageBeanTest extends AbstractPageBeanTest<OauthLogoutPageBean> {

    @Inject
    @PortalCorePagesLogout
    @Getter
    private OauthLogoutPageBean underTest;

    @Produces
    @LoginPagePath
    private String loginUrl = "login.jsf";

    @Inject
    @PortalConfigurationSource
    private PortalTestConfiguration configuration;

    @Inject
    @PortalAuthenticationFacade
    private Oauth2AuthenticationFacadeMock facadeMock;

    @Inject
    private Oauth2ConfigurationProducerMock oAuthConfiguration;

    @BeforeEach
    void beforeTest() {
        configuration.put(PortalConfigurationKeys.PORTAL_SESSION_TIMEOUT, "20");
        configuration.fireEvent();
    }

    @Test
    void shouldLogoutWithEmptyConfig() {
        assertEquals("login", underTest.logoutViewAction());
    }

    @Test
    void shouldLogoutWithLogoutUrl() {
        facadeMock.setClientLogoutUrl("https://client-logout-uri");
        oAuthConfiguration.setConfiguration(Oauth2ConfigurationImpl.builder().logoutUri("http://logout").build());
        assertNull(underTest.logoutViewAction());
        assertRedirect("https://client-logout-uri");
    }

    @Test
    void logoutWithRedirectUri() {
        facadeMock.setClientLogoutUrl("https://client-logout-uri");
        oAuthConfiguration.setConfiguration(Oauth2ConfigurationImpl.builder().logoutUri("http://logout")
                .logoutRedirectParamName("post_logout_test_redirect_uri")
                .postLogoutRedirectUri("https://post.logout.url").build());
        assertNull(underTest.logoutViewAction());
        assertRedirect("https://client-logout-uri?post_logout_test_redirect_uri=https%3A%2F%2Fpost.logout.url");
    }
}
