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
package de.cuioss.portal.ui.api.resources;

import de.cuioss.test.generator.Generators;
import de.cuioss.test.jsf.junit5.EnableJsfEnvironment;
import de.cuioss.test.jsf.util.JsfEnvironmentConsumer;
import de.cuioss.test.jsf.util.JsfEnvironmentHolder;
import de.cuioss.test.valueobjects.junit5.contracts.ShouldBeNotNull;
import de.cuioss.test.valueobjects.junit5.contracts.ShouldImplementEqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import static de.cuioss.portal.ui.api.resources.CacheableResource.HEADER_IF_NONE_MATCH;
import static org.junit.jupiter.api.Assertions.*;

@EnableJsfEnvironment
class CacheableResourceTest implements ShouldBeNotNull<CacheableResource>,
    ShouldImplementEqualsAndHashCode<CacheableResource>, JsfEnvironmentConsumer {

    @Setter
    @Getter
    private JsfEnvironmentHolder environmentHolder;

    private String etag;

    @BeforeEach
    void resetEtag() {
        etag = Generators.letterStrings(2, 10).next();
    }

    @Override
    public CacheableResource getUnderTest() {
        var resource = new CacheableResource() {

            @Override
            public URL getURL() {
                return null;
            }

            @Override
            public InputStream getInputStream() throws IOException {
                return null;
            }

            @Override
            protected String getETag() {
                return etag;
            }
        };
        resource.setLibraryName("library");
        resource.setResourceName("resource");
        return resource;
    }

    @Test
    void shouldDetectWhetherUANeedsNoUpdate() {
        var resource = getUnderTest();
        assertTrue(resource.userAgentNeedsUpdate(environmentHolder.getFacesContext()));

        environmentHolder.getRequestConfigDecorator().setRequestHeader(HEADER_IF_NONE_MATCH, HEADER_IF_NONE_MATCH);
        assertTrue(resource.userAgentNeedsUpdate(environmentHolder.getFacesContext()));

    }

    @Test
    void shouldDetectWhetherUATNeedsUpdate() {
        var resource = getUnderTest();

        environmentHolder.getRequestConfigDecorator().setRequestHeader(HEADER_IF_NONE_MATCH, etag);
        assertFalse(resource.userAgentNeedsUpdate(environmentHolder.getFacesContext()));

    }

    @Test
    void shouldHandleEtagIfPresent() {
        var resource = getUnderTest();
        var header = resource.getResponseHeaders();
        assertTrue(header.containsKey(CacheableResource.HEADER_E_TAG));
        assertTrue(header.containsValue(etag));

    }

    @Test
    void shouldDetermineResourcePath() {
        assertEquals("/jakarta.faces.resource/library/resource", getUnderTest().determineResourcePath());
    }

    @Test
    void shouldHandleEtagIfMotPresent() {
        var resource = new CacheableResource() {

            @Override
            public URL getURL() {
                return null;
            }

            @Override
            public InputStream getInputStream() throws IOException {
                return null;
            }

            @Override
            protected String getETag() {
                return null;
            }
        };

        var header = resource.getResponseHeaders();
        assertFalse(header.containsKey(CacheableResource.HEADER_E_TAG));
        assertFalse(header.containsValue(etag));
    }
}
