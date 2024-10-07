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

import de.cuioss.portal.configuration.PortalConfigurationKeys;
import de.cuioss.portal.core.test.junit5.EnablePortalConfiguration;
import de.cuioss.portal.core.test.mocks.configuration.PortalTestConfiguration;
import de.cuioss.portal.ui.test.junit5.EnablePortalUiEnvironment;
import de.cuioss.test.generator.internal.net.java.quickcheck.generator.support.IntegerGenerator;
import de.cuioss.test.jsf.mocks.CuiMockResource;
import de.cuioss.test.jsf.mocks.CuiMockResourceHandler;
import de.cuioss.test.jsf.util.JsfEnvironmentConsumer;
import de.cuioss.test.jsf.util.JsfEnvironmentHolder;
import de.cuioss.tools.string.MoreStrings;
import jakarta.inject.Inject;
import lombok.Getter;
import lombok.Setter;
import org.jboss.weld.junit5.auto.AddBeanClasses;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static de.cuioss.portal.configuration.PortalConfigurationKeys.RESOURCE_VERSION;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@EnablePortalUiEnvironment
@AddBeanClasses({PortalResourceConfiguration.class, CuiResourceManager.class})
@EnablePortalConfiguration(configuration = RESOURCE_VERSION + ":1.0")
class CuiResourceHandlerTest implements JsfEnvironmentConsumer {

    private static final String CSS_LIBRARY = "de.cuioss.portal.css";
    private static final String STYLE_CSS = "style.css";
    private static final String STYLE_MIN_CSS = "style.min.css";
    private static final String ANY_UNKNOWN_JPG = "any_unknown.jpg";
    private static final String ALIEN_LIB = "alien_lib";
    private static final String APPLICATION_CSS = "application.css";
    @Setter
    @Getter
    private JsfEnvironmentHolder environmentHolder;
    @Inject
    private PortalTestConfiguration configuration;

    private CuiResourceHandler underTest;

    private CuiMockResourceHandler mockResourceHandler;

    private static CuiMockResource prepareResource(String name, String lib) {
        return prepareResource(name, lib, null);
    }

    private static CuiMockResource prepareResource(String name, String lib, String versionInfo) {
        var mockResource = new CuiMockResource();
        mockResource.setResourceName(name);
        mockResource.setLibraryName(lib);
        if (MoreStrings.isBlank(versionInfo)) {
            mockResource.setRequestPath("/jakarta.faces.resource/" + name + "?ln=" + lib);
        } else {
            mockResource.setRequestPath("/jakarta.faces.resource/" + name + "?ln=" + lib + "&v=" + versionInfo);
        }
        return mockResource;
    }

    @BeforeEach
    void setUpHandlerTest() {
        setupResourceHandlerMock();
        configuration.production();
        underTest = new CuiResourceHandler(mockResourceHandler);
    }

    void setupResourceHandlerMock() {
        mockResourceHandler = new CuiMockResourceHandler();
        Map<String, CuiMockResource> availableResources = new HashMap<>();
        availableResources.put(CSS_LIBRARY + "-style.css", prepareResource(STYLE_CSS, CSS_LIBRARY));
        availableResources.put(CSS_LIBRARY + "-style.min.css", prepareResource(STYLE_MIN_CSS, CSS_LIBRARY));
        availableResources.put(CSS_LIBRARY + "-application.css",
                prepareResource(APPLICATION_CSS, CSS_LIBRARY, "1.0.0"));
        availableResources.put("alien_lib-any_unknown.jpg", prepareResource(ANY_UNKNOWN_JPG, ALIEN_LIB));
        mockResourceHandler.setAvailableResouces(availableResources);
    }

    @Test
    void testCreateResourceProduction() {
        var resource = underTest.createResource(STYLE_CSS, CSS_LIBRARY);
        assertNotNull(resource);
        assertEquals(CSS_LIBRARY, resource.getLibraryName());
        assertEquals(STYLE_MIN_CSS, resource.getResourceName());
    }

    @Test
    void testCreateResourceDevelopment() {
        configuration.development();
        underTest = new CuiResourceHandler(mockResourceHandler);
        var resource = underTest.createResource(STYLE_CSS, CSS_LIBRARY);
        assertNotNull(resource);
        assertEquals(CSS_LIBRARY, resource.getLibraryName());
        assertEquals(STYLE_CSS, resource.getResourceName());
    }

    @Test
    void shouldUpdateVersionInformation() {
        var version = new IntegerGenerator(1, 100).next().toString();
        configuration.update(PortalConfigurationKeys.RESOURCE_VERSION, version);
        var expectedVersionInfo = "&v=" + version;
        var resource = underTest.createResource(STYLE_CSS, CSS_LIBRARY);
        assertNotNull(resource);
        assertTrue(resource.getRequestPath().endsWith(expectedVersionInfo), "version information was not found");

        var applicationCssResource = underTest.createResource(APPLICATION_CSS, CSS_LIBRARY);
        assertTrue(applicationCssResource.getRequestPath().endsWith("1.0.0"), "version information was not found");
        assertFalse(applicationCssResource.getRequestPath().endsWith(expectedVersionInfo),
                "unexpected version information found");

        var alienResource = underTest.createResource(ANY_UNKNOWN_JPG, ALIEN_LIB);
        assertFalse(alienResource.getRequestPath().endsWith(expectedVersionInfo),
                "unexpected version information found");
    }
}
