package de.cuioss.portal.ui.oauth;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.jboss.weld.junit5.auto.AddBeanClasses;
import org.junit.jupiter.api.BeforeEach;

import de.cuioss.portal.authentication.facade.PortalAuthenticationFacade;
import de.cuioss.portal.authentication.oauth.LoginPagePath;
import de.cuioss.portal.configuration.PortalConfigurationKeys;
import de.cuioss.portal.configuration.PortalConfigurationSource;
import de.cuioss.portal.core.test.mocks.configuration.PortalTestConfiguration;
import de.cuioss.portal.ui.runtime.application.view.HttpHeaderFilterImpl;
import de.cuioss.portal.ui.runtime.application.view.matcher.ViewMatcherProducer;
import de.cuioss.portal.ui.test.junit5.EnablePortalUiEnvironment;
import de.cuioss.test.jsf.component.AbstractComponentTest;

@EnablePortalUiEnvironment
@AddBeanClasses({ Oauth2AuthenticationFacadeMock.class, WrappedOauthFacadeImpl.class, HttpHeaderFilterImpl.class,
        ViewMatcherProducer.class, Oauth2ConfigurationProducerMock.class })
class OauthRenewComponentTest extends AbstractComponentTest<OauthRenewComponent> {

    @Produces
    @LoginPagePath
    private String loginUrl = "login.jsf";

    @Inject
    @PortalAuthenticationFacade
    private Oauth2AuthenticationFacadeMock oauth2AuthenticationFacadeMock;

    @Inject
    @PortalConfigurationSource
    private PortalTestConfiguration configuration;

    @BeforeEach
    void beforeTest() {
        configuration.put(PortalConfigurationKeys.PORTAL_SESSION_TIMEOUT, "20");
        configuration.fireEvent();
    }

}
