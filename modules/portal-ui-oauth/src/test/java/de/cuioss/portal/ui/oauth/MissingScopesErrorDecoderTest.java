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

import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;

import java.net.URI;

import static jakarta.servlet.http.HttpServletResponse.SC_FORBIDDEN;
import static jakarta.servlet.http.HttpServletResponse.SC_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.*;

class MissingScopesErrorDecoderTest {

    @Test
    void upperCaseHeader() throws Exception {
        final var response = Response.created(new URI("http://localhost")).status(SC_FORBIDDEN, "forbidden")
                .header("WWW-Authenticate", "error=\"insufficient_scope\", scope=\"abc\"").build();
        var msed = new MissingScopesErrorDecoder();
        assertTrue(msed.handles(SC_FORBIDDEN, response.getHeaders()));
        var result = msed.toThrowable(response);
        assertNotNull(result);
        assertEquals("abc", result.getMissingScopes());
    }

    @Test
    void bearerError() throws Exception {
        final var response = Response.created(new URI("http://localhost")).status(SC_FORBIDDEN, "forbidden")
                .header("WWW-Authenticate", "Bearer error=\"insufficient_scope\", scope=\"abc\"").build();
        var msed = new MissingScopesErrorDecoder();
        assertTrue(msed.handles(SC_FORBIDDEN, response.getHeaders()));
        var result = msed.toThrowable(response);
        assertNotNull(result);
        assertEquals("abc", result.getMissingScopes());
    }

    @Test
    void lowerCaseHeader() throws Exception {
        final var response = Response.created(new URI("http://localhost")).status(SC_FORBIDDEN, "forbidden")
                .header("www-authenticate", "error=\"insufficient_scope\", scope=\"abc\"").build();
        var msed = new MissingScopesErrorDecoder();
        assertTrue(msed.handles(SC_FORBIDDEN, response.getHeaders()));
        var result = msed.toThrowable(response);
        assertNotNull(result);
        assertEquals("abc", result.getMissingScopes());
    }

    @Test
    void shouldReturnNullFor403WithoutWwwAuthenticateHeader() throws Exception {
        final var response = Response.created(new URI("http://localhost")).status(SC_FORBIDDEN, "forbidden").build();
        var msed = new MissingScopesErrorDecoder();
        assertFalse(msed.handles(SC_FORBIDDEN, response.getHeaders()));
        assertNull(msed.toThrowable(response));
    }

    @Test
    void shouldReturnNullFor403WithDifferentError() throws Exception {
        final var response = Response.created(new URI("http://localhost")).status(SC_FORBIDDEN, "forbidden")
                .header("WWW-Authenticate", "error=\"invalid_token\"").build();
        var msed = new MissingScopesErrorDecoder();
        assertTrue(msed.handles(SC_FORBIDDEN, response.getHeaders()));
        assertNull(msed.toThrowable(response));
    }

    @Test
    void shouldReturnNullWhenScopeEntryMissing() throws Exception {
        final var response = Response.created(new URI("http://localhost")).status(SC_FORBIDDEN, "forbidden")
                .header("WWW-Authenticate", "error=\"insufficient_scope\"").build();
        var msed = new MissingScopesErrorDecoder();
        assertTrue(msed.handles(SC_FORBIDDEN, response.getHeaders()));
        assertNull(msed.toThrowable(response));
    }

    @Test
    void test404() throws Exception {
        final var response = Response.created(new URI("http://localhost")).status(SC_NOT_FOUND, "not found").build();
        var msed = new MissingScopesErrorDecoder();
        assertFalse(msed.handles(SC_NOT_FOUND, response.getHeaders()));
        var result = msed.toThrowable(response);
        assertNull(result);
    }
}
