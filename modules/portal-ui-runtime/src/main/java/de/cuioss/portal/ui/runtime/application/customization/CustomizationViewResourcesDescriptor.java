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
package de.cuioss.portal.ui.runtime.application.customization;

import de.cuioss.portal.common.priority.PortalPriorities;
import de.cuioss.portal.configuration.PortalConfigurationKeys;
import de.cuioss.portal.ui.api.templating.PortalTemplateDescriptor;
import de.cuioss.portal.ui.api.templating.PortalViewDescriptor;
import de.cuioss.portal.ui.api.templating.StaticTemplateDescriptor;
import de.cuioss.portal.ui.api.templating.StaticViewDescriptor;
import de.cuioss.tools.collect.CollectionBuilder;
import de.cuioss.tools.logging.CuiLogger;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.io.IOException;
import java.io.Serial;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static de.cuioss.portal.configuration.PortalConfigurationKeys.PORTAL_CUSTOMIZATION_ENABLED;
import static de.cuioss.portal.ui.runtime.PortalUiRuntimeLogMessages.WARN;

/**
 * Descriptor to handle customized views and templates.
 * <p>
 * Searches for templates and views inside of
 * {@value PortalConfigurationKeys#PORTAL_CUSTOMIZATION_DIR}.
 * <p>
 *
 * @author Matthias Walliczek
 */
@PortalTemplateDescriptor
@PortalViewDescriptor
@ApplicationScoped
@Priority(PortalPriorities.PORTAL_INSTALLATION_LEVEL)
@Named
@ToString
@EqualsAndHashCode
public class CustomizationViewResourcesDescriptor implements StaticTemplateDescriptor, StaticViewDescriptor {

    private static final CuiLogger LOGGER = new CuiLogger(CustomizationViewResourcesDescriptor.class);

    private static final String TEMPLATES_DIRECTORY = "templates";
    private static final String VIEWS_DIRECTORY = "views";

    @Serial
    private static final long serialVersionUID = 2575347911928721019L;

    @Inject
    @ConfigProperty(name = PORTAL_CUSTOMIZATION_ENABLED)
    private Boolean customizationEnabled;

    @Inject
    @ConfigProperty(name = PortalConfigurationKeys.PORTAL_CUSTOMIZATION_DIR)
    private Optional<String> customizationDir;

    @Getter
    private List<String> handledTemplates;

    @Getter
    private String templatePath;

    @Getter
    private List<String> handledViews;

    @Getter
    private String viewPath;

    private static List<String> retrieveViewResources(final Path currentTemplatePath, final String prefix) {
        CollectionBuilder<String> result = new CollectionBuilder<>();
        try (var directoryStream = Files.newDirectoryStream(currentTemplatePath)) {
            for (Path pathname : directoryStream) {
                if (pathname.toFile().isFile() && pathname.toString().endsWith(".xhtml")) {
                    LOGGER.debug("Adding view resource: '%s'", pathname.toString());
                    result.add(prefix.concat(pathname.toFile().getName()));
                } else if (pathname.toFile().isDirectory()) {
                    LOGGER.debug("Handling directory: '%s'", pathname.toString());
                    result.add(retrieveViewResources(currentTemplatePath.resolve(pathname.toFile().getName()),
                            prefix.concat(pathname.toFile().getName() + "/")));
                }
            }
        } catch (IOException ex) {
            LOGGER.warn(ex, WARN.PORTAL_122_UNABLE_TO_SEARCH_PATH, currentTemplatePath.toString());
        }
        return result.toImmutableList();
    }

    /**
     * Initialization.
     */
    @PostConstruct
    public void initialize() {
        templatePath = null;
        handledTemplates = Collections.emptyList();
        viewPath = null;
        handledViews = Collections.emptyList();
        if (!customizationEnabled) {
            LOGGER.debug("Customization disabled, nothing to do here");
            return;
        }
        if (customizationDir.isEmpty()) {
            LOGGER.debug("Customization dir is empty, nothing to do here");
            return;
        }
        final var customizationPath = Path.of(customizationDir.get());
        var currentTemplatePath = customizationPath.resolve(TEMPLATES_DIRECTORY);
        if (currentTemplatePath.toFile().exists()) {
            templatePath = currentTemplatePath.toString();
            handledTemplates = retrieveViewResources(currentTemplatePath, "");
            LOGGER.debug("Found custom templates folder %s and registered these templates: %s", templatePath,
                    handledTemplates.toString());
        } else {
            LOGGER.debug("TEMPLATES folder %s does not exists", templatePath);
        }
        var currentViewPath = customizationPath.resolve(VIEWS_DIRECTORY);
        if (currentViewPath.toFile().exists()) {
            viewPath = currentViewPath.toString();
            handledViews = retrieveViewResources(currentViewPath, "");
            LOGGER.debug("Found custom views folder %s and registered these views: %s", viewPath, handledViews.toString());
        } else {
            LOGGER.debug("VIEWS folder %s does not exists", viewPath);
        }
    }
}
