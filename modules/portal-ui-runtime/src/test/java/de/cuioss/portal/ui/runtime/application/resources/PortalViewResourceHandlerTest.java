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
package de.cuioss.portal.ui.runtime.application.resources;

import de.cuioss.test.jsf.junit5.EnableJsfEnvironment;
import de.cuioss.test.jsf.mocks.CuiMockResourceHandler;
import de.cuioss.test.juli.LogAsserts;
import de.cuioss.test.juli.TestLogLevel;
import de.cuioss.test.juli.junit5.EnableTestLogger;
import jakarta.faces.context.FacesContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

@EnableJsfEnvironment
@EnableTestLogger
class PortalViewResourceHandlerTest {

    public static final String HELLO_WORLD_XHTML = "/hello/world.xhtml";
    public static final String NOT_THERE_XHTML = "/notThere.xhtml";

    private final CuiMockResourceHandler resourceHandler = new CuiMockResourceHandler();

    @Test
    void shouldIgnoreDefaultAndTemplates() {
        var facesContext = FacesContext.getCurrentInstance();
        PortalViewResourceHandler underTest = new PortalViewResourceHandler(resourceHandler);
        assertEquals(CuiMockResourceHandler.DUMMY_URL + "/", underTest.createViewResource(facesContext, "/").getURL().toString());
        assertEquals(CuiMockResourceHandler.DUMMY_URL + "/templates/hey", underTest.createViewResource(facesContext, "/templates/hey").getURL().toString());
    }

    @Test
    void shouldHandleAvailableViews() {
        var facesContext = FacesContext.getCurrentInstance();
        PortalViewResourceHandler underTest = new PortalViewResourceHandler(resourceHandler);
        final var result = underTest.createViewResource(facesContext, HELLO_WORLD_XHTML);
        assertInstanceOf(PortalViewResourceHolder.class, result);
        assertTrue(result.getURL().toString().endsWith(PortalViewResourceHandler.LOOKUP_PREFIX + HELLO_WORLD_XHTML), result.getURL().toString());
        final var cached = underTest.createViewResource(facesContext, HELLO_WORLD_XHTML);
        assertEquals(result, cached);
        assertSame(result, cached, "Should be the same instance");
    }

    @Test
    void shouldIgnoreMissingViews() {
        var facesContext = FacesContext.getCurrentInstance();
        PortalViewResourceHandler underTest = new PortalViewResourceHandler(resourceHandler);
        final var result = underTest.createViewResource(facesContext, NOT_THERE_XHTML);
        assertFalse(result instanceof PortalViewResourceHolder);
        assertEquals(CuiMockResourceHandler.DUMMY_URL + NOT_THERE_XHTML, result.getURL().toString());
        final var cached = underTest.createViewResource(facesContext, NOT_THERE_XHTML);
        assertEquals(CuiMockResourceHandler.DUMMY_URL + NOT_THERE_XHTML, cached.getURL().toString());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "/../../../etc/passwd",
            "/hello/../../META-INF/microprofile-config.properties",
            "/..%2f..%2fetc%2fpasswd",
            "/%2e%2e/secret.properties"
    })
    void shouldRejectPathTraversalAttempts(String maliciousPath) {
        var facesContext = FacesContext.getCurrentInstance();
        PortalViewResourceHandler underTest = new PortalViewResourceHandler(resourceHandler);
        // Path traversal attempts must fall through to the wrapped handler (not resolved from /portal/views)
        final var result = underTest.createViewResource(facesContext, maliciousPath);
        assertFalse(result instanceof PortalViewResourceHolder,
                "Path traversal attempt must not resolve to a PortalViewResourceHolder: " + maliciousPath);
        LogAsserts.assertLogMessagePresentContaining(TestLogLevel.WARN, "PORTAL-UI-RT-112");
    }
}