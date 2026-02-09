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

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PortalPathValidatorTest {

    private final PortalPathValidator underTest = new PortalPathValidator();

    @ParameterizedTest
    @ValueSource(strings = {
            "/hello/world.xhtml",
            "/guest/login.xhtml",
            "/account/preferences.xhtml",
            "/guest/401.xhtml",
            "/some-page.xhtml"
    })
    void shouldAcceptValidPaths(String path) {
        assertTrue(underTest.isValidPath(path), "Should accept valid path: " + path);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "/../../../etc/passwd",
            "/hello/../../etc/passwd",
            "/..%2f..%2f..%2fetc%2fpasswd",
            "/%2e%2e/%2e%2e/etc/passwd",
            "/hello/../../../META-INF/microprofile-config.properties",
            "/../META-INF/persistence.xml",
            "/..\\..\\etc\\passwd",
            "/hello%00world.xhtml",
            "/hello\u0000world.xhtml"
    })
    void shouldRejectMaliciousPaths(String path) {
        assertFalse(underTest.isValidPath(path), "Should reject malicious path: " + path);
    }

    @Test
    void shouldRejectNull() {
        assertFalse(underTest.isValidPath(null));
    }

    @Test
    void shouldRejectEmpty() {
        assertFalse(underTest.isValidPath(""));
    }
}
