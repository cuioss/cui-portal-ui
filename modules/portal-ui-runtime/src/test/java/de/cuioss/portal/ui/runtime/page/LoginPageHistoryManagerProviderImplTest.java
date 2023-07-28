package de.cuioss.portal.ui.runtime.page;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Provider;

import org.jboss.weld.junit5.auto.AddBeanClasses;
import org.junit.jupiter.api.Test;

import de.cuioss.jsf.api.application.history.HistoryManager;
import de.cuioss.jsf.api.common.view.ViewDescriptor;
import de.cuioss.jsf.api.common.view.ViewDescriptorImpl;
import de.cuioss.portal.ui.api.history.PortalHistoryManager;
import de.cuioss.portal.ui.api.ui.pages.LoginPage;
import de.cuioss.portal.ui.test.junit5.EnablePortalUiEnvironment;
import de.cuioss.portal.ui.test.mocks.PortalHistoryManagerMock;
import de.cuioss.portal.ui.test.tests.AbstractPageBeanTest;
import de.cuioss.tools.net.UrlParameter;
import de.cuioss.uimodel.application.LoginCredentials;
import lombok.Getter;

@EnablePortalUiEnvironment
@AddBeanClasses({ PortalHistoryManagerMock.class })
class LoginPageHistoryManagerProviderImplTest extends AbstractPageBeanTest<LoginPageHistoryManagerProviderImpl> {

    @Inject
    @Getter
    private LoginPageHistoryManagerProviderImpl underTest;

    @Inject
    @PortalHistoryManager
    private Provider<HistoryManager> historyManagerProvider;

    @Test
    void shouldRetrieveEmptyIfNothingDefined() {
        final var loginCredentials = underTest.extractFromDeepLinkingUrlParameter(null, "");
        assertFalse(loginCredentials.isPresent(), "No Login Credentials expected.");
    }

    @Test
    void shouldUseInitialLoginCredentials() {
        final var userName = "anyUser";
        final var userStore = "userStore";
        final var expected = LoginCredentials.builder().userStore(userStore).username(userName).build();
        final var loginCredentials = underTest.extractFromDeepLinkingUrlParameter(userStore, userName);

        assertTrue(loginCredentials.isPresent());
        assertEquals(expected, loginCredentials.get());
    }

    @Test
    void shouldUseLoginCredentialsFromDeepLinkUrl() {

        final var expected = LoginCredentials.builder().userStore("some_store").username("some_name").build();
        prepareDeepLinkRequest(expected);
        final var loginCredentials = underTest.extractFromDeepLinkingUrlParameter(null, null);
        assertTrue(loginCredentials.isPresent());
        assertEquals(expected, loginCredentials.get());

        final var redirectTarget = underTest.getCurrentViewExcludeUserStoreAndUserName();
        assertTrue(redirectTarget.getUrlParameters().isEmpty(), "No url parameter expected");
    }

    private void prepareDeepLinkRequest(LoginCredentials expected) {
        final List<UrlParameter> urlParameter = new ArrayList<>();
        final var userStoreParam = new UrlParameter(LoginPage.KEY_USERSTORE, expected.getUserStore());
        final var userNameParam = new UrlParameter(LoginPage.KEY_USERNAME, expected.getUsername());
        urlParameter.add(userStoreParam);
        urlParameter.add(userNameParam);

        final ViewDescriptor requestedView = ViewDescriptorImpl.builder().withLogicalViewId("deep_link_page")
                .withViewId("deep_link_page.jsf").withUrlParameter(urlParameter).build();

        underTest.getWrapped().addCurrentUriToHistory(requestedView);
    }

}
