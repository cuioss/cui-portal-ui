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

import de.cuioss.portal.common.cdi.PortalBeanManager;
import de.cuioss.tools.io.StructuredFilename;
import de.cuioss.tools.logging.CuiLogger;
import de.cuioss.uimodel.application.CuiProjectStage;
import jakarta.faces.application.Resource;
import jakarta.faces.application.ResourceHandler;
import lombok.Getter;

/**
 * Deals with cui-specific resources. The corresponding modifications will only
 * take place in production environments. This will contain choosing the min
 * version of resource and adding a cache-buster to the resource request.
 */
public class CuiResourceHandler extends AbstractVersionResourceHandler {

    private static final String MINIMIZED_SUFFIX = ".min";

    private static final CuiLogger LOGGER = new CuiLogger(CuiResourceHandler.class);

    @Getter(lazy = true)
    private final PortalResourceConfiguration configuration = PortalBeanManager
            .resolveRequiredBean(PortalResourceConfiguration.class);

    @Getter(lazy = true)
    private final CuiResourceManager resourceManager = PortalBeanManager.resolveRequiredBean(CuiResourceManager.class);

    @Getter(lazy = true)
    private final CuiProjectStage projectStage = PortalBeanManager.resolveRequiredBean(CuiProjectStage.class);

    public CuiResourceHandler(final ResourceHandler wrapped) {
        super(wrapped);
    }

    @Override
    public Resource createResource(final String resourceName, final String libraryName) {
        if (shouldHandle(resourceName, libraryName)) {
            LOGGER.debug("Handling '%s'/'%s'", libraryName, resourceName);
            final var determinedResourceName = determineResourceName(resourceName, libraryName);
            return super.createResource(determinedResourceName, libraryName);
        }
        return super.createResource(resourceName, libraryName);
    }

    @Override
    protected boolean shouldHandleRequestedResource(String resourceName, String libraryName) {
        return shouldHandle(resourceName, libraryName);
    }

    @Override
    protected String getNewResourceVersion() {
        return getConfiguration().getVersion();
    }

    /**
     * Checks whether the resourceHandler should handle the given request
     *
     * @param resourceName
     * @param libraryName
     * @return boolean indicating whether the resourceHandler should handle (modify)
     * the given resource Request
     */
    private boolean shouldHandle(final String resourceName, final String libraryName) {
        if (getProjectStage().isDevelopment()) {
            return false;
        }
        var structuredFilename = new StructuredFilename(resourceName);
        var config = getConfiguration();
        return config.getHandledLibraries().contains(libraryName)
                && config.getHandledSuffixes().contains(structuredFilename.getSuffix());
    }

    /**
     * Computes / checks whether the Resource name needs to be adapted / suffixed
     * with min, if corresponding version is available.
     *
     * @param resourceName to be checked against
     * @param libraryName  to be checked against
     * @return the computed resourceName;
     */
    private String determineResourceName(final String resourceName, final String libraryName) {
        var filename = new StructuredFilename(resourceName);
        var libraryInventory = getResourceManager().getLibraryInventory(libraryName);
        if (!libraryInventory.containsMapping(resourceName)) {
            // Special case: resource-name contains .min already
            if (filename.getNamePart().endsWith(MINIMIZED_SUFFIX)) {
                libraryInventory.addMapping(resourceName, resourceName);
            } else {
                registerResourceName(libraryInventory, filename);
            }
        }
        return libraryInventory.getResourceMapping(resourceName);
    }

    /**
     * Checks whether there is a minified version available. If so it will be
     * registered to the given {@link LibraryInventory} otherwise the original
     * filename will be registered.
     *
     * @param libraryInventory
     * @param filename
     */
    private void registerResourceName(final LibraryInventory libraryInventory, final StructuredFilename filename) {
        var minResourceName = filename.getAppendedName(MINIMIZED_SUFFIX);
        synchronized (CuiResourceHandler.class) {
            var check = getWrapped().createResource(minResourceName, libraryInventory.getLibraryName());
            if (null == check) {
                libraryInventory.addMapping(filename.getOriginalName(), filename.getOriginalName());
            } else {
                libraryInventory.addMapping(filename.getOriginalName(), minResourceName);
            }
        }
    }

}
