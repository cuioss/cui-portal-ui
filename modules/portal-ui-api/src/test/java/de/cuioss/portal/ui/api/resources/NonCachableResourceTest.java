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

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.faces.context.FacesContext;

import org.junit.jupiter.api.Test;

import de.cuioss.test.valueobjects.junit5.contracts.ShouldBeNotNull;
import de.cuioss.test.valueobjects.junit5.contracts.ShouldImplementEqualsAndHashCode;

class NonCachableResourceTest
        implements ShouldBeNotNull<NonCachableResource>, ShouldImplementEqualsAndHashCode<NonCachableResource> {

    @Override
    public NonCachableResource getUnderTest() {
        return new NonCachableResource() {

            @Override
            public InputStream getInputStream() throws IOException {
                return null;
            }

            @Override
            public URL getURL() {
                return null;
            }

        };
    }

    @Test
    void shouldAddNoCacheHeader() {
        var resource = getUnderTest();
        assertTrue(resource.userAgentNeedsUpdate(FacesContext.getCurrentInstance()));

        var header = resource.getResponseHeaders();
        assertTrue(header.containsKey(NonCachableResource.HEADER_ACCEPT));
        assertTrue(header.containsKey(NonCachableResource.HEADER_CACHE_CONTROL));

        assertTrue(header.containsValue(NonCachableResource.PUBLIC));
        assertTrue(header.containsValue(NonCachableResource.MAX_AGE_0));
    }

}
