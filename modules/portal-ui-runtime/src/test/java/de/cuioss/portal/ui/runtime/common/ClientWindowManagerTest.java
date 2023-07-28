package de.cuioss.portal.ui.runtime.common;

import static de.cuioss.portal.configuration.PortalConfigurationKeys.PORTAL_SESSION_MAX_INACTIVE_INTERVAL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.inject.Inject;

import org.jboss.weld.junit5.auto.AddBeanClasses;
import org.junit.jupiter.api.Test;

import de.cuioss.portal.configuration.PortalConfigurationSource;
import de.cuioss.portal.core.test.mocks.configuration.PortalTestConfiguration;
import de.cuioss.portal.ui.api.ui.context.CurrentViewProducer;
import de.cuioss.portal.ui.runtime.application.view.ViewTransientManagerBean;
import de.cuioss.portal.ui.runtime.application.view.matcher.ViewMatcherProducer;
import de.cuioss.portal.ui.test.junit5.EnablePortalUiEnvironment;
import de.cuioss.test.valueobjects.junit5.contracts.ShouldHandleObjectContracts;
import lombok.Getter;

@EnablePortalUiEnvironment
@AddBeanClasses({ ViewTransientManagerBean.class, ViewMatcherProducer.class, CurrentViewProducer.class })
class ClientWindowManagerTest implements ShouldHandleObjectContracts<ClientWindowManager> {

    @Inject
    @Getter
    private ClientWindowManager underTest;

    @Inject
    @PortalConfigurationSource
    private PortalTestConfiguration configuration;

    /**
     * Checks whether the correct user is produced.
     */
    @Test
    void shouldHandleMaxInactiveInterval() {
        configuration.fireEvent(PORTAL_SESSION_MAX_INACTIVE_INTERVAL, "10");
        assertEquals(10, underTest.getMaxInactiveInterval());
        assertTrue(underTest.isRenderTimeoutForm());
    }

    /**
     * Checks whether the correct user is produced.
     */
    @Test
    void shouldHandleNullMaxInactiveInterval() {
        configuration.fireEvent(PORTAL_SESSION_MAX_INACTIVE_INTERVAL, "0");
        assertEquals(0, underTest.getMaxInactiveInterval());
        assertFalse(underTest.isRenderTimeoutForm());
    }

}
