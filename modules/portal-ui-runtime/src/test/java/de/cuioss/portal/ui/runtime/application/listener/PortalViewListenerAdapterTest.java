package de.cuioss.portal.ui.runtime.application.listener;

import static de.cuioss.portal.ui.test.configuration.PortalNavigationConfiguration.VIEW_LOGIN_LOGICAL_VIEW_ID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
import de.cuioss.portal.ui.runtime.application.listener.view.PortalCDIViewListener;
import de.cuioss.portal.ui.runtime.application.listener.view.testhelper.AfterViewListener;
import de.cuioss.portal.ui.runtime.application.listener.view.testhelper.BeforeViewListener;
import de.cuioss.portal.ui.test.junit5.EnablePortalUiEnvironment;
import de.cuioss.test.jsf.util.JsfEnvironmentConsumer;
import de.cuioss.test.jsf.util.JsfEnvironmentHolder;
import lombok.Getter;
import lombok.Setter;

@EnablePortalUiEnvironment
@AddBeanClasses({ CurrentViewProducer.class, AfterViewListener.class, PortalCDIViewListener.class })
class PortalViewListenerAdapterTest implements JsfEnvironmentConsumer {

    @Setter
    @Getter
    private JsfEnvironmentHolder environmentHolder;

    @Inject
    @PortalRestoreViewListener(PhaseExecution.AFTER_PHASE)
    private AfterViewListener afterViewListener;

    @Inject
    @PortalRestoreViewListener(PhaseExecution.BEFORE_PHASE)
    private BeforeViewListener beforeViewListener;

    @Test
    void shouldHandlePhaseListener() {
        PortalViewListenerAdapter underTest = new PortalViewListenerAdapter();

        getRequestConfigDecorator().setViewId(VIEW_LOGIN_LOGICAL_VIEW_ID);
        underTest.beforePhase(new PhaseEvent(getFacesContext(), PhaseId.RESTORE_VIEW, new MockLifecycle()));

        assertNotNull(beforeViewListener.getHandledView());
        assertNull(afterViewListener.getHandledView());

        underTest.afterPhase(new PhaseEvent(getFacesContext(), PhaseId.RESTORE_VIEW, new MockLifecycle()));
        assertNotNull(afterViewListener.getHandledView());

    }

    @Test
    void shouldStickToRestoreView() {
        assertEquals(PhaseId.RESTORE_VIEW, new PortalViewListenerAdapter().getPhaseId());
    }

}
