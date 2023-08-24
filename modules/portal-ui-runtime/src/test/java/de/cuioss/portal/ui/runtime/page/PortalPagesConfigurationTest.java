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
import static org.junit.jupiter.api.Assertions.assertNotNull;

import javax.inject.Inject;

import org.junit.jupiter.api.Test;

import de.cuioss.portal.configuration.PortalConfigurationKeys;
import de.cuioss.portal.ui.api.ui.pages.LoginPageStrategy;
import de.cuioss.portal.ui.test.junit5.EnablePortalUiEnvironment;
import de.cuioss.portal.ui.test.tests.AbstractPageBeanTest;
import lombok.Getter;

@EnablePortalUiEnvironment
class PortalPagesConfigurationTest extends AbstractPageBeanTest<PortalPagesConfiguration> {

    @Inject
    @Getter
    private PortalPagesConfiguration underTest;

    /**
     * Tests, whether the default values are correct. This is handy for detecting
     * accidental changes in the configuration
     */
    @Test
    void shouldProvideDefaultConfiguration() {
        assertNotNull(underTest);
        assertEquals(LoginPageStrategy.GOTO_HOME, underTest.getLoginPageStrategy());
    }

    @Test
    void shouldReconfigure() {

        // Should not reconfigure
        configuration.fireEvent();
        assertEquals(LoginPageStrategy.GOTO_HOME, underTest.getLoginPageStrategy());

        // Should not reconfigure
        configuration.fireEvent(PortalConfigurationKeys.PAGES_LOGIN_ENTER_STRATEGY, LoginPageStrategy.GOTO_HOME.name());
        assertEquals(LoginPageStrategy.GOTO_HOME, underTest.getLoginPageStrategy());

        // Should reconfigure
        configuration.fireEvent(PortalConfigurationKeys.PAGES_LOGIN_ENTER_STRATEGY, LoginPageStrategy.LOGOUT.name());
        assertEquals(LoginPageStrategy.LOGOUT, underTest.getLoginPageStrategy());
    }
}
