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

import de.cuioss.portal.ui.api.history.HistoryManager;
import de.cuioss.portal.ui.api.listener.view.PhaseExecution;
import de.cuioss.portal.ui.api.listener.view.PortalRestoreViewListener;
import de.cuioss.portal.ui.runtime.application.history.DefaultHistoryConfiguration;
import de.cuioss.portal.ui.runtime.application.history.HistoryManagerBean;
import de.cuioss.portal.ui.runtime.application.view.matcher.ViewMatcherProducer;
import de.cuioss.portal.ui.test.junit5.EnablePortalUiEnvironment;
import de.cuioss.test.jsf.util.JsfEnvironmentConsumer;
import de.cuioss.test.jsf.util.JsfEnvironmentHolder;
import de.cuioss.test.valueobjects.junit5.contracts.ShouldHandleObjectContracts;
import jakarta.faces.lifecycle.ClientWindowScoped;
import jakarta.inject.Inject;
import lombok.Getter;
import lombok.Setter;
import org.jboss.weld.junit5.auto.ActivateScopes;
import org.jboss.weld.junit5.auto.AddBeanClasses;
import org.junit.jupiter.api.Test;

import static de.cuioss.portal.ui.test.configuration.PortalNavigationConfiguration.*;
import static org.junit.jupiter.api.Assertions.*;

@EnablePortalUiEnvironment
@AddBeanClasses({HistoryManagerBean.class, DefaultHistoryConfiguration.class, ViewMatcherProducer.class})
@ActivateScopes(ClientWindowScoped.class)
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
