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
package de.cuioss.portal.ui.test.junit5;

import de.cuioss.portal.configuration.PortalConfigurationKeys;
import de.cuioss.portal.core.test.mocks.configuration.PortalTestConfiguration;
import de.cuioss.test.jsf.defaults.BasicApplicationConfiguration;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Provider;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@EnablePortalUiEnvironment
class EnablePortalUiEnvironmentTest {

    @Inject
    private Provider<FacesContext> facesContext;

    @Inject
    @ConfigProperty(name = PortalConfigurationKeys.CLIENT_STORAGE_COOKIE_MAX_AGE)
    private Provider<String> attribute;

    @Inject
    private PortalTestConfiguration configuration;

    @Test
    void shouldEnableEnvironment() {
        assertNotNull(facesContext.get());
        assertNotNull(attribute.get());
        assertNotNull(configuration);
        assertEquals(BasicApplicationConfiguration.FIREFOX, facesContext.get().getExternalContext()
                .getRequestHeaderMap().get(BasicApplicationConfiguration.USER_AGENT));
    }

}
