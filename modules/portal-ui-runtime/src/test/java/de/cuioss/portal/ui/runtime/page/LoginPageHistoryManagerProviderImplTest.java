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
package de.cuioss.portal.ui.runtime.page;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import jakarta.inject.Inject;
import jakarta.inject.Provider;

import org.jboss.weld.junit5.auto.AddBeanClasses;
import org.junit.jupiter.api.Test;

import de.cuioss.jsf.api.common.view.ViewDescriptor;
import de.cuioss.jsf.api.common.view.ViewDescriptorImpl;
import de.cuioss.portal.ui.api.history.HistoryManager;
import de.cuioss.portal.ui.api.ui.pages.LoginPage;
import de.cuioss.portal.ui.runtime.application.history.DefaultHistoryConfiguration;
import de.cuioss.portal.ui.runtime.application.history.HistoryManagerBean;
import de.cuioss.portal.ui.runtime.application.view.matcher.ViewMatcherProducer;
import de.cuioss.portal.ui.test.junit5.EnablePortalUiEnvironment;
import de.cuioss.portal.ui.test.tests.AbstractPageBeanTest;
import de.cuioss.tools.net.UrlParameter;
import de.cuioss.uimodel.application.LoginCredentials;
import lombok.Getter;

@EnablePortalUiEnvironment
@AddBeanClasses({ HistoryManagerBean.class, DefaultHistoryConfiguration.class, ViewMatcherProducer.class })
class LoginPageHistoryManagerProviderImplTest extends AbstractPageBeanTest<LoginPageHistoryManagerProviderImpl> {

    @Inject
    @Getter
    private LoginPageHistoryManagerProviderImpl underTest;

    @Inject
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
