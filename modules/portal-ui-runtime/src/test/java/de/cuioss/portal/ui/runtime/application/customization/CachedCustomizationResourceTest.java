package de.cuioss.portal.ui.runtime.application.customization;

import static de.cuioss.test.generator.Generators.letterStrings;

import java.io.File;
import java.io.IOException;

import javax.inject.Inject;

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
