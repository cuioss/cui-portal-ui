package de.cuioss.portal.ui.test.junit5;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Provider;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.junit.jupiter.api.Test;

import de.cuioss.portal.configuration.PortalConfigurationKeys;
import de.cuioss.portal.configuration.PortalConfigurationSource;
import de.cuioss.portal.core.test.mocks.configuration.PortalTestConfiguration;
import de.cuioss.test.jsf.defaults.BasicApplicationConfiguration;

@EnablePortalUiEnvironment
class EnablePortalUiEnvironmentTest {

    @Inject
    private Provider<FacesContext> facesContext;

    @Inject
    @ConfigProperty(name = PortalConfigurationKeys.CLIENT_STORAGE_COOKIE_MAXAGE)
    private Provider<String> attribute;

    @Inject
    @PortalConfigurationSource
    private PortalTestConfiguration configuration;

    @Test
    void shouldEnableEnvironment() {
        assertNotNull(facesContext.get());
        // assertNotNull(attribute.get());
        assertNotNull(configuration);
        assertEquals(BasicApplicationConfiguration.FIREFOX, facesContext.get().getExternalContext()
                .getRequestHeaderMap().get(BasicApplicationConfiguration.USER_AGENT));
    }

}
