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

import static de.cuioss.portal.ui.api.PortalCoreBeanNames.MULTI_VIEW_MAPPER_BEAN_NAME;
import static de.cuioss.tools.collect.CollectionLiterals.mutableList;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.annotation.Priority;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;

import de.cuioss.portal.configuration.common.PortalPriorities;
import de.cuioss.portal.ui.api.templating.MultiViewMapper;
import de.cuioss.portal.ui.api.templating.PortalMultiViewMapper;
import de.cuioss.portal.ui.api.templating.PortalViewDescriptor;
import de.cuioss.portal.ui.api.templating.PortalViewResourcesConfigChanged;
import de.cuioss.portal.ui.api.templating.PortalViewResourcesConfigChangedType;
import de.cuioss.portal.ui.api.templating.StaticViewDescriptor;
import de.cuioss.tools.io.FileLoaderUtility;
import de.cuioss.tools.logging.CuiLogger;
import de.cuioss.uimodel.application.CuiProjectStage;
import lombok.EqualsAndHashCode;
import lombok.ToString;

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

    private static final CuiLogger log = new CuiLogger(PortalViewMapper.class);

    private Map<String, URL> viewMap;

    @Inject
    @PortalViewDescriptor
    private Instance<StaticViewDescriptor> descriptors;

    @Inject
    private Provider<CuiProjectStage> projectStageProvider;

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
            log.debug("found descriptor: {}", descriptor.getClass().getCanonicalName());
            for (final String resourceName : descriptor.getHandledViews()) {
                if (!viewMap.containsKey(resourceName)) {
                    handleDescriptor(viewMap, descriptor, resourceName);
                } else {
                    log.debug("skipping view {}", resourceName);
                }
            }
        }
        if (log.isDebugEnabled()) {
            final var viewMapDebug = new StringBuilder("Resulting view map:\r");
            viewMap.forEach((key, value) -> viewMapDebug.append("%-30s -> %s\r".formatted(key, value.getPath())));
            log.debug(viewMapDebug.toString());
        }
    }

    private void handleDescriptor(final Map<String, URL> builderMap, final StaticViewDescriptor descripor,
            final String resourceName) {
        try {
            final var url = FileLoaderUtility.getLoaderForPath(descripor.getViewPath() + '/' + resourceName).getURL();
            if (null == url) {
                log.warn("Portal-127: View {} with path {} from descriptor {} was not found", resourceName,
                        descripor.getViewPath(), descripor.toString());
            } else {
                log.debug("adding view {}", resourceName);
                builderMap.put(resourceName, url);
            }
        } catch (final IllegalArgumentException e) {
            log.warn("Portal-144: Configured view/template resource '{}' can not be resolved, skipped", resourceName);
        }
    }

    @Override
    public Optional<URL> resolveViewPath(final String requestedResource) {
        return Optional.ofNullable(viewMap.computeIfAbsent(requestedResource,
                key -> FileLoaderUtility.getLoaderForPath("classpath:/META-INF/" + key).getURL()));
    }

    /**
     * Listener for {@link PortalViewResourcesConfigChanged}s. Reinitialize the
     * templates map.
     *
     * @param type
     */
    void configurationChangeEventListener(
            @Observes @PortalViewResourcesConfigChanged final PortalViewResourcesConfigChangedType type) {
        if (PortalViewResourcesConfigChangedType.VIEWS == type) {
            log.debug("Reinitialize templates map");
            init();
        }
    }
}
