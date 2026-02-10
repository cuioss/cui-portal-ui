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

import de.cuioss.tools.io.ClassPathLoader;
import jakarta.faces.application.ViewResource;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.net.URL;

/**
 * Used for Caching Lookups for view-resources within /portal/views
 */
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString
class PortalViewResourceHolder extends ViewResource {

    private static final String CLASSPATH_PREFIXED_PORTAL_DIR = "classpath:" + PortalViewResourceHandler.LOOKUP_PREFIX;

    private final String requestedViewId;

    // Maybe null
    private final URL url;

    PortalViewResourceHolder(@NonNull String requestedViewId) {
        this.requestedViewId = requestedViewId;
        url = new ClassPathLoader(CLASSPATH_PREFIXED_PORTAL_DIR + requestedViewId).getURL();
    }

    /**
     * Checks whether there is view-resources for the given path under '/portal/views' is available.
     */
    boolean isResourceAvailable() {
        return url != null;
    }

    @Override
    public URL getURL() {
        return url;
    }
}
