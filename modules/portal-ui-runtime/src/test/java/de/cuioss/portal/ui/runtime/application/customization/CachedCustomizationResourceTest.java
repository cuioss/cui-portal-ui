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

import static de.cuioss.test.generator.Generators.letterStrings;

import java.io.File;
import java.io.IOException;

import jakarta.inject.Inject;

import de.cuioss.portal.configuration.PortalConfigurationKeys;
import de.cuioss.portal.configuration.PortalConfigurationSource;
import de.cuioss.portal.core.test.mocks.configuration.PortalTestConfiguration;
import de.cuioss.portal.ui.test.junit5.EnablePortalUiEnvironment;
import de.cuioss.test.valueobjects.junit5.contracts.ShouldImplementEqualsAndHashCode;

@EnablePortalUiEnvironment
class CachedCustomizationResourceTest implements ShouldImplementEqualsAndHashCode<CachedCustomizationResource> {

    @Inject
    @PortalConfigurationSource
    private PortalTestConfiguration configuration;

    @Override
    public CachedCustomizationResource getUnderTest() {
        configuration.put(PortalConfigurationKeys.RESOURCE_MAXAGE, "60");
        configuration.fireEvent();
        try {
            return new CachedCustomizationResource(
                    new CustomizationResource(File.createTempFile("application-default", ".css"),
                            "application-default.css", letterStrings(2, 10).next(), "application/css"));
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
