package de.cuioss.portal.ui.runtime.application.history;

import de.cuioss.portal.ui.api.history.HistoryManager;
import de.cuioss.portal.ui.api.pages.HomePage;
import de.cuioss.portal.ui.runtime.application.view.matcher.ViewMatcherProducer;
import de.cuioss.portal.ui.test.junit5.EnablePortalUiEnvironment;
import de.cuioss.test.jsf.util.JsfEnvironmentConsumer;
import de.cuioss.test.jsf.util.JsfEnvironmentHolder;
import jakarta.faces.application.ConfigurableNavigationHandler;
import jakarta.faces.lifecycle.ClientWindowScoped;
import jakarta.inject.Inject;
import lombok.Getter;
import lombok.Setter;
import org.jboss.weld.junit5.auto.ActivateScopes;
import org.jboss.weld.junit5.auto.AddBeanClasses;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static de.cuioss.portal.ui.runtime.application.history.HistoryManagerBeanTest.CURRENT_VIEW;
import static de.cuioss.portal.ui.runtime.application.history.HistoryManagerBeanTest.SECOND_VIEW;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@EnablePortalUiEnvironment
@AddBeanClasses({DefaultHistoryConfiguration.class, HistoryManagerBean.class, ViewMatcherProducer.class})
@ActivateScopes(ClientWindowScoped.class)
class HistoryNavigationHandlerTest implements JsfEnvironmentConsumer {

    public static final String PORTAL_HOME_JSF = "/portal/home.jsf";
    private HistoryNavigationHandler underTest;

    @Setter
    @Getter
    private JsfEnvironmentHolder environmentHolder;

    @Inject
    private HistoryManager historyManager;

    @BeforeEach
    void setupUnderTest() {
        underTest = new HistoryNavigationHandler(
                (ConfigurableNavigationHandler) getApplication().getNavigationHandler());
        historyManager.addCurrentUriToHistory(CURRENT_VIEW);
        historyManager.addCurrentUriToHistory(SECOND_VIEW);
    }

    @Test
    void shouldHandleDefaultNavigation() {
        assertEquals(CURRENT_VIEW.getLogicalViewId(), historyManager.peekPrevious().getViewId());
        underTest.handleNavigation(getFacesContext(), null, HomePage.OUTCOME);
        assertNavigatedWithOutcome(HomePage.OUTCOME);
        assertEquals(CURRENT_VIEW.getLogicalViewId(), historyManager.peekPrevious().getViewId(),
                "Should not change history");
    }

    @Test
    void shouldHandleBackNavigation() {
        assertEquals(CURRENT_VIEW.getLogicalViewId(), historyManager.peekPrevious().getViewId());
        underTest.handleNavigation(getFacesContext(), null, "back");

        assertRedirect(CURRENT_VIEW.getViewId());
        assertEquals(PORTAL_HOME_JSF, historyManager.peekPrevious().getViewId(), "Should change history");
    }

    @Test
    void shouldHandleDefaultNavigationCase() {
        assertEquals(CURRENT_VIEW.getLogicalViewId(), historyManager.peekPrevious().getViewId());
        var navigationCase = underTest.getNavigationCase(getFacesContext(), null, HomePage.OUTCOME);
        assertNotNull(navigationCase);
        assertEquals(PORTAL_HOME_JSF, navigationCase.getToViewId(getFacesContext()));
        assertEquals(CURRENT_VIEW.getLogicalViewId(), historyManager.peekPrevious().getViewId(),
                "Should not change history");
    }

    @Test
    void shouldHandleBackNavigationCase() {
        assertEquals(CURRENT_VIEW.getLogicalViewId(), historyManager.peekPrevious().getViewId());
        var navigationCase = underTest.getNavigationCase(getFacesContext(), null, "back");
        assertNotNull(navigationCase);
        assertEquals(CURRENT_VIEW.getLogicalViewId(), navigationCase.getToViewId(getFacesContext()));
        assertEquals(CURRENT_VIEW.getLogicalViewId(), historyManager.peekPrevious().getViewId(),
                "Should not change history");
    }
}