package de.cuioss.portal.ui.runtime.application.dashboard;

import static de.cuioss.portal.configuration.PortalConfigurationKeys.DASHBOARD_WIDGET;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;

import org.jboss.weld.junit5.auto.ActivateScopes;
import org.jboss.weld.junit5.auto.AddBeanClasses;
import org.jboss.weld.junit5.auto.EnableAutoWeld;
import org.junit.jupiter.api.Test;

import de.cuioss.portal.configuration.PortalConfigurationSource;
import de.cuioss.portal.core.test.junit5.EnablePortalConfiguration;
import de.cuioss.portal.core.test.mocks.configuration.PortalTestConfiguration;
import de.cuioss.portal.ui.runtime.application.dashboard.support.TestWidgetWithIdA;
import de.cuioss.portal.ui.runtime.application.dashboard.support.TestWidgetWithIdB;
import de.cuioss.portal.ui.runtime.application.dashboard.support.TestWidgetWithoutId;
import de.cuioss.portal.ui.runtime.application.view.matcher.ViewMatcherProducer;
import de.cuioss.test.valueobjects.junit5.contracts.ShouldHandleObjectContracts;
import lombok.Getter;

@EnableAutoWeld
@EnablePortalConfiguration
@AddBeanClasses({ ViewMatcherProducer.class, TestWidgetWithIdA.class, TestWidgetWithIdB.class,
        TestWidgetWithoutId.class })
@ActivateScopes({ ViewScoped.class })
class DashboardWidgetRegistrationTest implements ShouldHandleObjectContracts<DashboardWidgetRegistration> {

    @Inject
    @Getter
    private DashboardWidgetRegistration underTest;

    @Inject
    @PortalConfigurationSource
    private PortalTestConfiguration configuration;

    @Test
    void testWidgetsWithoutConfig() {
        configuration.fireEvent();

        assertNotNull(underTest.getWidgets());
        assertTrue(underTest.getWidgets().isEmpty());
    }

    @Test
    void testWidgetsWithConfigForA() {
        configuration.put(DASHBOARD_WIDGET + "A.order", "10");
        configuration.fireEvent();

        assertNotNull(underTest.getWidgets());
        assertEquals(1, underTest.getWidgets().size());
        assertEquals("A", underTest.getWidgets().get(0).getId());
    }

    @Test
    void testWidgetsWithConfigForAAndB() {
        configuration.put(DASHBOARD_WIDGET + "A.order", "10");
        configuration.put(DASHBOARD_WIDGET + "B.order", "20");
        configuration.fireEvent();

        assertNotNull(underTest.getWidgets());
        assertEquals(2, underTest.getWidgets().size());
        assertEquals("A", underTest.getWidgets().get(0).getId());
        assertEquals("B", underTest.getWidgets().get(1).getId());
    }

}
