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

import de.cuioss.portal.common.priority.PortalPriorities;
import de.cuioss.portal.ui.api.templating.MultiTemplatingMapper;
import de.cuioss.portal.ui.api.templating.PortalMultiTemplatingMapper;
import de.cuioss.portal.ui.api.templating.PortalTemplateDescriptor;
import de.cuioss.portal.ui.api.templating.PortalViewResourcesConfigChanged;
import de.cuioss.portal.ui.api.templating.PortalViewResourcesConfigChangedType;
import de.cuioss.portal.ui.api.templating.StaticTemplateDescriptor;
import de.cuioss.tools.collect.MapBuilder;
import de.cuioss.tools.io.FileLoaderUtility;
import de.cuioss.tools.logging.CuiLogger;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serial;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static de.cuioss.portal.ui.api.PortalCoreBeanNames.MULTI_TEMPLATING_MAPPER_BEAN_NAME;
import static de.cuioss.tools.collect.CollectionLiterals.mutableList;

/**
 * The Mapper collects all instances of {@link StaticTemplateDescriptor} and
 * sorts the templates accordingly. Itself it acts as a
 * {@link MultiTemplatingMapper}
 *
 * @author Oliver Wolff
 */
@PortalMultiTemplatingMapper
@ApplicationScoped
@Priority(PortalPriorities.PORTAL_CORE_LEVEL)
@Named(MULTI_TEMPLATING_MAPPER_BEAN_NAME)
@EqualsAndHashCode(of = "templateMap")
@ToString(of = "templateMap")
public class PortalTemplateMapper implements MultiTemplatingMapper {

    @Serial
    private static final long serialVersionUID = -8398917391620682636L;

    private static final CuiLogger log = new CuiLogger(PortalTemplateMapper.class);

    private Map<String, URL> templateMap;

    @Inject
    @PortalTemplateDescriptor
    private Instance<StaticTemplateDescriptor> descriptors;

    /**
     * Sorts the descriptors according to Priority annotation and creates the
     * corresponding mapping.
     */
    @PostConstruct
    public void init() {
        final List<StaticTemplateDescriptor> sortedDescriptors = PortalPriorities
                .sortByPriority(mutableList(descriptors));
        // Now iterate over sorted descriptors and create the mapping
        final Map<String, URL> builderMap = new HashMap<>();
        for (final StaticTemplateDescriptor descriptor : sortedDescriptors) {
            for (final String resourceName : descriptor.getHandledTemplates()) {
                if (!builderMap.containsKey(resourceName)) {
                    handleDescriptor(builderMap, descriptor, resourceName);
                }
            }
        }
        templateMap = MapBuilder.copyFrom(builderMap).toImmutableMap();
        if (log.isDebugEnabled()) {
            final var viewMapDebug = new StringBuilder("Resulting templates map:\r");
            templateMap.forEach((key, value) -> viewMapDebug.append("%-30s -> %s\r".formatted(key, value.getPath())));
            log.debug(viewMapDebug.toString());
        }
    }

    public void handleDescriptor(final Map<String, URL> builderMap, final StaticTemplateDescriptor descriptor,
                                 final String resourceName) {
        try {
            final var url = FileLoaderUtility.getLoaderForPath(descriptor.getTemplatePath() + '/' + resourceName)
                    .getURL();
            if (null == url) {
                log.warn("Portal-126: Template {} with path {} from descriptor {} was not found", resourceName,
                        descriptor.getTemplatePath(), descriptor.toString());
            } else {
                builderMap.put(resourceName, url);
            }
        } catch (final IllegalArgumentException e) {
            log.warn(
                    "Portal-144: Configured view/template resource '" + resourceName + "' can not be resolved, skipped",
                    e);
        }
    }

    @Override
    public URL resolveTemplatePath(final String requestedResource) {
        final var resolved = templateMap.get(requestedResource);
        if (null == resolved) {
            throw new IllegalArgumentException("No mapping found for " + requestedResource);
        }
        return resolved;
    }

    /**
     * Listener for {@link PortalViewResourcesConfigChanged}s. Reinitialize the
     * templates map.
     *
     * @param type
     */
    void configurationChangeEventListener(
            @Observes @PortalViewResourcesConfigChanged final PortalViewResourcesConfigChangedType type) {
        if (PortalViewResourcesConfigChangedType.TEMPLATES == type) {
            log.debug("Reinitialize templates map");
            init();
        }
    }
}
