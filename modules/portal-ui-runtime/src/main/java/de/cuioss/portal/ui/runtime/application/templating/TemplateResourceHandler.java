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
package de.cuioss.portal.ui.runtime.application.templating;

import de.cuioss.portal.common.cdi.PortalBeanManager;
import de.cuioss.portal.ui.api.templating.MultiTemplatingMapper;
import de.cuioss.portal.ui.api.templating.PortalMultiTemplatingMapper;
import de.cuioss.portal.ui.runtime.application.resources.PortalPathValidator;
import de.cuioss.tools.base.Preconditions;
import de.cuioss.tools.logging.CuiLogger;
import de.cuioss.tools.string.Joiner;
import de.cuioss.tools.string.Splitter;
import jakarta.faces.application.ResourceHandler;
import jakarta.faces.application.ResourceHandlerWrapper;
import jakarta.faces.application.ViewResource;
import jakarta.faces.context.FacesContext;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.net.URL;
import java.util.List;

import static de.cuioss.tools.collect.CollectionLiterals.mutableList;
import static de.cuioss.tools.string.MoreStrings.nullToEmpty;

/**
 * Implementation of Multi-Templating: see package-doc
 * de.cuioss.portal.ui.api.templating for details
 *
 * @author Oliver Wolff
 */
@RequiredArgsConstructor
public class TemplateResourceHandler extends ResourceHandlerWrapper {

    private static final CuiLogger LOGGER = new CuiLogger(TemplateResourceHandler.class);

    private static final PortalPathValidator PATH_VALIDATOR = new PortalPathValidator();

    public static final String RESOURCE_PREFIX_TEMPLATES = "/templates/";

    @Getter
    @NonNull
    private final ResourceHandler wrapped;

    @Getter(lazy = true)
    private final MultiTemplatingMapper multiTemplatingMapper = PortalBeanManager
            .resolveBeanOrThrowIllegalStateException(MultiTemplatingMapper.class, PortalMultiTemplatingMapper.class);

    private static String removePrefix(final String resourceName) {
        List<String> list = mutableList(Splitter.on("/").omitEmptyStrings().splitToList(resourceName));
        Preconditions.checkState(list.size() > 1, "Expected identifier in form of '/templates/xyz.xhtml', actually: %s", resourceName);
        return Joiner.on('/').join(list.subList(1, list.size()));
    }

    private static boolean shouldHandleResource(final String resourceName) {
        return nullToEmpty(resourceName).startsWith(RESOURCE_PREFIX_TEMPLATES);
    }

    @Override
    public ViewResource createViewResource(final FacesContext context, final String resourceName) {
        if (shouldHandleResource(resourceName)) {
            if (!PATH_VALIDATOR.isValidPath(resourceName)) {
                LOGGER.warn("Portal-150: Rejected invalid template resource path: '%s'", resourceName);
                return super.createViewResource(context, resourceName);
            }
            LOGGER.debug("Found template resource for %s", resourceName);
            return new ViewResource() {

                @Override
                public URL getURL() {
                    return getMultiTemplatingMapper().resolveTemplatePath(removePrefix(resourceName));
                }
            };
        }
        return super.createViewResource(context, resourceName);
    }
}
