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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.jboss.weld.junit5.auto.AddBeanClasses;
import org.junit.jupiter.api.Test;

import de.cuioss.portal.authentication.LoginEvent;
import de.cuioss.portal.authentication.PortalLoginEvent;
import de.cuioss.portal.authentication.facade.PortalAuthenticationFacade;
import de.cuioss.portal.core.test.mocks.authentication.PortalAuthenticationFacadeMock;
import de.cuioss.portal.core.test.mocks.authentication.PortalTestUserProducer;
import de.cuioss.portal.ui.api.ui.pages.LoginPage;
import de.cuioss.portal.ui.api.ui.pages.PortalCorePagesLogout;
import de.cuioss.portal.ui.runtime.page.PortalPagesConfiguration;
import de.cuioss.portal.ui.test.junit5.EnablePortalUiEnvironment;
import de.cuioss.portal.ui.test.tests.AbstractPageBeanTest;
import lombok.Getter;

@EnablePortalUiEnvironment
@AddBeanClasses({ PortalPagesConfiguration.class, PortalTestUserProducer.class })
class LogoutPageBeanTest extends AbstractPageBeanTest<LogoutPageBean> {

    @Inject
    @PortalCorePagesLogout
    @Getter
    private LogoutPageBean underTest;

    @Inject
    @PortalAuthenticationFacade
    private PortalAuthenticationFacadeMock authenticationFacadeMock;

    private LoginEvent event;

    @Test
    void shouldLogoutOnViewAction() {
        assertNull(event);
        assertEquals(LoginPage.OUTCOME, underTest.logoutViewAction());
        authenticationFacadeMock.assertAuthenticated(false);
        assertNotNull(event);
        assertEquals(LoginEvent.Action.LOGOUT, event.getAction());
    }

    void onLoginEventListener(@Observes @PortalLoginEvent final LoginEvent givenEvent) {
        event = givenEvent;
    }

}
