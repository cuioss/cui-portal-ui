package de.cuioss.portal.ui.runtime.application.customization;

import static de.cuioss.portal.configuration.PortalConfigurationKeys.PORTAL_CUSTOMIZATION_DIR;
import static de.cuioss.portal.configuration.PortalConfigurationKeys.PORTAL_CUSTOMIZATION_ENABLED;
import static de.cuioss.tools.collect.CollectionLiterals.mutableSet;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;

import javax.enterprise.context.Dependent;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.jboss.weld.junit5.auto.AddBeanClasses;
import org.jboss.weld.junit5.auto.EnableAlternatives;
import org.jboss.weld.junit5.auto.ExcludeBeanClasses;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.cuioss.portal.configuration.PortalConfigurationSource;
import de.cuioss.portal.configuration.schedule.FileChangedEvent;
import de.cuioss.portal.configuration.schedule.FileWatcherService;
import de.cuioss.portal.configuration.schedule.PortalFileWatcherService;
import de.cuioss.portal.core.locale.PortalLocale;
import de.cuioss.portal.core.test.mocks.configuration.PortalTestConfiguration;
import de.cuioss.portal.ui.runtime.support.FileWatcherServiceMock;
import de.cuioss.portal.ui.test.junit5.EnablePortalUiEnvironment;
import de.cuioss.portal.ui.test.mocks.PortalLocaleProducerMock;
import de.cuioss.test.juli.LogAsserts;
import de.cuioss.test.juli.TestLogLevel;
import de.cuioss.test.juli.junit5.EnableTestLogger;
import de.cuioss.test.valueobjects.junit5.contracts.ShouldHandleObjectContracts;
import lombok.Getter;
import lombok.Setter;

@EnablePortalUiEnvironment
@AddBeanClasses({ FileWatcherServiceMock.class })
@EnableAlternatives({ FileWatcherServiceMock.class })
@ExcludeBeanClasses({ PortalLocaleProducerMock.class })
@EnableTestLogger(trace = { CustomizationResourceBundleWrapper.class })
class CustomizationResourceBundleWrapperTest
        implements ShouldHandleObjectContracts<CustomizationResourceBundleWrapper> {

    private static final String CUSTOMIZATION_DIR = "src/test/resources/customization";

    private static final Path CUSTOMIZATION_DIR_PATH = Paths.get(CUSTOMIZATION_DIR);

    @Inject
    @Getter
    private CustomizationResourceBundleWrapper underTest;

    @Getter
    @Setter
    @Produces
    @Dependent
    @PortalLocale
    private Locale locale = Locale.ENGLISH;

    @Inject
    @PortalFileWatcherService
    private FileWatcherService fileWatcherService;

    @Inject
    @FileChangedEvent
    private Event<Path> fileChangeEvent;

    @Inject
    @PortalConfigurationSource
    private PortalTestConfiguration configuration;

    @BeforeEach
    void before() {
        configuration.put(PORTAL_CUSTOMIZATION_ENABLED, "true");
        configuration.put(PORTAL_CUSTOMIZATION_DIR, CUSTOMIZATION_DIR_PATH.toString());
        configuration.fireEvent();
    }

    @Test
    void shouldHandleEnglishBundle() {
        assertEquals(mutableSet("a", "c"), underTest.keySet());
        assertEquals("b", underTest.getMessage("a"));
    }

    @Test
    void shouldHandleGermanBundle() {
        locale = Locale.GERMAN;
        assertEquals(mutableSet("a", "c", "e", "g"), underTest.keySet());
        assertEquals("z", underTest.getMessage("a"));
    }

    @Test
    void shouldHandleGermanyBundle() {
        locale = Locale.GERMANY;
        assertEquals(mutableSet("a", "c", "e", "g"), underTest.keySet());
        assertEquals("z", underTest.getMessage("a"));
    }

    @Test
    void shouldHandleChangeEvent() {
        configuration.fireEvent(PORTAL_CUSTOMIZATION_DIR, "");
        assertEquals(mutableSet(), underTest.keySet());
        locale = Locale.GERMANY;
        configuration.fireEvent(PORTAL_CUSTOMIZATION_DIR, CUSTOMIZATION_DIR_PATH.toString());
        assertEquals(mutableSet("a", "c", "e", "g"), underTest.keySet());
        assertEquals("z", underTest.getMessage("a"));
    }

    @Test
    void shouldHandleNewFile() throws Exception {
        locale = Locale.ENGLISH;
        final var path = Paths.get("target/test-files/");
        createDir(path);
        final var i18nPath = path.resolve("i18n");
        createDir(i18nPath);
        final var newMessagePath = i18nPath.resolve("messages.properties");

        if (newMessagePath.toFile().exists()) {
            Files.delete(newMessagePath);
        }

        configuration.fireEvent(PORTAL_CUSTOMIZATION_DIR, path.toString());

        // Initializes lazy
        assertFalse(fileWatcherService.getRegisteredPaths().contains(i18nPath));
        underTest.keySet();
        assertTrue(fileWatcherService.getRegisteredPaths().contains(i18nPath));

        assertEquals(mutableSet(), underTest.keySet());

        var writer = new PrintWriter(newMessagePath.toFile(), "UTF-8");
        writer.println("a = i");
        writer.close();

        fileChangeEvent.fire(i18nPath);
        assertEquals("i", underTest.getMessage("a"));
        Files.delete(newMessagePath);

        LogAsserts.assertLogMessagePresentContaining(TestLogLevel.INFO, "Portal-018");
    }

    @Test
    void changeEventOnNonExistingPath() {
        underTest.getMessage("test");
        fileChangeEvent.fire(Paths.get("b00m"));
        LogAsserts.assertNoLogMessagePresent(TestLogLevel.WARN, "Portal-123");
    }

    private static void createDir(final Path path) throws IOException {
        if (!Files.exists(path)) {
            Files.createDirectory(path);
            path.toFile().deleteOnExit();
        }
    }
}
