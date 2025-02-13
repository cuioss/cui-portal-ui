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
import lombok.val;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * USed for resolving View-Resources from /portal/views
 *
 * @author Oliver Wolff
 */
@RequiredArgsConstructor
public class PortalViewResourceHandler extends ResourceHandlerWrapper {

    private static final Map<String, PortalViewResourceHolder> CACHE = new ConcurrentHashMap<>();

    private static final CuiLogger LOGGER = new CuiLogger(PortalViewResourceHandler.class);

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
