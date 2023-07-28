package de.cuioss.portal.ui.api.menu;

import static de.cuioss.portal.configuration.PortalConfigurationKeys.MENU_BASE;
import static de.cuioss.portal.configuration.PortalConfigurationKeys.MENU_TOP_IDENTIFIER;
import static de.cuioss.portal.ui.api.menu.MockPortalNavigationMenuItemImplBase.ID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.inject.Inject;
import javax.inject.Provider;

import org.jboss.weld.junit5.auto.AddBeanClasses;
import org.jboss.weld.junit5.auto.EnableAutoWeld;
import org.junit.jupiter.api.Test;

import de.cuioss.portal.configuration.PortalConfigurationSource;
import de.cuioss.portal.core.test.junit5.EnablePortalConfiguration;
import de.cuioss.portal.core.test.mocks.configuration.PortalTestConfiguration;
import de.cuioss.test.jsf.junit5.EnableJsfEnvironment;
import de.cuioss.test.jsf.producer.JsfObjectsProducers;
import lombok.Getter;

@EnableAutoWeld
@AddBeanClasses({ JsfObjectsProducers.class })
@EnableJsfEnvironment
@EnablePortalConfiguration
class PortalNavigationMenuItemImplBaseTest {

    private static final String BASE = MENU_BASE + ID + ".";

    private static final String ENABLED = BASE + "enabled";
    private static final String ORDER = BASE + "order";
    private static final String PARENT = BASE + "parent";

    private static final Integer DEFAULT_ORDER = 10;
    private static final String DEFAULT_PARENT = MENU_TOP_IDENTIFIER;

    @Inject
    @Getter
    private MockPortalNavigationMenuItemImplBase underTest;

    @Inject
    private Provider<MockPortalNavigationMenuItemImplBase> underTestProvider;

    @Inject
    @PortalConfigurationSource
    private PortalTestConfiguration configuration;

    @Test
    void shouldHandleMissingConfigurationGracefully() {
        assertNull(underTest.getParentId());
        assertEquals(Integer.valueOf(-1), underTest.getOrder());
        assertNull(underTest.getIconStyleClass());
        assertNull(underTest.getResolvedLabel());
        assertNull(underTest.getResolvedTitle());
        assertFalse(underTest.isRendered());
    }

    @Test
    void shouldHandleConfiguration() {
        configuration.fireEvent(ENABLED, "true", ORDER, DEFAULT_ORDER.toString());
        configuration.fireEvent(PARENT, DEFAULT_PARENT);

        underTest = underTestProvider.get();
        assertEquals(DEFAULT_PARENT, underTest.getParentId());
        assertEquals(DEFAULT_ORDER, underTest.getOrder());
        assertTrue(underTest.isRendered());
    }
}
