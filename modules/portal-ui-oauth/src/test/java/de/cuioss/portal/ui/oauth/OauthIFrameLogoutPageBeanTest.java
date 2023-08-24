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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.jboss.weld.junit5.auto.AddBeanClasses;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.cuioss.portal.authentication.AuthenticatedUserInfo;
import de.cuioss.portal.authentication.LoginEvent;
import de.cuioss.portal.authentication.PortalLoginEvent;
import de.cuioss.portal.authentication.PortalUser;
import de.cuioss.portal.authentication.facade.PortalAuthenticationFacade;
import de.cuioss.portal.authentication.model.BaseAuthenticatedUserInfo;
import de.cuioss.portal.core.test.mocks.authentication.PortalAuthenticationFacadeMock;
import de.cuioss.portal.ui.test.junit5.EnablePortalUiEnvironment;
import de.cuioss.portal.ui.test.tests.AbstractPageBeanTest;
import lombok.Getter;

@EnablePortalUiEnvironment
@AddBeanClasses({ PortalAuthenticationFacadeMock.class })
class OauthIFrameLogoutPageBeanTest extends AbstractPageBeanTest<OauthIFrameLogoutPageBean> {

    private static final String USER = "user";

    @Inject
    @Getter
    private OauthIFrameLogoutPageBean underTest;

    @Inject
    @PortalAuthenticationFacade
    private PortalAuthenticationFacadeMock facade;

    @Produces
    @PortalUser
    private AuthenticatedUserInfo user;

    private LoginEvent logoutEvent;

    @BeforeEach
    void resetLogoutEvent() {
        logoutEvent = null;
        user = BaseAuthenticatedUserInfo.builder().displayName(USER).identifier(USER).qualifiedIdentifier(USER)
                .authenticated(true).build();
    }

    @Test
    void shouldCallLogoutForAuthenticated() {
        underTest.logoutViewAction();
        assertFalse(facade.retrieveCurrentAuthenticationContext(null).isAuthenticated());
        assertNotNull(logoutEvent);
    }

    @Test
    void shouldCallLogoutForNotAuthenticated() {

        user = BaseAuthenticatedUserInfo.builder().displayName(USER).identifier(USER).qualifiedIdentifier(USER)
                .authenticated(false).build();
        underTest.logoutViewAction();
        assertFalse(facade.retrieveCurrentAuthenticationContext(null).isAuthenticated());
        assertNull(logoutEvent);
    }

    void listener(@Observes @PortalLoginEvent LoginEvent event) {
        this.logoutEvent = event;
    }
}
