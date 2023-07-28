package de.cuioss.portal.ui.runtime.application.listener.view;

import static de.cuioss.portal.ui.test.configuration.PortalNavigationConfiguration.DESCRIPTOR_PREFERENCES;
import static de.cuioss.portal.ui.test.configuration.PortalNavigationConfiguration.VIEW_HOME_LOGICAL_VIEW_ID;
import static de.cuioss.portal.ui.test.configuration.PortalNavigationConfiguration.VIEW_PREFERENCES_LOGICAL_VIEW_ID;
import static de.cuioss.portal.ui.test.configuration.PortalNavigationConfiguration.VIEW_PREFERENCES_VIEW_ID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.inject.Inject;

import org.jboss.weld.junit5.auto.AddBeanClasses;
import org.junit.jupiter.api.Test;

import de.cuioss.jsf.api.application.history.HistoryManager;
import de.cuioss.portal.ui.api.history.PortalHistoryManager;
import de.cuioss.portal.ui.api.listener.view.PhaseExecution;
import de.cuioss.portal.ui.api.listener.view.PortalRestoreViewListener;
import de.cuioss.portal.ui.test.junit5.EnablePortalUiEnvironment;
import de.cuioss.portal.ui.test.mocks.PortalHistoryManagerMock;
import de.cuioss.test.jsf.util.JsfEnvironmentConsumer;
import de.cuioss.test.jsf.util.JsfEnvironmentHolder;
import de.cuioss.test.valueobjects.junit5.contracts.ShouldHandleObjectContracts;
import lombok.Getter;
import lombok.Setter;

@EnablePortalUiEnvironment
@AddBeanClasses({ PortalHistoryManagerMock.class })
class HistoryManagerListenerTest
        implements ShouldHandleObjectContracts<HistoryManagerListener>, JsfEnvironmentConsumer {

    @Setter
    @Getter
    private JsfEnvironmentHolder environmentHolder;

    @Inject
    @PortalRestoreViewListener(PhaseExecution.AFTER_PHASE_EXCLUDE_POSTBACK)
    @Getter
    private HistoryManagerListener underTest;

    @Inject
    @PortalHistoryManager
    private HistoryManager historyManager;

    @Test
    void shouldAddToHistory() {
        // Should start on home
        assertEquals(VIEW_HOME_LOGICAL_VIEW_ID, historyManager.getCurrentView().getViewId());
        getRequestConfigDecorator().setViewId(VIEW_PREFERENCES_LOGICAL_VIEW_ID);
        underTest.handleView(DESCRIPTOR_PREFERENCES);
        assertEquals(VIEW_PREFERENCES_LOGICAL_VIEW_ID, historyManager.getCurrentView().getViewId());
    }

    @Test
    void shouldDetectPageReload() {
        getRequestConfigDecorator().setViewId(VIEW_PREFERENCES_VIEW_ID);
        underTest.handleView(DESCRIPTOR_PREFERENCES);

        assertFalse(historyManager.isPageReload());

        underTest.handleView(DESCRIPTOR_PREFERENCES);

        assertTrue(historyManager.isPageReload());
    }

}
