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

import de.cuioss.tools.string.MoreStrings;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Enable the token renewing by setting the cors header for the login page to be
 * accessed via indirect Ajax call through oauth server.
 */
public class OauthHttpHeaderFilter implements Filter {

    static final String ACCESS_CONTROL_ALLOW_CREDENTIALS = "Access-Control-Allow-Credentials";

    static final String ACCESS_CONTROL_ALLOW_ORIGIN = "Access-Control-Allow-Origin";

    static final String ORIGIN = "Origin";

    static final String FACES_GUEST_LOGIN_JSF = "/guest/login.jsf";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String origin = ((HttpServletRequest) request).getHeader(ORIGIN);
        if (!MoreStrings.isEmpty(origin)) {
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            httpResponse.setHeader(ACCESS_CONTROL_ALLOW_ORIGIN, origin);
            httpResponse.setHeader(ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");
        }
        chain.doFilter(request, response);
    }
}
