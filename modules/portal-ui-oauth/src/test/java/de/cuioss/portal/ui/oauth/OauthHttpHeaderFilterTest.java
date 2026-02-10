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
package de.cuioss.portal.ui.oauth;

import de.cuioss.portal.ui.test.junit5.EnablePortalUiEnvironment;
import de.cuioss.test.jsf.mocks.CuiMockHttpServletRequest;
import de.cuioss.test.valueobjects.junit5.contracts.ShouldBeNotNull;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import org.apache.myfaces.test.mock.MockExternalContext;
import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

import static de.cuioss.portal.ui.oauth.OauthHttpHeaderFilter.ACCESS_CONTROL_ALLOW_ORIGIN;
import static de.cuioss.portal.ui.oauth.OauthHttpHeaderFilter.ORIGIN;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@EnablePortalUiEnvironment
class OauthHttpHeaderFilterTest implements ShouldBeNotNull<OauthHttpHeaderFilter> {

    static final String FACES_GUEST_LOGIN_JSF = "/guest/login.jsf";
    private static final String ACCESS_CONTROL_ALLOW_CREDENTIALS = OauthHttpHeaderFilter.ACCESS_CONTROL_ALLOW_CREDENTIALS;
    private static final String ME = "https://locahost:8080/me";
    @Getter
    private final OauthHttpHeaderFilter underTest = new OauthHttpHeaderFilter();
    private final FilterChain filterChain = EasyMock.createNiceMock(FilterChain.class);

    @Test
    void shouldHandleHappyCase() throws Exception {
        var externalContext = FacesContext.getCurrentInstance().getExternalContext();
        var response = (HttpServletResponse) externalContext.getResponse();
        setRequestUrl(externalContext, FACES_GUEST_LOGIN_JSF);
        ((MockExternalContext) externalContext).addRequestHeader(ORIGIN, ME);
        HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();

        underTest.doFilter(request, response, filterChain);

        assertEquals(ME, response.getHeader(ACCESS_CONTROL_ALLOW_ORIGIN));
        assertEquals("true", response.getHeader(ACCESS_CONTROL_ALLOW_CREDENTIALS));
    }

    @Test
    void shouldNotSetHeaderOnMissingOriginHeader() throws Exception {
        var externalContext = FacesContext.getCurrentInstance().getExternalContext();
        var response = (HttpServletResponse) externalContext.getResponse();
        setRequestUrl(externalContext, FACES_GUEST_LOGIN_JSF);
        HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();

        underTest.doFilter(request, response, filterChain);
        assertNull(response.getHeader(ACCESS_CONTROL_ALLOW_ORIGIN));
        assertNull(response.getHeader(ACCESS_CONTROL_ALLOW_CREDENTIALS));
    }

    private void setRequestUrl(ExternalContext externalContext, String url) {
        var request = (CuiMockHttpServletRequest) externalContext.getRequest();
        request.setPathInfo(url);
    }
}
