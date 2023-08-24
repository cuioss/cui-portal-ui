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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.jboss.weld.junit5.auto.AddBeanClasses;
import org.jboss.weld.junit5.auto.EnableAlternatives;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.cuioss.portal.configuration.PortalConfigurationKeys;
import de.cuioss.portal.configuration.PortalConfigurationSource;
import de.cuioss.portal.configuration.schedule.FileChangedEvent;
import de.cuioss.portal.configuration.schedule.FileWatcherService;
import de.cuioss.portal.configuration.schedule.PortalFileWatcherService;
import de.cuioss.portal.core.test.mocks.configuration.PortalTestConfiguration;
import de.cuioss.portal.ui.api.templating.PortalTemplateDescriptor;
import de.cuioss.portal.ui.api.templating.PortalViewDescriptor;
import de.cuioss.portal.ui.api.templating.PortalViewResourcesConfigChanged;
import de.cuioss.portal.ui.api.templating.PortalViewResourcesConfigChangedType;
import de.cuioss.portal.ui.runtime.support.FileWatcherServiceMock;
import de.cuioss.portal.ui.test.junit5.EnablePortalUiEnvironment;
import de.cuioss.test.valueobjects.junit5.contracts.ShouldBeNotNull;
import lombok.Getter;

@EnablePortalUiEnvironment
@AddBeanClasses({ FileWatcherServiceMock.class })
@EnableAlternatives({ FileWatcherServiceMock.class })
class CustomizationViewResourcesDescriptorTest implements ShouldBeNotNull<CustomizationViewResourcesDescriptor> {

    private static final String TEMPLATES_FOLDER = "templates";
    private static final String VIEWS_FOLDER = "views";
    public static final Path BASE_PATH = Paths.get("src/test/resources");
    private static final Path BASE_CUSTOMIZATION = BASE_PATH.resolve("customization");
    private static final SimpleDateFormat FILE_SUFFIX_DATEFORMAT = new SimpleDateFormat("yyyyMMddHHmmss");

    @Inject
    @PortalTemplateDescriptor
    @PortalViewDescriptor
    @Getter
    private CustomizationViewResourcesDescriptor underTest;

    @Inject
    @PortalFileWatcherService
    private FileWatcherService fileWatcherService;

    @Inject
    @FileChangedEvent
    private Event<Path> fileChangeEvent;

    @Inject
    @PortalConfigurationSource
    private PortalTestConfiguration configuration;

    private boolean templatesEventWasFired;
    private boolean viewsEventWasFired;

    @BeforeEach
    void before() {
        configuration.fireEvent(PortalConfigurationKeys.PORTAL_CUSTOMIZATION_DIR, BASE_CUSTOMIZATION.toString());
        underTest.initialize();
    }

    @Test
    void shouldDetermineExistingTemplatesDirectory() {
        assertEquals(BASE_CUSTOMIZATION.resolve(TEMPLATES_FOLDER).toString(), underTest.getTemplatePath());
        configuration.put(PortalConfigurationKeys.PORTAL_CUSTOMIZATION_ENABLED, "true");
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

    @Test
    void shouldHandleNotExistingDirectories() throws IOException {
        final var playGround = Paths.get("target/playground");
        if (!Files.exists(playGround)) {
            Files.createDirectories(playGround);
        }
        final var stamp = FILE_SUFFIX_DATEFORMAT.format(new Date()) + letterStrings(5, 6).next();
        final var playGroundBase = playGround.resolve(stamp);
        Files.createDirectories(playGroundBase);

        configuration.fireEvent(PortalConfigurationKeys.PORTAL_CUSTOMIZATION_DIR, playGround.toString());
        underTest.initialize();

        assertNull(underTest.getTemplatePath());
        assertNotNull(underTest.getHandledTemplates());
        assertTrue(underTest.getHandledTemplates().isEmpty());
        assertNull(underTest.getViewPath());
        assertNotNull(underTest.getHandledViews());
        assertTrue(underTest.getHandledViews().isEmpty());
    }

    @Test
    void shouldHandleConfigChanges() {
        templatesEventWasFired = false;
        viewsEventWasFired = false;

        configuration.fireEvent(PortalConfigurationKeys.PORTAL_CUSTOMIZATION_DIR, "");
        underTest.initialize();
        assertNull(underTest.getTemplatePath());
        assertNotNull(underTest.getHandledTemplates());
        assertTrue(underTest.getHandledTemplates().isEmpty());
        assertNull(underTest.getViewPath());
        assertNotNull(underTest.getHandledViews());
        assertTrue(underTest.getHandledViews().isEmpty());

        configuration.fireEvent(PortalConfigurationKeys.PORTAL_CUSTOMIZATION_DIR, BASE_CUSTOMIZATION.toString());

        assertEquals(BASE_CUSTOMIZATION.resolve(TEMPLATES_FOLDER).toString(), underTest.getTemplatePath());
        assertEquals(2, underTest.getHandledTemplates().size());
        assertTrue(templatesEventWasFired);
        assertEquals(BASE_CUSTOMIZATION.resolve(VIEWS_FOLDER).toString(), underTest.getViewPath());
        assertEquals(1, underTest.getHandledViews().size());
        assertTrue(viewsEventWasFired);
    }

    @Test
    void shouldHandleNewFiles() throws Exception {
        templatesEventWasFired = false;
        viewsEventWasFired = false;

        final var playGround = Paths.get("target/playground");
        if (!Files.exists(playGround)) {
            Files.createDirectories(playGround);
        }

        final var stamp = FILE_SUFFIX_DATEFORMAT.format(new Date()) + letterStrings(5, 6).next();
        final var playGroundBase = playGround.resolve(stamp);
        Files.createDirectories(playGroundBase);
        final var templatesBase = playGroundBase.resolve(TEMPLATES_FOLDER);
        Files.createDirectories(templatesBase);
        final var viewsBase = playGroundBase.resolve(VIEWS_FOLDER);
        Files.createDirectories(viewsBase);

        configuration.fireEvent(PortalConfigurationKeys.PORTAL_CUSTOMIZATION_DIR, playGroundBase.toString());
        underTest.initialize();

        assertEquals(templatesBase.toString(), underTest.getTemplatePath());
        assertEquals(0, underTest.getHandledTemplates().size());
        final var newTemplateName = FILE_SUFFIX_DATEFORMAT.format(new Date()) + letterStrings(5, 6).next() + ".xhtml";
        final var newTemplatePath = Paths.get(underTest.getTemplatePath()).resolve(newTemplateName);
        Files.createFile(newTemplatePath);

        fileChangeEvent.fire(templatesBase);
        assertEquals(1, underTest.getHandledTemplates().size());
        assertTrue(templatesEventWasFired);

        assertEquals(viewsBase.toString(), underTest.getViewPath());
        assertEquals(0, underTest.getHandledViews().size());
        final var newViewName = FILE_SUFFIX_DATEFORMAT.format(new Date()) + letterStrings(5, 6).next() + ".xhtml";
        final var newViewPath = Paths.get(underTest.getViewPath()).resolve(newViewName);
        Files.createFile(newViewPath);
        fileChangeEvent.fire(viewsBase);
        assertEquals(1, underTest.getHandledViews().size());
        assertTrue(viewsEventWasFired);
    }

    void providerChangeEventListener(
            @Observes @PortalViewResourcesConfigChanged final PortalViewResourcesConfigChangedType type) {
        if (PortalViewResourcesConfigChangedType.TEMPLATES == type) {
            templatesEventWasFired = true;
        }
        if (PortalViewResourcesConfigChangedType.VIEWS == type) {
            viewsEventWasFired = true;
        }
    }
}
