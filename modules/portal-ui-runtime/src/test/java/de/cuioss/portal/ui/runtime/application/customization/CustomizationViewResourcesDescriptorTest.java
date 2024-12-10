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
import de.cuioss.portal.core.test.mocks.configuration.PortalTestConfiguration;
import de.cuioss.portal.ui.api.templating.PortalTemplateDescriptor;
import de.cuioss.portal.ui.api.templating.PortalViewDescriptor;
import de.cuioss.portal.ui.test.junit5.EnablePortalUiEnvironment;
import de.cuioss.test.valueobjects.junit5.contracts.ShouldBeNotNull;
import jakarta.inject.Inject;
import lombok.Getter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@EnablePortalUiEnvironment
class CustomizationViewResourcesDescriptorTest implements ShouldBeNotNull<CustomizationViewResourcesDescriptor> {

    public static final Path BASE_PATH = Paths.get("src/test/resources");
    private static final String TEMPLATES_FOLDER = "templates";
    private static final String VIEWS_FOLDER = "views";
    private static final Path BASE_CUSTOMIZATION = BASE_PATH.resolve("customization");
    public static final SimpleDateFormat FILE_SUFFIX_DATEFORMAT = new SimpleDateFormat("yyyyMMddHHmmss");

    @Inject
    @PortalTemplateDescriptor
    @PortalViewDescriptor
    @Getter
    private CustomizationViewResourcesDescriptor underTest;

    @Inject
    private PortalTestConfiguration configuration;

    @BeforeEach
    void before() {
        configuration.update(PortalConfigurationKeys.PORTAL_CUSTOMIZATION_DIR, BASE_CUSTOMIZATION.toString());
        underTest.initialize();
    }

    @Test
    void shouldDetermineExistingTemplatesDirectory() {
        assertEquals(BASE_CUSTOMIZATION.resolve(TEMPLATES_FOLDER).toString(), underTest.getTemplatePath());
        configuration.update(PortalConfigurationKeys.PORTAL_CUSTOMIZATION_ENABLED, "true");
        assertEquals(2, underTest.getHandledTemplates().size());
        assertTrue(underTest.getHandledTemplates().contains("a.xhtml"));
        assertTrue(underTest.getHandledTemplates().contains("c.xhtml/d.xhtml"));
    }

    @Test
    void shouldDetermineExistingViewsDirectory() {
        assertEquals(BASE_CUSTOMIZATION.resolve(VIEWS_FOLDER).toString(), underTest.getViewPath());
        assertEquals(1, underTest.getHandledViews().size());
        assertEquals("hello.xhtml", underTest.getHandledViews().get(0));
    }
}
