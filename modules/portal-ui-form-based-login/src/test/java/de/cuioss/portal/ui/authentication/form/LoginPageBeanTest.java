package de.cuioss.portal.ui.authentication.form;

import static de.cuioss.portal.core.test.mocks.authentication.PortalAuthenticationFacadeMock.DEFAULT_USER_STORE;
import static de.cuioss.portal.core.test.mocks.authentication.PortalAuthenticationFacadeMock.SOME_LDAP_USER_STORE;
import static de.cuioss.portal.core.test.mocks.authentication.PortalAuthenticationFacadeMock.SOME_OTHER_LDAP_USER_STORE;
import static de.cuioss.tools.collect.CollectionLiterals.mutableList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;

import org.jboss.weld.junit5.auto.AddBeanClasses;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.omnifaces.cdi.Param;

import de.cuioss.jsf.api.application.bundle.CuiResourceBundle;
import de.cuioss.jsf.api.common.view.ViewDescriptor;
import de.cuioss.jsf.api.common.view.ViewDescriptorImpl;
import de.cuioss.jsf.api.converter.nameprovider.LabeledKeyConverter;
import de.cuioss.jsf.test.mock.application.MirrorCuiRessourcBundle;
import de.cuioss.portal.authentication.LoginEvent;
import de.cuioss.portal.authentication.PortalLoginEvent;
import de.cuioss.portal.authentication.facade.AuthenticationResults;
import de.cuioss.portal.authentication.facade.PortalAuthenticationFacade;
import de.cuioss.portal.configuration.PortalConfigurationKeys;
import de.cuioss.portal.configuration.PortalConfigurationSource;
import de.cuioss.portal.core.storage.PortalSessionStorage;
import de.cuioss.portal.core.test.mocks.authentication.PortalAuthenticationFacadeMock;
import de.cuioss.portal.core.test.mocks.authentication.PortalTestUserProducer;
import de.cuioss.portal.core.test.mocks.configuration.PortalTestConfiguration;
import de.cuioss.portal.core.test.mocks.core.PortalClientStorageMock;
import de.cuioss.portal.core.test.mocks.core.PortalSessionStorageMock;
import de.cuioss.portal.ui.api.history.PortalHistoryManager;
import de.cuioss.portal.ui.api.message.PortalMessageProducer;
import de.cuioss.portal.ui.api.ui.pages.HomePage;
import de.cuioss.portal.ui.api.ui.pages.LoginPage;
import de.cuioss.portal.ui.api.ui.pages.LoginPageStrategy;
import de.cuioss.portal.ui.api.ui.pages.PortalCorePagesLogin;
import de.cuioss.portal.ui.runtime.page.LoginPageClientStorageImpl;
import de.cuioss.portal.ui.runtime.page.LoginPageHistoryManagerProviderImpl;
import de.cuioss.portal.ui.runtime.page.PortalPagesConfiguration;
import de.cuioss.portal.ui.test.configuration.PortalNavigationConfiguration;
import de.cuioss.portal.ui.test.junit5.EnablePortalUiEnvironment;
import de.cuioss.portal.ui.test.mocks.PortalHistoryManagerMock;
import de.cuioss.portal.ui.test.mocks.PortalMessageProducerMock;
import de.cuioss.portal.ui.test.tests.AbstractPageBeanTest;
import de.cuioss.test.jsf.config.BeanConfigurator;
import de.cuioss.test.jsf.config.ComponentConfigurator;
import de.cuioss.test.jsf.config.decorator.BeanConfigDecorator;
import de.cuioss.test.jsf.config.decorator.ComponentConfigDecorator;
import de.cuioss.tools.net.UrlParameter;
import de.cuioss.uimodel.nameprovider.LabeledKey;
import lombok.Getter;

@EnablePortalUiEnvironment
@AddBeanClasses({ PortalPagesConfiguration.class, PortalTestUserProducer.class, PortalClientStorageMock.class,
        LoginPageClientStorageImpl.class, LoginPageHistoryManagerProviderImpl.class })
class LoginPageBeanTest extends AbstractPageBeanTest<LoginPageBean> implements ComponentConfigurator, BeanConfigurator {

    private static final String SOME_ERROR_KEY = "some.error";

    private static final String TEST_USER_STORE = SOME_LDAP_USER_STORE.getName();

    private static final String TEST_USER_NAME = "testUserName";

    @Inject
    @PortalCorePagesLogin
    @Getter
    private LoginPageBean underTest;

    @Inject
    @PortalAuthenticationFacade
    private PortalAuthenticationFacadeMock authenticationFacadeMock;

    @Inject
    @PortalMessageProducer
    private PortalMessageProducerMock messageProducerMock;

    @Inject
    @PortalHistoryManager
    private PortalHistoryManagerMock portalHistoryManagerMock;

    @Inject
    @PortalSessionStorage
    private PortalSessionStorageMock mapStorage;

    @Inject
    @PortalConfigurationSource
    private PortalTestConfiguration configuration;

    private String username;
    private String userstore;

    private LoginEvent event;

    @Produces
    @Param
    public String getParameter(final InjectionPoint injectionPoint) {

        configuration.put(PortalConfigurationKeys.PAGES_LOGIN_DEFAULT_USERSTORE, SOME_OTHER_LDAP_USER_STORE.getName());
        configuration.fireEvent();

        final var name = injectionPoint.getMember().getName();
        switch (name) {
        case LoginPage.KEY_USERNAME:
            return username;
        case LoginPage.KEY_USERSTORE:
            return userstore;
        default:
            return null;
        }

    }

    @Test
    void shouldUseUrlParameter() {
        username = TEST_USER_NAME;
        userstore = TEST_USER_STORE;

        assertEquals(username, underTest.getLoginCredentials().getUsername());
        assertEquals(userstore, underTest.getLoginCredentials().getUserStore());
    }

    @Test
    void shouldUseUrlParameterAtDeepLinking() {

        authenticationFacadeMock.logout(null);
        getRequestConfigDecorator().setViewId(PortalNavigationConfiguration.VIEW_LOGIN_LOGICAL_VIEW_ID);

        // Mimic that Preferences was initially called
        final ViewDescriptor newDescriptor = ViewDescriptorImpl.builder()
                .withViewId(PortalNavigationConfiguration.VIEW_PREFERENCES_LOGICAL_VIEW_ID)
                .withUrlParameter(mutableList(new UrlParameter(LoginPage.KEY_USERNAME, TEST_USER_NAME),
                        new UrlParameter(LoginPage.KEY_USERSTORE, TEST_USER_STORE)))
                .withLogicalViewId(PortalNavigationConfiguration.VIEW_PREFERENCES_LOGICAL_VIEW_ID).build();
        portalHistoryManagerMock.addCurrentUriToHistory(newDescriptor);

        assertEquals(TEST_USER_NAME, underTest.getLoginCredentials().getUsername());
        assertEquals(TEST_USER_STORE, underTest.getLoginCredentials().getUserStore());
        underTest.getLoginCredentials().setPassword(TEST_USER_NAME);

        underTest.login();
        assertRedirect(PortalNavigationConfiguration.VIEW_PREFERENCES_LOGICAL_VIEW_ID);
        assertNotNull(event);
        assertEquals(LoginEvent.Action.LOGIN_SUCCESS, event.getAction());
    }

    @Test
    void shouldRedirectToRequestedUrlAfterSuccessfulLogin() {

        authenticationFacadeMock.logout(null);
        getRequestConfigDecorator().setViewId(PortalNavigationConfiguration.VIEW_LOGIN_LOGICAL_VIEW_ID);
        // Mimic that Preferences was initially called
        portalHistoryManagerMock.addCurrentUriToHistory(PortalNavigationConfiguration.DESCRIPTOR_PREFERENCES);

        underTest.getLoginCredentials().setPassword(PortalAuthenticationFacadeMock.USER);
        underTest.getLoginCredentials().setUsername(PortalAuthenticationFacadeMock.USER);

        underTest.login();
        assertRedirect(PortalNavigationConfiguration.VIEW_PREFERENCES_LOGICAL_VIEW_ID);
    }

    @Test
    void shouldReturnHomeAfterSuccessfulLogin() {

        authenticationFacadeMock.logout(null);
        getRequestConfigDecorator().setViewId(PortalNavigationConfiguration.VIEW_LOGIN_LOGICAL_VIEW_ID);
        underTest.getLoginCredentials().setPassword(PortalAuthenticationFacadeMock.USER);
        underTest.getLoginCredentials().setUsername(PortalAuthenticationFacadeMock.USER);

        underTest.login();
        assertRedirect(PortalNavigationConfiguration.VIEW_HOME_LOGICAL_VIEW_ID);
    }

    @Test
    void shouldFailOnInvalidLoginCredentials() {

        authenticationFacadeMock.logout(null);
        getRequestConfigDecorator().setViewId(PortalNavigationConfiguration.VIEW_LOGIN_LOGICAL_VIEW_ID);
        underTest.getLoginCredentials().setPassword(PortalAuthenticationFacadeMock.ADMIN);
        underTest.getLoginCredentials().setUsername(PortalAuthenticationFacadeMock.USER);
        assertNull(underTest.login());
        messageProducerMock.assertSingleGlobalMessageWithKeyPresent(AuthenticationResults.KEY_INVALID_CREDENTIALS);
        assertNotNull(event);
        assertEquals(LoginEvent.Action.LOGIN_FAILED, event.getAction());
    }

    @Test
    void shouldDetermineWhetherToDisplayUserStore() {
        assertTrue(underTest.isShouldDisplayUserStoreDropdown());
    }

    @Test
    void shouldHideUserStoreSelectionOnSingleEntry() {

        // simulate only one user store is available
        authenticationFacadeMock.setAvailableUserStores(mutableList(DEFAULT_USER_STORE));

        underTest.init();

        assertFalse(underTest.isShouldDisplayUserStoreDropdown(), "User store drop-down should be hidden");

        assertEquals(DEFAULT_USER_STORE.getName(), underTest.getLoginCredentials().getUserStore(),
                "On single user store the first must be selected as default");
    }

    @Test
    void viewActionShouldReturnHomeOnDefaultStrategy() {
        assertEquals(HomePage.OUTCOME, underTest.initViewAction());
        authenticationFacadeMock.assertAuthenticated(true);
    }

    @Test
    void shouldDisplayErrorText() {
        underTest.setErrorTextKey(SOME_ERROR_KEY);
        underTest.initViewAction();
        messageProducerMock.assertSingleGlobalMessageWithKeyPresent(SOME_ERROR_KEY);
    }

    void viewActionShouldLogoutOnLogoutStrategy() {
        configuration.put(PortalConfigurationKeys.PAGES_LOGIN_ENTER_STRATEGY,
                LoginPageStrategy.LOGOUT.getStrategyName());
        configuration.fireEvent();
        assertNull(underTest.initViewAction());
        authenticationFacadeMock.assertAuthenticated(false);
    }

    @Test
    @Disabled // No idea here
    void shouldUseConfiguredUserStoreAsDefault() {
        assertEquals(underTest.getLoginCredentials().getUserStore(), SOME_OTHER_LDAP_USER_STORE.getName(),
                "Wrong selected user store");
    }

    @Override
    public void configureComponents(final ComponentConfigDecorator decorator) {
        decorator.registerConverter(LabeledKeyConverter.class, LabeledKey.class);
    }

    @Override
    public void configureBeans(final BeanConfigDecorator decorator) {
        decorator.register(new MirrorCuiRessourcBundle(), CuiResourceBundle.BEAN_NAME);

    }

    void onLoginEventListener(@Observes @PortalLoginEvent final LoginEvent givenEvent) {
        event = givenEvent;
    }
}
