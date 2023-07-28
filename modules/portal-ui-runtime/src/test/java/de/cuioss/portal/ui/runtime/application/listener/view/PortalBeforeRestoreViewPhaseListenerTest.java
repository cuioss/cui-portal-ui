package de.cuioss.portal.ui.runtime.application.listener.view;

import static de.cuioss.portal.ui.test.configuration.PortalNavigationConfiguration.VIEW_LOGIN_LOGICAL_VIEW_ID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.inject.Inject;

import org.apache.myfaces.test.mock.lifecycle.MockLifecycle;
import org.jboss.weld.junit5.auto.AddBeanClasses;
import org.junit.jupiter.api.Test;

import de.cuioss.portal.ui.api.listener.view.PhaseExecution;
import de.cuioss.portal.ui.api.listener.view.PortalRestoreViewListener;
import de.cuioss.portal.ui.api.ui.context.CurrentViewProducer;
import de.cuioss.portal.ui.runtime.application.listener.view.testhelper.AfterViewListener;
import de.cuioss.portal.ui.runtime.application.listener.view.testhelper.AfterViewNoPostbackListener;
import de.cuioss.portal.ui.runtime.application.listener.view.testhelper.BeforeViewListener;
import de.cuioss.portal.ui.test.junit5.EnablePortalUiEnvironment;
import de.cuioss.test.jsf.config.RequestConfigurator;
import de.cuioss.test.jsf.config.decorator.RequestConfigDecorator;
import de.cuioss.test.jsf.util.JsfEnvironmentConsumer;
import de.cuioss.test.jsf.util.JsfEnvironmentHolder;
import de.cuioss.test.valueobjects.junit5.contracts.ShouldHandleObjectContracts;
import lombok.Getter;
import lombok.Setter;

@EnablePortalUiEnvironment
@AddBeanClasses({ CurrentViewProducer.class, AfterViewListener.class })
class PortalBeforeRestoreViewPhaseListenerTest implements ShouldHandleObjectContracts<PortalRestoreViewPhaseListener>,
        JsfEnvironmentConsumer, RequestConfigurator {

    @Setter
    @Getter
    private JsfEnvironmentHolder environmentHolder;

    @Inject
    @Getter
    private PortalRestoreViewPhaseListener underTest;

    @Inject
    @PortalRestoreViewListener(PhaseExecution.AFTER_PHASE)
    private AfterViewListener afterViewListener;

    @Inject
    @PortalRestoreViewListener(PhaseExecution.AFTER_PHASE_EXCLUDE_POSTBACK)
    private AfterViewNoPostbackListener afterViewNoPostbackListener;

    @Inject
    @PortalRestoreViewListener(PhaseExecution.BEFORE_PHASE)
    private BeforeViewListener beforeViewListener;

    @Test
    void shouldCallBeforeView() {
        fireBeforeViewPhaseEvent();
        assertEquals(VIEW_LOGIN_LOGICAL_VIEW_ID, beforeViewListener.getHandledView().getViewId());
        assertNull(afterViewListener.getHandledView());
        assertNull(afterViewNoPostbackListener.getHandledView());
    }

    @Test
    void shouldCallAfterViewNoPostback() {
        fireAfterViewPhaseEvent();
        assertEquals(VIEW_LOGIN_LOGICAL_VIEW_ID, afterViewListener.getHandledView().getViewId());
        assertEquals(VIEW_LOGIN_LOGICAL_VIEW_ID, afterViewNoPostbackListener.getHandledView().getViewId());
        assertNull(beforeViewListener.getHandledView());
    }

    @Test
    void shouldCallAfterViewPostback() {
        getRequestConfigDecorator().setPostback(true);
        fireAfterViewPhaseEvent();
        assertEquals(VIEW_LOGIN_LOGICAL_VIEW_ID, afterViewListener.getHandledView().getViewId());
        assertNull(afterViewNoPostbackListener.getHandledView());
        assertNull(beforeViewListener.getHandledView());
    }

    private void fireBeforeViewPhaseEvent() {
        underTest.beforePhase(new PhaseEvent(getFacesContext(), PhaseId.RESTORE_VIEW, new MockLifecycle()));
    }

    private void fireAfterViewPhaseEvent() {
        underTest.afterPhase(new PhaseEvent(getFacesContext(), PhaseId.RESTORE_VIEW, new MockLifecycle()));
    }

    @Override
    public void configureRequest(RequestConfigDecorator decorator) {
        decorator.setViewId(VIEW_LOGIN_LOGICAL_VIEW_ID);
    }

}
