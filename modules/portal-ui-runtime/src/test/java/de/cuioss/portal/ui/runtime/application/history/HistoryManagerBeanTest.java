package de.cuioss.portal.ui.runtime.application.history;

import static de.cuioss.portal.ui.test.configuration.PortalNavigationConfiguration.VIEW_HOME_LOGICAL_VIEW_ID;
import static org.junit.jupiter.api.Assertions.assertEquals;

import javax.inject.Inject;

import org.jboss.weld.junit5.auto.AddBeanClasses;
import org.junit.jupiter.api.Test;

import de.cuioss.portal.ui.api.history.PortalHistoryManager;
import de.cuioss.portal.ui.runtime.application.view.matcher.ViewMatcherProducer;
import de.cuioss.portal.ui.test.junit5.EnablePortalUiEnvironment;
import de.cuioss.test.valueobjects.junit5.contracts.ShouldHandleObjectContracts;
import lombok.Getter;

@EnablePortalUiEnvironment
@AddBeanClasses({ DefaultHistoryConfiguration.class, ViewMatcherProducer.class })
class HistoryManagerBeanTest implements ShouldHandleObjectContracts<HistoryManagerBean> {

    @Inject
    @PortalHistoryManager
    @Getter
    private HistoryManagerBean underTest;

    /**
     * Checks the default configuration
     */
    @Test
    void shouldBeConfiguredProperly() {
        assertEquals(VIEW_HOME_LOGICAL_VIEW_ID, underTest.getCurrentView().getViewId());
    }

}
