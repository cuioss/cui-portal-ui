package de.cuioss.portal.ui.runtime.application.history;

import static de.cuioss.portal.ui.test.configuration.PortalNavigationConfiguration.DESCRIPTOR_HOME;
import static de.cuioss.portal.ui.test.configuration.PortalNavigationConfiguration.DESCRIPTOR_LOGIN;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.inject.Inject;
import javax.inject.Provider;

import org.jboss.weld.junit5.auto.AddBeanClasses;
import org.jboss.weld.junit5.auto.EnableAutoWeld;
import org.junit.jupiter.api.Test;

import de.cuioss.portal.core.test.junit5.EnablePortalConfiguration;
import de.cuioss.portal.ui.api.ui.pages.HomePage;
import de.cuioss.portal.ui.runtime.application.view.matcher.ViewMatcherProducer;
import de.cuioss.test.valueobjects.junit5.contracts.ShouldHandleObjectContracts;

@EnableAutoWeld
@EnablePortalConfiguration
@AddBeanClasses({ DefaultHistoryConfiguration.class, ViewMatcherProducer.class })
class DefaultHistoryConfigurationTest implements ShouldHandleObjectContracts<DefaultHistoryConfiguration> {

    @Inject
    private Provider<DefaultHistoryConfiguration> underTest;

    /**
     * Tests, whether the default values are correct. This is handy for detecting
     * accidental changes in the configuration
     */
    @Test
    void shouldProvideDefaultConfiguration() {
        final var configuration = underTest.get();
        assertNotNull(configuration);
        assertTrue(configuration.isExcludeFacesParameter());
        assertEquals(3, configuration.getExcludeParameter().size());
        assertNull(configuration.getFallback());
        assertEquals(HomePage.OUTCOME, configuration.getFallbackOutcome());
        assertTrue(configuration.getExcludeFromHistoryMatcher().match(DESCRIPTOR_LOGIN));
        assertFalse(configuration.getExcludeFromHistoryMatcher().match(DESCRIPTOR_HOME));
    }

    @Override
    public DefaultHistoryConfiguration getUnderTest() {
        return underTest.get();
    }
}
