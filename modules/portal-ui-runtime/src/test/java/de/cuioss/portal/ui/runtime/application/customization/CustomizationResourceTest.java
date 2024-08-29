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
package de.cuioss.portal.ui.runtime.application.customization;

import de.cuioss.portal.configuration.PortalConfigurationKeys;
import de.cuioss.portal.core.test.mocks.configuration.PortalTestConfiguration;
import de.cuioss.portal.ui.test.junit5.EnablePortalUiEnvironment;
import de.cuioss.test.valueobjects.junit5.contracts.ShouldBeNotNull;
import de.cuioss.tools.string.MoreStrings;
import jakarta.inject.Inject;
import lombok.Getter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@EnablePortalUiEnvironment
class CustomizationResourceTest implements ShouldBeNotNull<CustomizationResource> {

    @Getter
    private CustomizationResource underTest;

    @Inject
    private PortalTestConfiguration configuration;

    @BeforeEach
    void before() throws IOException {
        configuration.update(PortalConfigurationKeys.RESOURCE_MAXAGE, "60");

        underTest = new CustomizationResource(File.createTempFile("application-default", ".css"),
                "application-default.css", "vendor", "application/css");
    }

    @Test
    void testCustomizationResource() throws IOException {
        assertFalse(MoreStrings.isEmpty(underTest.getETag()));
        var headers = underTest.getResponseHeaders();
        assertNotNull(headers);
        assertTrue(headers.containsKey("Expires"));

        assertFalse(MoreStrings.isEmpty(headers.get("Expires")));
        assertTrue(headers.containsKey("Last-Modified"));
        assertFalse(MoreStrings.isEmpty(headers.get("Last-Modified")));
        assertNotNull(underTest.getInputStream());
        assertEquals("vendor", underTest.getLibraryName());
        assertEquals("application-default.css", underTest.getResourceName());
        assertEquals("application/css", underTest.getContentType());
        assertEquals("/jakarta.faces.resource/vendor/application-default.css", underTest.getRequestPath());
    }

}
