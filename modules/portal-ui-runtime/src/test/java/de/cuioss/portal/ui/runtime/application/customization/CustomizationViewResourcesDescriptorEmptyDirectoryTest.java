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

import de.cuioss.portal.configuration.PortalConfigurationKeys;
import de.cuioss.portal.core.test.mocks.configuration.PortalTestConfiguration;
import de.cuioss.portal.ui.api.templating.PortalTemplateDescriptor;
import de.cuioss.portal.ui.api.templating.PortalViewDescriptor;
import de.cuioss.portal.ui.test.junit5.EnablePortalUiEnvironment;
import de.cuioss.test.juli.junit5.EnableTestLogger;
import de.cuioss.test.valueobjects.junit5.contracts.ShouldBeNotNull;
import jakarta.inject.Inject;
import lombok.Getter;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;

import static de.cuioss.test.generator.Generators.letterStrings;
import static org.junit.jupiter.api.Assertions.*;

@EnablePortalUiEnvironment
@EnableTestLogger
class CustomizationViewResourcesDescriptorEmptyDirectoryTest implements ShouldBeNotNull<CustomizationViewResourcesDescriptor> {


    @Inject
    @PortalTemplateDescriptor
    @PortalViewDescriptor
    @Getter
    private CustomizationViewResourcesDescriptor underTest;


    @Inject
    private PortalTestConfiguration configuration;

    @Test
    void shouldHandleNotExistingDirectories() throws Exception {
        final var playGround = Path.of("target/playground/");
        if (!Files.exists(playGround)) {
            Files.createDirectories(playGround);
        }
        final var stamp = CustomizationViewResourcesDescriptorTest.FILE_SUFFIX_DATEFORMAT.format(new Date()) + letterStrings(5, 6).next();
        final var playGroundBase = playGround.resolve(stamp);

        configuration.update(PortalConfigurationKeys.PORTAL_CUSTOMIZATION_DIR, playGroundBase.toString());
        underTest.initialize();

        assertNull(underTest.getTemplatePath());
        assertNotNull(underTest.getHandledTemplates());
        assertTrue(underTest.getHandledTemplates().isEmpty());
        assertNull(underTest.getViewPath());
        assertNotNull(underTest.getHandledViews());
        assertTrue(underTest.getHandledViews().isEmpty());
    }
}
