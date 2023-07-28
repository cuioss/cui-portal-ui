package de.cuioss.portal.ui.runtime.application.customization;

import static de.cuioss.portal.configuration.PortalConfigurationKeys.PORTAL_CUSTOMIZATION_DIR;
import static de.cuioss.portal.configuration.PortalConfigurationKeys.PORTAL_CUSTOMIZATION_ENABLED;
import static de.cuioss.tools.collect.CollectionLiterals.mutableList;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.annotation.Priority;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import de.cuioss.portal.configuration.PortalConfigurationChangeEvent;
import de.cuioss.portal.configuration.PortalConfigurationKeys;
import de.cuioss.portal.configuration.common.PortalPriorities;
import de.cuioss.portal.configuration.schedule.FileChangedEvent;
import de.cuioss.portal.configuration.schedule.FileWatcherService;
import de.cuioss.portal.configuration.schedule.PortalFileWatcherService;
import de.cuioss.portal.ui.api.templating.PortalTemplateDescriptor;
import de.cuioss.portal.ui.api.templating.PortalViewDescriptor;
import de.cuioss.portal.ui.api.templating.PortalViewResourcesConfigChanged;
import de.cuioss.portal.ui.api.templating.PortalViewResourcesConfigChangedType;
import de.cuioss.portal.ui.api.templating.StaticTemplateDescriptor;
import de.cuioss.portal.ui.api.templating.StaticViewDescriptor;
import de.cuioss.tools.io.MorePaths;
import de.cuioss.tools.logging.CuiLogger;
import de.cuioss.tools.string.MoreStrings;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Descriptor to handle customized views and templates.
 *
 * Searches for templates and views inside of
 * {@value PortalConfigurationKeys#PORTAL_CUSTOMIZATION_DIR}.
 *
 * In {@link ProjectStage#DEVELOPMENT} the folders are monitored and new created
 * / deleted files will result in a reset of the corresponding file lists.
 *
 * @author Matthias Walliczek
 */
@PortalTemplateDescriptor
@PortalViewDescriptor
@ApplicationScoped
@Priority(PortalPriorities.PORTAL_INSTALLATION_LEVEL)
@Named
@ToString(exclude = { "fileWatcherService", "viewResourcesConfigChangedEvent" })
@EqualsAndHashCode(exclude = { "fileWatcherService", "viewResourcesConfigChangedEvent" })
public class CustomizationViewResourcesDescriptor implements StaticTemplateDescriptor, StaticViewDescriptor {

    private static final CuiLogger log = new CuiLogger(CustomizationViewResourcesDescriptor.class);

    private static final String TEMPLATES_DIRECTORY = "templates";
    private static final String VIEWS_DIRECTORY = "views";

    private static final long serialVersionUID = 2575347911928721019L;

    @Inject
    @PortalFileWatcherService
    private FileWatcherService fileWatcherService;

    @Inject
    @PortalViewResourcesConfigChanged
    private Event<PortalViewResourcesConfigChangedType> viewResourcesConfigChangedEvent;

    @Inject
    @ConfigProperty(name = PORTAL_CUSTOMIZATION_ENABLED)
    private Provider<Boolean> customizationEnabledProvider;

    @Inject
    @ConfigProperty(name = PortalConfigurationKeys.PORTAL_CUSTOMIZATION_DIR)
    private Provider<Optional<String>> customizationDir;

    @Getter
    private List<String> handledTemplates;

    @Getter
    private String templatePath;

    @Getter
    private List<String> handledViews;

    @Getter
    private String viewPath;

    /**
     * Initialization.
     */
    @PostConstruct
    public void initialize() {
        if (!MoreStrings.isEmpty(templatePath)) {
            fileWatcherService.unregister(Paths.get(templatePath));
        }
        if (!MoreStrings.isEmpty(viewPath)) {
            fileWatcherService.unregister(Paths.get(viewPath));
        }
        templatePath = null;
        handledTemplates = Collections.emptyList();
        viewPath = null;
        handledViews = Collections.emptyList();
        if (!customizationEnabledProvider.get().booleanValue()) {
            return;
        }
        final var customizationDirectory = customizationDir.get();
        if (!customizationDirectory.isPresent()) {
            return;
        }
        final var customizationPath = Paths.get(customizationDirectory.get());
        var currentTemplatePath = customizationPath.resolve(TEMPLATES_DIRECTORY);
        if (currentTemplatePath.toFile().exists()) {
            templatePath = currentTemplatePath.toString();
            handledTemplates = retrieveViewResources(currentTemplatePath, "");
            log.debug("Found custom templates folder {} and registered these templates: {}", templatePath,
                    handledTemplates.toString());
            fileWatcherService.register(currentTemplatePath);
        } else {
            log.debug("TEMPLATES folder {} does not exists", templatePath);
        }
        var currentViewPath = customizationPath.resolve(VIEWS_DIRECTORY);
        if (currentViewPath.toFile().exists()) {
            viewPath = currentViewPath.toString();
            handledViews = retrieveViewResources(currentViewPath, "");
            log.debug("Found custom views folder {} and registered these views: {}", viewPath, handledViews.toString());
            fileWatcherService.register(currentViewPath);
        } else {
            log.debug("VIEWS folder {} does not exists", viewPath);
        }
    }

    private static List<String> retrieveViewResources(final Path currentTemplatePath, final String prefix) {
        List<String> result = mutableList();
        try (var directoryStream = Files.newDirectoryStream(currentTemplatePath)) {
            for (Path pathname : directoryStream) {
                if (pathname.toFile().isFile() && pathname.toString().endsWith(".xhtml")) {
                    result.add(prefix.concat(pathname.toFile().getName()));
                } else if (pathname.toFile().isDirectory()) {
                    result.addAll(retrieveViewResources(currentTemplatePath.resolve(pathname.toFile().getName()),
                            prefix.concat(pathname.toFile().getName() + "/")));
                }
            }
        } catch (IOException ex) {
            log.warn(ex, "Portal-122: Unable to search path: {}", currentTemplatePath.toString());
        }
        return result;
    }

    /**
     * Listener for {@link PortalConfigurationChangeEvent}s. Reconfigures the
     * project-stage-configuration
     *
     * @param deltaMap changed configuration properties
     */
    void portalConfigurationChangeEventListener(
            @Observes @PortalConfigurationChangeEvent final Map<String, String> deltaMap) {
        if (deltaMap.containsKey(PORTAL_CUSTOMIZATION_ENABLED) || deltaMap.containsKey(PORTAL_CUSTOMIZATION_DIR)) {
            reloadAndFireEvents();
        }
    }

    void fileChangeListener(@Observes @FileChangedEvent final Path newPath) {
        if (null != templatePath && MorePaths.isSameFile(Paths.get(templatePath), newPath)
                || null != viewPath && MorePaths.isSameFile(Paths.get(viewPath), newPath)) {
            reloadAndFireEvents();
        }
    }

    private void reloadAndFireEvents() {
        log.debug("Portal-007: Reloading custom view resources");
        var oldTemplatePath = templatePath;
        var oldHandledTemplates = handledTemplates;
        var oldViewPath = viewPath;
        var oldHandledViews = handledViews;
        initialize();
        if (!MoreStrings.nullToEmpty(templatePath).equals(oldTemplatePath)
                || !handledTemplates.equals(oldHandledTemplates)) {
            viewResourcesConfigChangedEvent.fire(PortalViewResourcesConfigChangedType.TEMPLATES);
        }
        if (!MoreStrings.nullToEmpty(viewPath).equals(oldViewPath) || !handledViews.equals(oldHandledViews)) {
            viewResourcesConfigChangedEvent.fire(PortalViewResourcesConfigChangedType.VIEWS);
        }
    }
}
