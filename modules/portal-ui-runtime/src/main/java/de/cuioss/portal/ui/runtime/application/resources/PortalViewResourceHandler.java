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

import de.cuioss.portal.ui.runtime.application.templating.TemplateResourceHandler;
import de.cuioss.tools.logging.CuiLogger;
import jakarta.faces.application.ResourceHandler;
import jakarta.faces.application.ResourceHandlerWrapper;
import jakarta.faces.application.ViewResource;
import jakarta.faces.context.FacesContext;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Used for resolving View-Resources from /portal/views
 *
 * @author Oliver Wolff
 */
@RequiredArgsConstructor
public class PortalViewResourceHandler extends ResourceHandlerWrapper {

    private static final int MAX_CACHE_SIZE = 200;

    private static final Map<String, PortalViewResourceHolder> CACHE =
            Collections.synchronizedMap(new LinkedHashMap<>(64, 0.75f, true) {
                @Override
                protected boolean removeEldestEntry(Map.Entry<String, PortalViewResourceHolder> eldest) {
                    return size() > MAX_CACHE_SIZE;
                }
            });

    private static final CuiLogger LOGGER = new CuiLogger(PortalViewResourceHandler.class);

    private static final PortalPathValidator PATH_VALIDATOR = new PortalPathValidator();

    /**
     * Describes the path where to look up the portal-views.
     */
    public static final String LOOKUP_PREFIX = "/portal/views";

    @Getter
    @NonNull
    private final ResourceHandler wrapped;

    private static boolean shouldHandleResource(final String resourceName) {
        return !resourceName.startsWith(TemplateResourceHandler.RESOURCE_PREFIX_TEMPLATES) && !"/".equals(resourceName);
    }

    @Override
    public ViewResource createViewResource(final FacesContext context, final String resourceName) {
        if (shouldHandleResource(resourceName)) {
            if (!PATH_VALIDATOR.isValidPath(resourceName)) {
                LOGGER.warn("Portal-150: Rejected invalid view resource path: '%s'", resourceName);
                return wrapped.createViewResource(context, resourceName);
            }
            LOGGER.debug("Requested view resource: %s", resourceName);
            final var handler = CACHE.computeIfAbsent(resourceName, k -> new PortalViewResourceHolder(resourceName));
            if (handler.isResourceAvailable()) {
                return handler;
            }
            LOGGER.debug("Requested view resource: %s can not be found within /portal/views/", resourceName);
        }
        return wrapped.createViewResource(context, resourceName);
    }
}
