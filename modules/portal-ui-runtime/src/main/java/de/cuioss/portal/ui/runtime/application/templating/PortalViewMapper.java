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
package de.cuioss.portal.ui.runtime.application.templating;

import de.cuioss.portal.common.priority.PortalPriorities;
import de.cuioss.portal.ui.api.templating.MultiViewMapper;
import de.cuioss.portal.ui.api.templating.PortalMultiViewMapper;
import de.cuioss.portal.ui.api.templating.PortalViewDescriptor;
import de.cuioss.portal.ui.api.templating.StaticViewDescriptor;
import de.cuioss.portal.ui.runtime.application.resources.PortalPathValidator;
import de.cuioss.tools.io.FileLoaderUtility;
import de.cuioss.tools.logging.CuiLogger;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static de.cuioss.portal.ui.api.PortalCoreBeanNames.MULTI_VIEW_MAPPER_BEAN_NAME;
import static de.cuioss.tools.collect.CollectionLiterals.mutableList;

/**
 * The Mapper collects all instances of {@link StaticViewDescriptor} and sorts
 * the templates accordingly. Itself it acts as a {@link MultiViewMapper}
 *
 * @author Oliver Wolff
 */
@PortalMultiViewMapper
@ApplicationScoped
@Priority(PortalPriorities.PORTAL_CORE_LEVEL)
@Named(MULTI_VIEW_MAPPER_BEAN_NAME)
@EqualsAndHashCode(of = "viewMap")
@ToString(of = "viewMap")
public class PortalViewMapper implements MultiViewMapper {

    private static final CuiLogger LOGGER = new CuiLogger(PortalViewMapper.class);

    private static final PortalPathValidator PATH_VALIDATOR = new PortalPathValidator();

    private Map<String, URL> viewMap;

    @Inject
    @PortalViewDescriptor
    private Instance<StaticViewDescriptor> descriptors;

    /**
     * Sorts the descriptors according to Priority annotation and creates the
     * corresponding mapping.
     */
    @PostConstruct
    public void init() {
        final List<StaticViewDescriptor> sortedDescriptors = PortalPriorities.sortByPriority(mutableList(descriptors));
        // Now iterate over sorted descriptors and create the mapping
        viewMap = new HashMap<>();
        for (final StaticViewDescriptor descriptor : sortedDescriptors) {
            LOGGER.debug("found descriptor: %s", descriptor.getClass().getCanonicalName());
            for (final String resourceName : descriptor.getHandledViews()) {
                if (!viewMap.containsKey(resourceName)) {
                    handleDescriptor(viewMap, descriptor, resourceName);
                } else {
                    LOGGER.debug("skipping view %s", resourceName);
                }
            }
        }
        if (LOGGER.isDebugEnabled()) {
            final var viewMapDebug = new StringBuilder("Resulting view map:\r");
            viewMap.forEach((key, value) -> viewMapDebug.append("%-30s -> %s\r".formatted(key, value.getPath())));
            LOGGER.debug(viewMapDebug.toString());
        }
    }

    private void handleDescriptor(final Map<String, URL> builderMap, final StaticViewDescriptor descriptor,
            final String resourceName) {
        try {
            final var url = FileLoaderUtility.getLoaderForPath(descriptor.getViewPath() + '/' + resourceName).getURL();
            if (null == url) {
                LOGGER.warn("Portal-127: View %s with path %s from descriptor %s was not found", resourceName,
                        descriptor.getViewPath(), descriptor.toString());
            } else {
                LOGGER.debug("adding view %s", resourceName);
                builderMap.put(resourceName, url);
            }
        } catch (final IllegalArgumentException e) {
            LOGGER.warn("Portal-144: Configured view/template resource '%s' can not be resolved, skipped", resourceName);
        }
    }

    @Override
    public Optional<URL> resolveViewPath(final String requestedResource) {
        if (!PATH_VALIDATOR.isValidPath(requestedResource)) {
            LOGGER.warn("Portal-150: Rejected invalid view path: '%s'", requestedResource);
            return Optional.empty();
        }
        return Optional.ofNullable(viewMap.computeIfAbsent(requestedResource,
                key -> FileLoaderUtility.getLoaderForPath("classpath:/META-INF/" + key).getURL()));
    }

}
