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
package de.cuioss.portal.ui.runtime.support;

import jakarta.faces.application.Resource;
import jakarta.faces.application.ResourceHandler;
import jakarta.faces.application.ResourceHandlerWrapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ResourceHandlerMock extends ResourceHandlerWrapper {

    private String resourceName;
    private String libraryName;

    public ResourceHandlerMock(ResourceHandler resourceHandler) {
        super(resourceHandler);
    }

    @Override
    public Resource createResource(String resourceName, String libraryName) {
        this.resourceName = resourceName;
        this.libraryName = libraryName;
        return super.createResource(resourceName, libraryName);
    }

    public void assertResourceCreated(String expectedResourceName, String expectedLibraryName) {
        assertEquals(expectedResourceName, resourceName);
        assertEquals(expectedLibraryName, libraryName);
    }

}
