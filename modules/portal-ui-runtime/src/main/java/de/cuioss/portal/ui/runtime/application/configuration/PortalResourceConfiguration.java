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
package de.cuioss.portal.ui.runtime.application.configuration;

import static de.cuioss.portal.configuration.PortalConfigurationKeys.RESOURCE_HANDLED_LIBRARIES;
import static de.cuioss.portal.configuration.PortalConfigurationKeys.RESOURCE_HANDLED_SUFFIXES;
import static de.cuioss.portal.configuration.PortalConfigurationKeys.RESOURCE_VERSION;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import de.cuioss.jsf.api.application.resources.CuiResourceConfiguration;
import de.cuioss.jsf.api.application.resources.impl.CuiResourceConfigurationImpl;
import de.cuioss.portal.configuration.PortalConfigurationChangeEvent;
import de.cuioss.portal.configuration.PortalConfigurationKeys;
import de.cuioss.portal.configuration.types.ConfigAsList;
import de.cuioss.tools.collect.MoreCollections;
import de.cuioss.tools.logging.CuiLogger;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * The default implementation reads the Resource related configuration from the
 * web.xml, the keys are described at {@link PortalConfigurationKeys}
 *
 * @author Oliver Wolff
 */
@ApplicationScoped
@Named(CuiResourceConfigurationImpl.BEAN_NAME)
@EqualsAndHashCode(of = { "version", "handledLibraries", "handledSuffixes" })
@ToString(of = { "version", "handledLibraries", "handledSuffixes" })
public class PortalResourceConfiguration implements CuiResourceConfiguration {

    private static final long serialVersionUID = -4555046242472132960L;

    private static final CuiLogger log = new CuiLogger(PortalResourceConfiguration.class);

    @Inject
    @ConfigAsList(name = RESOURCE_HANDLED_LIBRARIES, defaultValue = "com.icw.cui.fonts,com.icw.portal.css,com.icw.cui.javascript,thirdparty.legacy.js,thirdparty.js,vendor")
    private Provider<List<String>> handledLibrariesProvider;

    @Inject
    @ConfigAsList(name = RESOURCE_HANDLED_SUFFIXES)
    private Provider<List<String>> handledSuffixesProvider;

    @Inject
    @ConfigProperty(name = RESOURCE_VERSION)
    private Provider<String> versionProvider;

    @Getter
    private String version;

    @Getter
    private List<String> handledLibraries;

    @Getter
    private List<String> handledSuffixes;

    /**
     * Ensures that the configuration system is initialized properly
     */
    @PostConstruct
    public void init() {
        doConfigure();
    }

    private void doConfigure() {
        version = versionProvider.get();
        handledLibraries = handledLibrariesProvider.get();
        handledSuffixes = handledSuffixesProvider.get();
    }

    /**
     * Listener for {@link PortalConfigurationChangeEvent}s. Reconfigures the
     * default-resource-configuration
     *
     * @param deltaMap
     */
    void configurationChangeEventListener(
            @Observes @PortalConfigurationChangeEvent final Map<String, String> deltaMap) {
        if (MoreCollections.containsKey(deltaMap, RESOURCE_VERSION, RESOURCE_HANDLED_LIBRARIES,
                RESOURCE_HANDLED_SUFFIXES)) {
            log.debug("Reloading Resource Configuration");
            doConfigure();
        }
    }
}
