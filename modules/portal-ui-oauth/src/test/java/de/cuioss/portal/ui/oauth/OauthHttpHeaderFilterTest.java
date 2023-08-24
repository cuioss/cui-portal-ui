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

import static de.cuioss.portal.ui.oauth.OauthHttpHeaderFilter.ACCESS_CONTROL_ALLOW_ORIGIN;
import static de.cuioss.portal.ui.oauth.OauthHttpHeaderFilter.FACES_GUEST_LOGIN_JSF;
import static de.cuioss.portal.ui.oauth.OauthHttpHeaderFilter.ORIGIN;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import javax.inject.Inject;

import org.junit.jupiter.api.Test;

import de.cuioss.portal.ui.test.junit5.EnablePortalUiEnvironment;
import de.cuioss.test.jsf.mocks.CuiMockHttpServletRequest;
import de.cuioss.test.jsf.util.JsfEnvironmentConsumer;
import de.cuioss.test.jsf.util.JsfEnvironmentHolder;
import de.cuioss.test.valueobjects.junit5.contracts.ShouldBeNotNull;
import lombok.Getter;
import lombok.Setter;

@EnablePortalUiEnvironment
class OauthHttpHeaderFilterTest implements ShouldBeNotNull<OauthHttpHeaderFilter>, JsfEnvironmentConsumer {

    private static final String ACCESS_CONTROL_ALLOW_CREDENTIALS = OauthHttpHeaderFilter.ACCESS_CONTROL_ALLOW_CREDENTIALS;

    private static final String ME = "https://locahost:8080/me";

    @Setter
    @Getter
    private JsfEnvironmentHolder environmentHolder;

    @Inject
    @Getter
    private OauthHttpHeaderFilter underTest;

    @Test
    void shouldHandleHappyCase() {
        var response = environmentHolder.getResponse();
        setRequestUrl(FACES_GUEST_LOGIN_JSF);
        environmentHolder.getRequestConfigDecorator().setRequestHeader(ORIGIN, ME);
        underTest.onCreate(response);
        assertEquals(ME, response.getHeader(ACCESS_CONTROL_ALLOW_ORIGIN));
        assertEquals("true", response.getHeader(ACCESS_CONTROL_ALLOW_CREDENTIALS));
    }

    @Test
    void shouldNotSetHeaderOnInvalidUrl() {
        var response = environmentHolder.getResponse();
        setRequestUrl(ME);
        environmentHolder.getRequestConfigDecorator().setRequestHeader(ORIGIN, ME);
        underTest.onCreate(response);
        assertNull(response.getHeader(ACCESS_CONTROL_ALLOW_ORIGIN));
        assertNull(response.getHeader(ACCESS_CONTROL_ALLOW_CREDENTIALS));
    }

    @Test
    void shouldNotSetHeaderOnMissingOriginHeader() {
        var response = environmentHolder.getResponse();
        setRequestUrl(FACES_GUEST_LOGIN_JSF);
        underTest.onCreate(response);
        assertNull(response.getHeader(ACCESS_CONTROL_ALLOW_ORIGIN));
        assertNull(response.getHeader(ACCESS_CONTROL_ALLOW_CREDENTIALS));
    }

    private void setRequestUrl(String url) {
        var request = (CuiMockHttpServletRequest) environmentHolder.getExternalContext().getRequest();
        request.setPathInfo(url);
    }
}
