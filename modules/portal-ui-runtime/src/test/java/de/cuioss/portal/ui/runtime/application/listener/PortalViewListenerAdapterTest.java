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
package de.cuioss.portal.ui.runtime.application.listener;

import de.cuioss.portal.ui.api.context.CurrentViewProducer;
import de.cuioss.portal.ui.api.listener.view.PhaseExecution;
import de.cuioss.portal.ui.api.listener.view.PortalRestoreViewListener;
import de.cuioss.portal.ui.runtime.application.listener.view.PortalCDIViewListener;
import de.cuioss.portal.ui.runtime.application.listener.view.testhelper.AfterViewListener;
import de.cuioss.portal.ui.runtime.application.listener.view.testhelper.BeforeViewListener;
import de.cuioss.portal.ui.test.junit5.EnablePortalUiEnvironment;
import de.cuioss.test.jsf.util.JsfEnvironmentConsumer;
import de.cuioss.test.jsf.util.JsfEnvironmentHolder;
import jakarta.faces.event.PhaseEvent;
import jakarta.faces.event.PhaseId;
import jakarta.inject.Inject;
import lombok.Getter;
import lombok.Setter;
import org.apache.myfaces.test.mock.lifecycle.MockLifecycle;
import org.jboss.weld.junit5.auto.AddBeanClasses;
import org.junit.jupiter.api.Test;

import static de.cuioss.portal.ui.test.configuration.PortalNavigationConfiguration.VIEW_LOGIN_LOGICAL_VIEW_ID;
import static org.junit.jupiter.api.Assertions.*;

@EnablePortalUiEnvironment
@AddBeanClasses({CurrentViewProducer.class, AfterViewListener.class, PortalCDIViewListener.class})
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
        var underTest = new PortalViewListenerAdapter();

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
