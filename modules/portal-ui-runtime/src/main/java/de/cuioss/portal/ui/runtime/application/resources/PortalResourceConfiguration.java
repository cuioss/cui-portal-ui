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
package de.cuioss.portal.ui.runtime.application.resources;

import de.cuioss.portal.configuration.PortalConfigurationKeys;
import de.cuioss.portal.configuration.types.ConfigAsList;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

import static de.cuioss.portal.configuration.PortalConfigurationKeys.*;

/**
 * Combines the runtime information that is specific to the
 * {@link CuiResourceHandler}. Usually these are version information (for the
 * cache-buster-functionality) and library names that are to be under version
 * control.
 * <p>
 * The default implementation reads the Resource related configuration from the
 * configuration-subsystem, the keys are described at
 * {@link PortalConfigurationKeys}
 *
 * @author Oliver Wolff
 */
@ApplicationScoped
@EqualsAndHashCode
@ToString
public class PortalResourceConfiguration implements Serializable {

    @Serial
    private static final long serialVersionUID = -4555046242472132960L;

    @Getter
    private String version;

    @Getter
    private List<String> handledLibraries;

    @Getter
    private List<String> handledSuffixes;

    protected PortalResourceConfiguration() {
        // for CDI proxy
    }

    @Inject
    public PortalResourceConfiguration(
            @ConfigProperty(name = RESOURCE_VERSION) String version,
            @ConfigAsList(name = RESOURCE_HANDLED_LIBRARIES) List<String> handledLibraries,
            @ConfigAsList(name = RESOURCE_HANDLED_SUFFIXES) List<String> handledSuffixes) {
        this.version = version;
        this.handledLibraries = handledLibraries;
        this.handledSuffixes = handledSuffixes;
    }

}
