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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import jakarta.faces.application.Resource;
import jakarta.inject.Inject;

import org.apache.myfaces.test.mock.MockServletContext;
import org.jboss.weld.junit5.auto.AddBeanClasses;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.cuioss.portal.configuration.PortalConfigurationKeys;
import de.cuioss.portal.configuration.PortalConfigurationSource;
import de.cuioss.portal.core.test.mocks.configuration.PortalTestConfiguration;
import de.cuioss.portal.ui.api.resources.CacheableResource;
import de.cuioss.portal.ui.test.junit5.EnablePortalUiEnvironment;
import de.cuioss.test.jsf.mocks.CuiMockResource;
import de.cuioss.test.jsf.mocks.CuiMockResourceHandler;
import de.cuioss.test.jsf.util.JsfEnvironmentConsumer;
import de.cuioss.test.jsf.util.JsfEnvironmentHolder;
import de.cuioss.test.valueobjects.junit5.contracts.ShouldBeNotNull;
import de.cuioss.tools.io.IOStreams;
import lombok.Getter;
import lombok.Setter;

@EnablePortalUiEnvironment
@AddBeanClasses({ CustomizationResourceProducer.class })
class CustomizationResourceHandlerTest
        implements ShouldBeNotNull<CustomizationResourceHandler>, JsfEnvironmentConsumer {

    private static final String TEST_LIBRARY = "a";
    private static final String TEST_RESOURCE = "some_style.css";
    private static final String MOCK_LIB = "mock_lib";

    @Setter
    @Getter
    private JsfEnvironmentHolder environmentHolder;

    @Getter
    private CustomizationResourceHandler underTest;

    @Inject
    @PortalConfigurationSource
    private PortalTestConfiguration configuration;

    @BeforeEach
    void before() {
        configuration.put(PortalConfigurationKeys.PORTAL_CUSTOMIZATION_DIR, "src/test/resources/customization");
        configuration.put(PortalConfigurationKeys.PORTAL_CUSTOMIZATION_ENABLED, "true");
        configuration.put(PortalConfigurationKeys.RESOURCE_MAXAGE, "60");
        configuration.fireEvent();

        final var wrapped = new CuiMockResourceHandler();

        final Map<String, CuiMockResource> mockResources = new HashMap<>();
        mockResources.put(MOCK_LIB + CuiMockResourceHandler.LIBRARY_RESOURCE_DELIMITER + TEST_RESOURCE,
                new CuiMockResource());

        wrapped.setAvailableResouces(mockResources);
        underTest = new CustomizationResourceHandler(wrapped);
        ((MockServletContext) getExternalContext().getContext()).addMimeType("txt", "text/plain");
        ((MockServletContext) getExternalContext().getContext()).addMimeType("css", "text/css");
    }

    @Test
    void shouldIgnoreNotHandledResources() {
        final var resolved = getUnderTest().createResource(TEST_RESOURCE, MOCK_LIB);
        assertNotNull(resolved);
        assertTrue(resolved instanceof CuiMockResource);
    }

    @Test
    void shouldHandledMatchingLibraryName() throws IOException {
        final var resolved = getUnderTest().createResource(TEST_RESOURCE, TEST_LIBRARY);
        checkResource(resolved);
    }

    @Test
    void productionShouldUseCache() throws IOException {
        configuration.fireEvent(PortalConfigurationKeys.PORTAL_STAGE, "production");
        final var resolved = getUnderTest().createResource(TEST_RESOURCE, TEST_LIBRARY);
        final var resolved2 = getUnderTest().createResource(TEST_RESOURCE, TEST_LIBRARY);
        assertSame(resolved, resolved2);
        checkResource(resolved2);
    }

    @Test
    void developmentShouldNotUseCache() throws IOException {
        configuration.put(PortalConfigurationKeys.PORTAL_STAGE, "development");
        configuration.fireEvent();
        final var resolved = getUnderTest().createResource(TEST_RESOURCE, TEST_LIBRARY);
        final var resolved2 = getUnderTest().createResource(TEST_RESOURCE, TEST_LIBRARY);
        assertNotSame(resolved, resolved2);
        assertEquals(resolved, resolved2);
        checkResource(resolved2);
    }

    private static void checkResource(final Resource resolved) throws IOException {
        assertNotNull(resolved, "Resource is null");
        assertTrue(resolved instanceof CacheableResource, "Resource should be instance of CacheableResource");
        assertNotNull(resolved.getContentType(), "Resource ContentType is missing");
        assertNotNull(resolved.getResourceName(), "Resource ResourceName is missing");
        assertNotNull(resolved.getLibraryName(), "Resource LibraryName is missing");
        assertNotNull(resolved.getResponseHeaders(), "Resource ResponseHeaders is missing");
        assertNotNull(resolved.getInputStream(), "Resource InputStream is missing");
    }

    @Test
    void shouldHandledMatchingResourceNameIncludingLibraryName() throws IOException {
        final var resolved = getUnderTest().createResource(TEST_LIBRARY + "/" + TEST_RESOURCE, null);
        checkResource(resolved);
    }

    @Test
    void shouldHandledMatchingResourceNameIncludingLibraryNameWithoutResourceName() {
        final var resolved = getUnderTest().createResource(TEST_LIBRARY + "/", null);
        assertNull(resolved);
    }

    @Test
    void shouldHandleResourceNameNullCorrectly() {
        final var resolved = getUnderTest().createResource(null, MOCK_LIB);
        assertNull(resolved);
    }

    @Test
    void shouldHandleLibraryNameNullCorrectly() {
        final var resolved = getUnderTest().createResource(TEST_RESOURCE, null);
        assertNull(resolved);
    }

    @Test
    void shouldHandleNullValuesCorrectly() {
        final var resolved = getUnderTest().createResource(null, null);
        assertNull(resolved);
    }

    @Test
    void shouldUseMinifiedArtifactIfAvailable() throws IOException {

        // ask for some_style.css, but retrieve minified version
        final var resolved = getUnderTest().createResource(TEST_RESOURCE, TEST_LIBRARY);
        assertNotNull(resolved);

        final var expected = IOStreams
                .toByteArray(loadResourceFromClasspath("/customization/resources/a/some_style.min.css"));

        final var actual = IOStreams.toByteArray(resolved.getInputStream());

        assertEquals(expected.length, actual.length, "Retrieved content is not minified. Size is wrong!");

        // verify expanded resources still retrieved

        final var javaScript = getUnderTest().createResource("example.js", TEST_LIBRARY);

        final var expectedJavascriptContent = IOStreams
                .toByteArray(loadResourceFromClasspath("/customization/resources/a/example.js"));

        final var actualJavascriptContent = IOStreams.toByteArray(javaScript.getInputStream());

        assertEquals(expectedJavascriptContent.length, actualJavascriptContent.length, "Retrieved content is wrong!");

    }

    private InputStream loadResourceFromClasspath(final String resource) {

        final var resourceAsStream = this.getClass().getResourceAsStream(resource);

        assertNotNull(resourceAsStream, "Could not load >" + resource + "< resource from classpath");

        return resourceAsStream;
    }
}
