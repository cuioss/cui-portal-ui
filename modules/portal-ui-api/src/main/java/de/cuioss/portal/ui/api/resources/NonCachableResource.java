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

import jakarta.faces.context.FacesContext;

import java.util.HashMap;
import java.util.Map;

/**
 * Abstract resource disabling caching.
 *
 * @author Matthias Walliczek
 */
public abstract class NonCachableResource extends CuiResource {

    static final String MAX_AGE_0 = "max-age=0";
    static final String PUBLIC = "public";
    static final String HEADER_ACCEPT = "Accept";
    static final String HEADER_CACHE_CONTROL = "Cache-Control";

    @Override
    public Map<String, String> getResponseHeaders() {
        Map<String, String> responseHeaders = new HashMap<>();
        responseHeaders.put(HEADER_ACCEPT, PUBLIC);
        responseHeaders.put(HEADER_CACHE_CONTROL, MAX_AGE_0);
        return responseHeaders;
    }

    @Override
    public boolean userAgentNeedsUpdate(final FacesContext context) {
        return true;
    }

}
