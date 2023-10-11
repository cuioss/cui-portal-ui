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

import static de.cuioss.portal.configuration.PortalConfigurationKeys.PORTAL_CUSTOMIZATION_DIR;
import static de.cuioss.portal.configuration.PortalConfigurationKeys.PORTAL_CUSTOMIZATION_ENABLED;
import static de.cuioss.tools.collect.CollectionLiterals.immutableSet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;

import javax.annotation.Priority;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Provider;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import de.cuioss.jsf.api.application.bundle.ResourceBundleWrapper;
import de.cuioss.portal.common.locale.PortalLocale;
import de.cuioss.portal.common.priority.PortalPriorities;
import de.cuioss.portal.configuration.PortalConfigurationChangeEvent;
import de.cuioss.portal.configuration.PortalConfigurationKeys;
import de.cuioss.portal.configuration.schedule.FileChangedEvent;
import de.cuioss.portal.configuration.schedule.FileWatcherService;
import de.cuioss.portal.configuration.schedule.PortalFileWatcherService;
import de.cuioss.tools.io.MorePaths;
import de.cuioss.tools.logging.CuiLogger;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Implementation of a {@link ResourceBundleWrapper} to retrieve messages from
 * the {@link #RESOURCES_DIRECTORY} directory inside
 * {@link PortalConfigurationKeys#PORTAL_CUSTOMIZATION_DIR} at the file system.
 * Takes care of the locale. A FileWatcher is registered and at each change all
 * messages are reloaded.
 *
 * @author Matthias Walliczek
 *
 */
@ApplicationScoped
@EqualsAndHashCode(of = "resourcePath", callSuper = false)
@ToString(of = "resourcePath")
@Priority(PortalPriorities.PORTAL_INSTALLATION_LEVEL)
public class CustomizationResourceBundleWrapper implements ResourceBundleWrapper {

    private static final long serialVersionUID = 919996532090944489L;

    private static final CuiLogger log = new CuiLogger(CustomizationResourceBundleWrapper.class);

    private static final String MESSAGES_SUFFIX = ".properties";

    private static final String MESSAGES_PREFIX = "messages_";

    private static final String DEFAULT_MESSAGES_PROPERTIES = "messages.properties";

    /**
     * The name of the folder inside the
     * {@link PortalConfigurationKeys#PORTAL_CUSTOMIZATION_DIR}.
     */
    public static final String RESOURCES_DIRECTORY = "i18n";

    @SuppressWarnings("cdi-ambiguous-dependency")
    @Inject
    @PortalLocale
    private Provider<Locale> localeProvider;

    @SuppressWarnings("cdi-ambiguous-dependency")
    @Inject
    @PortalFileWatcherService
    private FileWatcherService fileWatcherServiceProvider;

    @SuppressWarnings("cdi-ambiguous-dependency")
    @Inject
    @ConfigProperty(name = PORTAL_CUSTOMIZATION_ENABLED)
    private Provider<Boolean> customizationEnabledProvider;

    @SuppressWarnings("cdi-ambiguous-dependency")
    @Inject
    @ConfigProperty(name = PORTAL_CUSTOMIZATION_DIR)
    private Provider<Optional<String>> customizationDir;

    private File resourcePath;

    private Map<Locale, Properties> properties;

    @Override
    public String getMessage(final String key) {
        return getProperties(localeProvider.get()).getProperty(key);
    }

    @Override
    public Set<String> keySet() {
        return immutableSet(getProperties(localeProvider.get()).stringPropertyNames());
    }

    @Override
    public String getBundleContent() {
        return toString();
    }

    private Properties getProperties(final Locale locale) {
        if (null == properties) {
            configureResourcePath();
        }
        return properties.computeIfAbsent(locale, this::retrievePropertiesForLocale);
    }

    private Properties retrievePropertiesForLocale(final Locale locale) {
        var currentProperties = new Properties();
        if (null != resourcePath && resourcePath.isDirectory()) {
            checkAndParseFile(currentProperties, DEFAULT_MESSAGES_PROPERTIES);
            checkAndParseFile(currentProperties, MESSAGES_PREFIX + locale.getLanguage() + MESSAGES_SUFFIX);
            checkAndParseFile(currentProperties, MESSAGES_PREFIX + locale.toString() + MESSAGES_SUFFIX);
        }
        log.debug("retrieve properties for locale {}: {}", locale, currentProperties);
        return currentProperties;
    }

    private void configureResourcePath() {
        properties = new HashMap<>();
        if (null != resourcePath) {
            fileWatcherServiceProvider.unregister(resourcePath.toPath());
        }
        resourcePath = null;
        if (!customizationEnabledProvider.get().booleanValue()) {
            return;
        }
        var customizationDirectory = customizationDir.get();
        if (customizationDirectory.isPresent()) {
            resourcePath = Paths.get(customizationDirectory.get()).resolve(RESOURCES_DIRECTORY).toFile();
            if (resourcePath.exists() && resourcePath.isDirectory()) {
                fileWatcherServiceProvider.register(resourcePath.toPath());
            } else {
                log.warn("Portal-157: Customization resources directory not available: {}", resourcePath);
            }
        }
    }

    private void checkAndParseFile(final Properties currentProperties, final String fileName) {
        var defaultPath = resourcePath.toPath().resolve(fileName).toFile();
        if (defaultPath.isFile()) {
            try (var stream = new FileInputStream(defaultPath)) {
                currentProperties.load(stream);
            } catch (IOException e) {
                log.warn(e, "Portal-124: Customization messages properties file {} could not be read", defaultPath);
            }
        }
    }

    /**
     * Listener for {@link PortalConfigurationChangeEvent}s. Emptying our properties
     * to trigger re-configuration.
     *
     * @param deltaMap changed configuration properties
     */
    void portalConfigurationChangeEventListener(
            @Observes @PortalConfigurationChangeEvent final Map<String, String> deltaMap) {
        if (deltaMap.containsKey(PORTAL_CUSTOMIZATION_ENABLED) || deltaMap.containsKey(PORTAL_CUSTOMIZATION_DIR)) {
            properties = null;
        }
    }

    void fileChangeListener(@Observes @FileChangedEvent final Path newPath) {
        if (null != resourcePath && MorePaths.isSameFile(resourcePath.toPath(), newPath)) {
            log.info("Portal-018: Changes in customization dir ({}) detected. Reloading.", newPath.toAbsolutePath());
            properties = null;
        }
    }
}
