package de.cuioss.portal.ui.oauth;

import static de.cuioss.portal.ui.test.configuration.PortalNavigationConfiguration.DESCRIPTOR_HOME;
import static de.cuioss.portal.ui.test.configuration.PortalNavigationConfiguration.DESCRIPTOR_PREFERENCES;
import static de.cuioss.portal.ui.test.configuration.PortalNavigationConfiguration.VIEW_PREFERENCES_LOGICAL_VIEW_ID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Collections;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Alternative;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.jboss.weld.junit5.auto.AddBeanClasses;
import org.jboss.weld.junit5.auto.EnableAlternatives;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.cuioss.jsf.api.application.navigation.ViewIdentifier;
import de.cuioss.jsf.api.common.view.ViewDescriptor;
import de.cuioss.jsf.api.servlet.ServletAdapterUtil;
import de.cuioss.portal.authentication.facade.PortalAuthenticationFacade;
import de.cuioss.portal.authentication.oauth.LoginPagePath;
import de.cuioss.portal.configuration.PortalConfigurationSource;
import de.cuioss.portal.core.test.mocks.configuration.PortalTestConfiguration;
import de.cuioss.portal.ui.api.history.PortalHistoryManager;
import de.cuioss.portal.ui.api.ui.context.CuiCurrentView;
import de.cuioss.portal.ui.runtime.application.view.HttpHeaderFilterImpl;
import de.cuioss.portal.ui.runtime.application.view.matcher.ViewMatcherProducer;
import de.cuioss.portal.ui.test.junit5.EnablePortalUiEnvironment;
import de.cuioss.portal.ui.test.mocks.PortalHistoryManagerMock;
import de.cuioss.test.jsf.util.JsfEnvironmentConsumer;
import de.cuioss.test.jsf.util.JsfEnvironmentHolder;
import de.cuioss.test.valueobjects.junit5.contracts.ShouldBeNotNull;
import de.cuioss.tools.net.ParameterFilter;
import lombok.Getter;
import lombok.Setter;

@EnablePortalUiEnvironment
@AddBeanClasses({ Oauth2AuthenticationFacadeMock.class, HttpHeaderFilterImpl.class, ViewMatcherProducer.class,
        Oauth2ConfigurationProducerMock.class })
@EnableAlternatives(WrappedOauthFacadeImplTest.class)
class WrappedOauthFacadeImplTest implements ShouldBeNotNull<WrappedOauthFacadeImpl>, JsfEnvironmentConsumer {

    @Setter
    @Getter
    private JsfEnvironmentHolder environmentHolder;

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

    @Produces
    HttpServletRequest servletRequest;

    @Inject
    @PortalAuthenticationFacade
    private Oauth2AuthenticationFacadeMock oauth2AuthenticationFacadeMock;

    @Inject
    @PortalHistoryManager
    private PortalHistoryManagerMock portalHistoryManagerMock;

    @Inject
    @PortalConfigurationSource
    private PortalTestConfiguration configuration;

    @BeforeEach
    void beforeTest() {
        servletRequest = ServletAdapterUtil.getRequest(getFacesContext());
        configuration.fireEvent();
    }

    @Test
    void testRetrieveTokenShouldPass() {
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
    void testRetrieveTokenShouldTriggerNew() {
        oauth2AuthenticationFacadeMock.setTokenToRetrieve(null);
        oauth2AuthenticationFacadeMock.setAuthenticated(true);
        assertNull(underTest.retrieveToken("abc"));
        assertNull(loginPage.testLoginViewAction());
        assertRedirect(VIEW_PREFERENCES_LOGICAL_VIEW_ID);
    }

    @Test
    void testRetrieveTargetView() {
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
