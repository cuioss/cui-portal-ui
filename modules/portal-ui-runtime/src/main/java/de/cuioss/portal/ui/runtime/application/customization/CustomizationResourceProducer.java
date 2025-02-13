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
package de.cuioss.portal.ui.runtime.application.customization;

import de.cuioss.portal.configuration.PortalConfigurationKeys;
import de.cuioss.portal.configuration.schedule.FileChangedEvent;
import de.cuioss.tools.io.MorePaths;
import de.cuioss.tools.logging.CuiLogger;
import de.cuioss.tools.string.MoreStrings;
import de.cuioss.tools.string.Splitter;
import de.cuioss.uimodel.application.CuiProjectStage;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.faces.application.Resource;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Provider;
import lombok.Synchronized;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Allow overriding {@link Resource}s for customization at the file system. Will
 * retrieve all files in {@link #RESOURCES_DIRECTORY} and return these files
 * when asked for a matching resource. During {@link de.cuioss.portal.common.stage.ProjectStage#PRODUCTION}
 * all resources are cached.
 *
 * @author Matthias Walliczek
 */
@ApplicationScoped
@PortalResourceProducer
public class CustomizationResourceProducer implements ResourceProducer {

    /**
     * The name of the folder inside the
     * {@link PortalConfigurationKeys#PORTAL_CUSTOMIZATION_DIR}.
     */
    public static final String RESOURCES_DIRECTORY = "resources";
    private static final CuiLogger LOGGER = new CuiLogger(CustomizationResourceProducer.class);

    @Inject
    private Provider<CuiProjectStage> projectStageProvider;

    @Inject
    private Provider<FacesContext> facesContextProvider;

    @Inject
    @ConfigProperty(name = PortalConfigurationKeys.PORTAL_CUSTOMIZATION_DIR)
    private Provider<Optional<String>> customizationDirProvider;

    private File resourcePath;

    private Map<String, List<String>> foundResources;

    private Map<File, CachedCustomizationResource> resourcesCache;

    private static String createResourceNameInMinifiedStyle(final String resourceName) {
        final var parts = Splitter.on('.').trimResults().limit(2).splitToList(resourceName);
        if (!parts.isEmpty() && parts.size() > 1)
            return parts.get(0) + ".min." + parts.get(1);
        return null;
    }

    private static File lookupResourceDirectory(final Path customizationPath) {
        if (null != customizationPath) {
            final var resourcesDir = customizationPath.resolve(RESOURCES_DIRECTORY).toFile();
            if (resourcesDir.exists() && resourcesDir.isDirectory())
                return resourcesDir;
        }
        LOGGER.info(
                "No installation specific customization detected, using defaults. If this is intentional you can ignore this message");
        return null;
    }

    private static List<String> findFilesInDirectory(final Path path) {

        File[] filesInDirectory = null;

        try {
            filesInDirectory = path.toFile().listFiles(File::isFile);
        } catch (final SecurityException e) {
            // it's not critical enough to explode if access to directory failed
            LOGGER.warn(e, "Portal-122 : access denied to: {}", path.toFile().getName());
        }

        if (null == filesInDirectory)
            return Collections.emptyList();

        return Arrays.stream(filesInDirectory).map(File::getName).toList();
    }

    @Override
    public Resource retrieveResource(final String resourceName, final String libraryName) {
        // lazy init
        if (null == foundResources) {
            determineResources();
        }

        if (foundResources.containsKey(libraryName) && foundResources.get(libraryName).contains(resourceName))
            return createCustomizationResource(resourceName, libraryName);

        if (libraryName == null && !MoreStrings.isEmpty(resourceName)) {
            for (final String libName : foundResources.keySet()) {
                final var libNameAsPrefix = libName + "/";
                if (resourceName.startsWith(libNameAsPrefix))
                    // recursion
                    return retrieveResource(resourceName.substring(libNameAsPrefix.length()), libName);
            }
        }
        return null;
    }

    private Resource createCustomizationResource(final String resourceName, final String libraryName) {

        final var resourceFile = loadFile(resourceName, libraryName);

        LOGGER.trace("create customization resource: libraryName {}, resourceName {}", libraryName, resourceName);
        // during development, create each time new resource
        if (projectStageProvider.get().isDevelopment())
            return new CustomizationResource(resourceFile, resourceName, libraryName, determineMimeType(resourceFile));

        if (!resourcesCache.containsKey(resourceFile)) {
            final var mimeType = determineMimeType(resourceFile);
            resourcesCache.put(resourceFile, new CachedCustomizationResource(
                    new CustomizationResource(resourceFile, resourceName, libraryName, mimeType)));
        }

        return resourcesCache.get(resourceFile);
    }

    private File loadFile(final String resourceName, final String libraryName) {

        if (!projectStageProvider.get().isDevelopment()) {

            final var resourceNameMinified = createResourceNameInMinifiedStyle(resourceName);

            if (null != resourceNameMinified && foundResources.get(libraryName).contains(resourceNameMinified))
                return resourcePath.toPath().resolve(libraryName).resolve(resourceNameMinified).toFile();
        }

        return resourcePath.toPath().resolve(libraryName).resolve(resourceName).toFile();
    }

    private String determineMimeType(final File resourceFile) {
        return facesContextProvider.get().getExternalContext().getMimeType(resourceFile.toString());
    }

    private void determineResources() {

        foundResources = new HashMap<>(0);
        resourcesCache = new HashMap<>(0);

        customizationDirProvider.get()
                .ifPresent(customizationDir -> resourcePath = lookupResourceDirectory(Path.of(customizationDir)));

        if (null != resourcePath) {

            LOGGER.info("Searching customization resources in folder {}", resourcePath.getAbsolutePath());

            try (final var directoryStream = Files.newDirectoryStream(resourcePath.toPath())) {
                for (final Path path : directoryStream) {
                    if (path.toFile().isDirectory()) {

                        final var pathName = path.toFile().getName();

                        final var filesInDirectory = findFilesInDirectory(
                                resourcePath.toPath().resolve(path.getFileName()));

                        foundResources.put(pathName, filesInDirectory);
                    }
                }
                LOGGER.info("Found resources: {}", foundResources);
            } catch (final IOException ex) {
                LOGGER.warn(ex, "Portal-122: Unable to search path: {}", resourcePath.toString());
            }
        }

    }

    void fileChangeListener(@Observes @FileChangedEvent final Path newPath) {
        if (null != resourcePath && MorePaths.isSameFile(resourcePath.toPath(), newPath)) {
            forceDetermineResources();
        }
    }

    @Synchronized
    private void forceDetermineResources() {

        if (null != foundResources) {
            foundResources.clear();
        }

        foundResources = null;

        if (null != resourcesCache) {
            resourcesCache.clear();
        }

        resourcesCache = null;
    }
}
