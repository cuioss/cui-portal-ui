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
package de.cuioss.portal.ui.api.resources;

import de.cuioss.test.jsf.junit5.EnableJsfEnvironment;
import de.cuioss.test.valueobjects.junit5.contracts.ShouldBeNotNull;
import de.cuioss.test.valueobjects.junit5.contracts.ShouldImplementEqualsAndHashCode;
import jakarta.faces.context.FacesContext;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@EnableJsfEnvironment
class CuiResourceTest implements ShouldBeNotNull<CuiResource>, ShouldImplementEqualsAndHashCode<CuiResource> {

    @Override
    public CuiResource getUnderTest() {
        var resource = new CuiResource() {

            @Override
            public boolean userAgentNeedsUpdate(FacesContext context) {
                return false;
            }

            @Override
            public URL getURL() {
                return null;
            }

            @Override
            public Map<String, String> getResponseHeaders() {
                return null;
            }

            @Override
            public InputStream getInputStream() throws IOException {
                return null;
            }
        };
        resource.setLibraryName("library");
        resource.setResourceName("resource");
        return resource;
    }

    @Test
    void shouldComputeRequestPath() {
        assertEquals("null/jakarta.faces.resource/resource?ln=library", getUnderTest().getRequestPath());
    }

}
