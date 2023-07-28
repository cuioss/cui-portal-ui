package de.cuioss.portal.ui.runtime.application.customization;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;

import javax.inject.Inject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.cuioss.portal.configuration.PortalConfigurationKeys;
import de.cuioss.portal.configuration.PortalConfigurationSource;
import de.cuioss.portal.core.test.mocks.configuration.PortalTestConfiguration;
import de.cuioss.portal.ui.test.junit5.EnablePortalUiEnvironment;
import de.cuioss.test.valueobjects.junit5.contracts.ShouldBeNotNull;
import de.cuioss.tools.string.MoreStrings;
import lombok.Getter;

@EnablePortalUiEnvironment
class CustomizationResourceTest implements ShouldBeNotNull<CustomizationResource> {

    @Getter
    private CustomizationResource underTest;

    @Inject
    @PortalConfigurationSource
    private PortalTestConfiguration configuration;

    @BeforeEach
    void before() throws IOException {
        configuration.fireEvent(PortalConfigurationKeys.RESOURCE_MAXAGE, "60");

        underTest = new CustomizationResource(File.createTempFile("application-default", ".css"),
                "application-default.css", "vendor", "application/css");
    }

    @Test
    void testCustomizationResource() throws IOException {
        assertFalse(MoreStrings.isEmpty(underTest.getETag()));
        var headers = underTest.getResponseHeaders();
        assertNotNull(headers);
        assertTrue(headers.containsKey("Expires"));

        assertFalse(MoreStrings.isEmpty(headers.get("Expires")));
        assertTrue(headers.containsKey("Last-Modified"));
        assertFalse(MoreStrings.isEmpty(headers.get("Last-Modified")));
        assertNotNull(underTest.getInputStream());
        assertEquals("vendor", underTest.getLibraryName());
        assertEquals("application-default.css", underTest.getResourceName());
        assertEquals("application/css", underTest.getContentType());
        assertEquals("/javax.faces.resource/vendor/application-default.css", underTest.getRequestPath());
    }

}
