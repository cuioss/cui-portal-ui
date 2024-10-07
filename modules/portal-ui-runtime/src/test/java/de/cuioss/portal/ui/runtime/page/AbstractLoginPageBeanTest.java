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

import de.cuioss.portal.core.test.mocks.authentication.PortalAuthenticationFacadeMock;
import de.cuioss.portal.ui.test.junit5.EnablePortalUiEnvironment;
import de.cuioss.portal.ui.test.tests.AbstractPageBeanTest;
import jakarta.inject.Inject;
import lombok.Getter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNull;

@EnablePortalUiEnvironment
class AbstractLoginPageBeanTest extends AbstractPageBeanTest<TestLoginPage> {

    @Inject
    @Getter
    private TestLoginPage underTest;

    private final PortalAuthenticationFacadeMock facadeMock = new PortalAuthenticationFacadeMock();

    @BeforeEach
    void before() {
        facadeMock.init();
        underTest.setUserInfo(facadeMock.retrieveCurrentAuthenticationContext(null));
    }

    @Test
    void shouldHandlePositiveLogin() {
        assertNull(underTest.doLogin());
    }

    @Test
    void shouldReactOnErrors() {
        underTest.setSimulateLoginError(true);
        assertNull(underTest.doLogin());
    }

}
