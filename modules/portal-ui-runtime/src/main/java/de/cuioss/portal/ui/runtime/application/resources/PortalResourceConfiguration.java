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

import static de.cuioss.portal.configuration.PortalConfigurationKeys.RESOURCE_HANDLED_LIBRARIES;
import static de.cuioss.portal.configuration.PortalConfigurationKeys.RESOURCE_HANDLED_SUFFIXES;
import static de.cuioss.portal.configuration.PortalConfigurationKeys.RESOURCE_VERSION;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import de.cuioss.portal.configuration.PortalConfigurationKeys;
import de.cuioss.portal.configuration.types.ConfigAsList;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Combines the runtime information that are specific to the
 * {@link CuiResourceHandler}. Usually this are version information (for the
 * cache-buster-functionality) and library names that are to be under version
 * control.
 * <p>
 * The default implementation reads the Resource related configuration from the
 * configuration-sub-system, the keys are described at
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

    @Inject
    @ConfigProperty(name = RESOURCE_VERSION)
    @Getter
    private String version;

    @Getter
    @Inject
    @ConfigAsList(name = RESOURCE_HANDLED_LIBRARIES)
    private List<String> handledLibraries;

    @Getter
    @Inject
    @ConfigAsList(name = RESOURCE_HANDLED_SUFFIXES)
    private List<String> handledSuffixes;

}
