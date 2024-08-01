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

import de.cuioss.portal.authentication.facade.PortalAuthenticationFacade;
import de.cuioss.portal.authentication.oauth.LoginPagePath;
import de.cuioss.portal.configuration.PortalConfigurationKeys;
import de.cuioss.portal.core.test.mocks.configuration.PortalTestConfiguration;
import de.cuioss.portal.ui.runtime.application.view.HttpHeaderFilterImpl;
import de.cuioss.portal.ui.runtime.application.view.matcher.ViewMatcherProducer;
import de.cuioss.portal.ui.test.junit5.EnablePortalUiEnvironment;
import de.cuioss.test.jsf.component.AbstractComponentTest;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import org.jboss.weld.junit5.auto.AddBeanClasses;
import org.junit.jupiter.api.BeforeEach;

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
    private PortalTestConfiguration configuration;

    @BeforeEach
    void beforeTest() {
        configuration.put(PortalConfigurationKeys.PORTAL_SESSION_TIMEOUT, "20");
        configuration.fireEvent();
    }

}
