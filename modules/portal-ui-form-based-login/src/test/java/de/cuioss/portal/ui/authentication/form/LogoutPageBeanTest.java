/*
 * Copyright © 2025 CUI-OpenSource-Software (info@cuioss.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.cuioss.portal.ui.authentication.form;

import de.cuioss.portal.authentication.LoginEvent;
import de.cuioss.portal.authentication.PortalLoginEvent;
import de.cuioss.portal.authentication.facade.PortalAuthenticationFacade;
import de.cuioss.portal.core.test.mocks.authentication.PortalAuthenticationFacadeMock;
import de.cuioss.portal.core.test.mocks.authentication.PortalTestUserProducer;
import de.cuioss.portal.ui.api.pages.LoginPage;
import de.cuioss.portal.ui.runtime.page.PortalPagesConfiguration;
import de.cuioss.portal.ui.test.junit5.EnablePortalUiEnvironment;
import de.cuioss.portal.ui.test.tests.AbstractPageBeanTest;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import lombok.Getter;
import org.jboss.weld.junit5.auto.AddBeanClasses;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@EnablePortalUiEnvironment
@AddBeanClasses({PortalPagesConfiguration.class, PortalTestUserProducer.class})
class LogoutPageBeanTest extends AbstractPageBeanTest<LogoutPageBean> {

    @Inject
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
