/*
 * Copyright 2023 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * https://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
