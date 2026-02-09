/*
 * Copyright © 2025 CUI-OpenSource-Software (info@cuioss.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.cuioss.portal.ui.runtime.application.listener.view;

import de.cuioss.portal.ui.api.context.CurrentViewProducer;
import de.cuioss.portal.ui.api.listener.view.PhaseExecution;
import de.cuioss.portal.ui.api.listener.view.PortalRestoreViewListener;
import de.cuioss.portal.ui.runtime.application.listener.view.testhelper.AfterViewListener;
import de.cuioss.portal.ui.runtime.application.listener.view.testhelper.AfterViewNoPostbackListener;
import de.cuioss.portal.ui.runtime.application.listener.view.testhelper.BeforeViewListener;
import de.cuioss.portal.ui.test.junit5.EnablePortalUiEnvironment;
import de.cuioss.test.jsf.config.RequestConfigurator;
import de.cuioss.test.jsf.config.decorator.RequestConfigDecorator;
import de.cuioss.test.jsf.util.JsfEnvironmentConsumer;
import de.cuioss.test.jsf.util.JsfEnvironmentHolder;
import de.cuioss.test.valueobjects.junit5.contracts.ShouldHandleObjectContracts;
import jakarta.faces.event.PhaseEvent;
import jakarta.faces.event.PhaseId;
import jakarta.inject.Inject;
import lombok.Getter;
import lombok.Setter;
import org.apache.myfaces.test.mock.lifecycle.MockLifecycle;
import org.jboss.weld.junit5.auto.AddBeanClasses;
import org.junit.jupiter.api.Test;

import static de.cuioss.portal.ui.test.configuration.PortalNavigationConfiguration.VIEW_LOGIN_LOGICAL_VIEW_ID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@EnablePortalUiEnvironment
@AddBeanClasses({CurrentViewProducer.class, AfterViewListener.class})
class PortalCDIViewListenerTest
        implements ShouldHandleObjectContracts<PortalCDIViewListener>, JsfEnvironmentConsumer, RequestConfigurator {

    @Setter
    @Getter
    private JsfEnvironmentHolder environmentHolder;

    @Inject
    @Getter
    private PortalCDIViewListener underTest;

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
