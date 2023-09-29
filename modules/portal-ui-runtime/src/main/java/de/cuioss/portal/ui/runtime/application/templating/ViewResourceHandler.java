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

import static de.cuioss.tools.collect.CollectionLiterals.mutableList;
import static de.cuioss.tools.string.MoreStrings.nullToEmpty;

import java.net.URL;
import java.util.List;

import javax.faces.application.ResourceHandler;
import javax.faces.application.ResourceHandlerWrapper;
import javax.faces.application.ViewResource;
import javax.faces.context.FacesContext;

import de.cuioss.portal.core.cdi.PortalBeanManager;
import de.cuioss.portal.ui.api.templating.MultiTemplatingMapper;
import de.cuioss.portal.ui.api.templating.PortalMultiTemplatingMapper;
import de.cuioss.tools.logging.CuiLogger;
import de.cuioss.tools.string.Joiner;
import de.cuioss.tools.string.Splitter;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * Implementation of Multitemplating: see package-doc
 * de.cuioss.portal.ui.api.templating for details
 *
 * @author Oliver Wolff
 */
@RequiredArgsConstructor
public class ViewResourceHandler extends ResourceHandlerWrapper {

    private static final CuiLogger LOGGER = new CuiLogger(ViewResourceHandler.class);

    private static final String RESOURCE_PREFIX_TEMPLATES = "/templates/";

    private MultiTemplatingMapper multiTemplatingMapper;

    @Getter
    @NonNull
    private final ResourceHandler wrapped;

    @Override
    public ViewResource createViewResource(final FacesContext context, final String resourceName) {
        if (shouldHandleResource(resourceName)) {
            LOGGER.debug("Found template resource for %s", resourceName);
            return new ViewResource() {

                @Override
                public URL getURL() {
                    return computeURL(resourceName);
                }
            };
        }
        return super.createViewResource(context, resourceName);
    }

    /**
     * Computes a corresponding classpath related url.
     *
     * @param resourceName
     * @return
     */
    URL computeURL(final String resourceName) {
        checkMapper();
        LOGGER.debug("Resolving template resource for %s", resourceName);
        return multiTemplatingMapper.resolveTemplatePath(removePrefix(resourceName));

    }

    private void checkMapper() {
        if (null == multiTemplatingMapper) {
            multiTemplatingMapper = PortalBeanManager
                    .resolveBean(MultiTemplatingMapper.class, PortalMultiTemplatingMapper.class)
                    .orElseThrow(() -> new IllegalArgumentException(PortalBeanManager
                            .createErrorMessage(MultiTemplatingMapper.class, PortalMultiTemplatingMapper.class)));
        }

    }

    private static String removePrefix(final String resourceName) {
        List<String> list = mutableList(Splitter.on("/").omitEmptyStrings().splitToList(resourceName));
        if (list.size() < 2) {
            throw new IllegalStateException(
                    "Expected identifier in form of '/templates/xyz.xhtml', actually: " + resourceName);
        }
        return Joiner.on('/').join(list.subList(1, list.size()));
    }

    private static boolean shouldHandleResource(final String resourceName) {
        return nullToEmpty(resourceName).startsWith(RESOURCE_PREFIX_TEMPLATES);
    }
}
