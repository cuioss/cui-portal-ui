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
package de.cuioss.portal.ui.authentication.form;

import de.cuioss.jsf.api.common.view.ViewDescriptor;
import de.cuioss.jsf.api.common.view.ViewDescriptorImpl;
import de.cuioss.jsf.api.converter.nameprovider.LabeledKeyConverter;
import de.cuioss.jsf.test.MessageProducerMock;
import de.cuioss.portal.authentication.LoginEvent;
import de.cuioss.portal.authentication.PortalLoginEvent;
import de.cuioss.portal.authentication.facade.AuthenticationResults;
import de.cuioss.portal.authentication.facade.PortalAuthenticationFacade;
import de.cuioss.portal.configuration.PortalConfigurationKeys;
import de.cuioss.portal.configuration.PortalConfigurationSource;
import de.cuioss.portal.core.test.mocks.authentication.PortalAuthenticationFacadeMock;
import de.cuioss.portal.core.test.mocks.authentication.PortalTestUserProducer;
import de.cuioss.portal.core.test.mocks.configuration.PortalTestConfiguration;
import de.cuioss.portal.core.test.mocks.core.PortalClientStorageMock;
import de.cuioss.portal.ui.api.ui.pages.HomePage;
import de.cuioss.portal.ui.api.ui.pages.LoginPage;
import de.cuioss.portal.ui.api.ui.pages.LoginPageStrategy;
import de.cuioss.portal.ui.runtime.page.LoginPageClientStorageImpl;
import de.cuioss.portal.ui.runtime.page.LoginPageHistoryManagerProviderImpl;
import de.cuioss.portal.ui.runtime.page.PortalPagesConfiguration;
import de.cuioss.portal.ui.test.configuration.PortalNavigationConfiguration;
import de.cuioss.portal.ui.test.junit5.EnablePortalUiEnvironment;
import de.cuioss.portal.ui.test.mocks.PortalHistoryManagerMock;
import de.cuioss.portal.ui.test.tests.AbstractPageBeanTest;
import de.cuioss.test.jsf.config.ComponentConfigurator;
import de.cuioss.test.jsf.config.decorator.ComponentConfigDecorator;
import de.cuioss.tools.net.UrlParameter;
import de.cuioss.uimodel.nameprovider.LabeledKey;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.Produces;
import jakarta.enterprise.inject.spi.InjectionPoint;
import jakarta.inject.Inject;
import lombok.Getter;
import org.jboss.weld.junit5.auto.AddBeanClasses;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.omnifaces.cdi.Param;

import static de.cuioss.portal.core.test.mocks.authentication.PortalAuthenticationFacadeMock.*;
import static de.cuioss.tools.collect.CollectionLiterals.mutableList;
import static org.junit.jupiter.api.Assertions.*;

@EnablePortalUiEnvironment
@AddBeanClasses({PortalPagesConfiguration.class, PortalTestUserProducer.class, PortalClientStorageMock.class,
    LoginPageClientStorageImpl.class, LoginPageHistoryManagerProviderImpl.class})
class LoginPageBeanTest extends AbstractPageBeanTest<LoginPageBean> implements ComponentConfigurator {

    private static final String SOME_ERROR_KEY = "some.error";

    private static final String TEST_USER_STORE = SOME_LDAP_USER_STORE.getName();

    private static final String TEST_USER_NAME = "testUserName";

    @Inject
    @Getter
    private LoginPageBean underTest;

    @Inject
    @PortalAuthenticationFacade
    private PortalAuthenticationFacadeMock authenticationFacadeMock;

    @Inject
    private MessageProducerMock messageProducerMock;

    @Inject
    private PortalHistoryManagerMock portalHistoryManagerMock;

    @Inject
    @PortalConfigurationSource
    private PortalTestConfiguration configuration;

    private String username;
    private String userStore;

    private LoginEvent event;

    @Produces
    @Param
    public String getParameter(final InjectionPoint injectionPoint) {

        configuration.put(PortalConfigurationKeys.PAGES_LOGIN_DEFAULT_USER_STORE, SOME_OTHER_LDAP_USER_STORE.getName());
        configuration.fireEvent();

        final var name = injectionPoint.getMember().getName();
        return switch (name) {
            case LoginPage.KEY_USERNAME -> username;
            case LoginPage.KEY_USERSTORE -> userStore;
            default -> null;
        };

    }

    @Test
    void shouldUseUrlParameter() {
        username = TEST_USER_NAME;
        userStore = TEST_USER_STORE;

        assertEquals(username, underTest.getLoginCredentials().getUsername());
        assertEquals(userStore, underTest.getLoginCredentials().getUserStore());
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

    @Test
    void viewActionShouldLogoutOnLogoutStrategy() {
        configuration.put(PortalConfigurationKeys.PAGES_LOGIN_ENTER_STRATEGY,
            LoginPageStrategy.LOGOUT.getStrategyName());
        configuration.fireEvent();
        assertNull(underTest.initViewAction());
        authenticationFacadeMock.assertAuthenticated(false);
    }

    @Test
    @Disabled
        // No idea here
    void shouldUseConfiguredUserStoreAsDefault() {
        assertEquals(underTest.getLoginCredentials().getUserStore(), SOME_OTHER_LDAP_USER_STORE.getName(),
            "Wrong selected user store");
    }

    @Override
    public void configureComponents(final ComponentConfigDecorator decorator) {
        decorator.registerConverter(LabeledKeyConverter.class, LabeledKey.class);
    }

    void onLoginEventListener(@Observes @PortalLoginEvent final LoginEvent givenEvent) {
        event = givenEvent;
    }
}
