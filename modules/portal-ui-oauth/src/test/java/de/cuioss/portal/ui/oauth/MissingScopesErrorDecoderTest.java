package de.cuioss.portal.ui.oauth;

import static javax.servlet.http.HttpServletResponse.SC_FORBIDDEN;
import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND;

import java.net.URI;
import java.net.URISyntaxException;

import javax.ws.rs.core.Response;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class MissingScopesErrorDecoderTest {

    @Test
    void testUpperCaseHeader() throws URISyntaxException {
        final var response = Response.created(new URI("http://localhost")).status(SC_FORBIDDEN, "forbidden")
                .header("WWW-Authenticate", "error=\"insufficient_scope\", scope=\"abc\"").build();
        var msed = new MissingScopesErrorDecoder();
        Assertions.assertTrue(msed.handles(SC_FORBIDDEN, response.getHeaders()));
        var result = msed.toThrowable(response);
        Assertions.assertNotNull(result);
        Assertions.assertEquals("abc", result.getMissingScopes());
    }

    @Test
    void testBearerError() throws URISyntaxException {
        final var response = Response.created(new URI("http://localhost")).status(SC_FORBIDDEN, "forbidden")
                .header("WWW-Authenticate", "Bearer error=\"insufficient_scope\", scope=\"abc\"").build();
        var msed = new MissingScopesErrorDecoder();
        Assertions.assertTrue(msed.handles(SC_FORBIDDEN, response.getHeaders()));
        var result = msed.toThrowable(response);
        Assertions.assertNotNull(result);
        Assertions.assertEquals("abc", result.getMissingScopes());
    }

    @Test
    void testLowerCaseHeader() throws URISyntaxException {
        final var response = Response.created(new URI("http://localhost")).status(SC_FORBIDDEN, "forbidden")
                .header("www-authenticate", "error=\"insufficient_scope\", scope=\"abc\"").build();
        var msed = new MissingScopesErrorDecoder();
        Assertions.assertTrue(msed.handles(SC_FORBIDDEN, response.getHeaders()));
        var result = msed.toThrowable(response);
        Assertions.assertNotNull(result);
        Assertions.assertEquals("abc", result.getMissingScopes());
    }

    @Test
    void test404() throws URISyntaxException {
        final var response = Response.created(new URI("http://localhost")).status(SC_NOT_FOUND, "not found")
                .build();
        var msed = new MissingScopesErrorDecoder();
        Assertions.assertFalse(msed.handles(SC_NOT_FOUND, response.getHeaders()));
        var result = msed.toThrowable(response);
        Assertions.assertNull(result);
    }
}
