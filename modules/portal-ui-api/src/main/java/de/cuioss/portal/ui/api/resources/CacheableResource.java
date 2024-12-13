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

import jakarta.faces.application.ResourceHandler;
import jakarta.faces.context.FacesContext;

import java.util.HashMap;
import java.util.Map;

/**
 * Abstract resource allowing basic caching functionality.
 *
 * @author Matthias Walliczek
 */
public abstract class CacheableResource extends CuiResource {

    static final String HEADER_E_TAG = "ETag";

    static final String HEADER_IF_NONE_MATCH = "If-None-Match";

    /**
     * Generate a unique string for the current resource file version.
     *
     * @return a string unique for the current resource file version.
     */
    protected abstract String getETag();

    @Override
    public Map<String, String> getResponseHeaders() {
        final Map<String, String> responseHeaders = new HashMap<>();
        if (getETag() != null) {
            responseHeaders.put(HEADER_E_TAG, getETag());
        }
        return responseHeaders;
    }

    @Override
    public boolean userAgentNeedsUpdate(final FacesContext context) {
        final var requestHeaders = context.getExternalContext().getRequestHeaderMap();

        return !requestHeaders.containsKey(HEADER_IF_NONE_MATCH)
                || !getETag().equals(requestHeaders.get(HEADER_IF_NONE_MATCH));
    }

    /**
     * Create a resource path to be appended after a context path
     */
    protected String determineResourcePath() {
        return ResourceHandler.RESOURCE_IDENTIFIER + '/' +
                getLibraryName() +
                '/' +
                getResourceName();
    }
}
