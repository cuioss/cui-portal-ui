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
package de.cuioss.portal.ui.runtime.common;

import de.cuioss.portal.core.test.mocks.configuration.PortalTestConfiguration;
import de.cuioss.portal.ui.api.context.CurrentViewProducer;
import de.cuioss.portal.ui.runtime.application.view.ViewTransientManagerBean;
import de.cuioss.portal.ui.runtime.application.view.matcher.ViewMatcherProducer;
import de.cuioss.portal.ui.test.junit5.EnablePortalUiEnvironment;
import de.cuioss.test.valueobjects.junit5.contracts.ShouldHandleObjectContracts;
import jakarta.inject.Inject;
import lombok.Getter;
import org.jboss.weld.junit5.auto.AddBeanClasses;
import org.junit.jupiter.api.Test;

import static de.cuioss.portal.configuration.PortalConfigurationKeys.PORTAL_SESSION_MAX_INACTIVE_INTERVAL;
import static org.junit.jupiter.api.Assertions.*;

@EnablePortalUiEnvironment
@AddBeanClasses({ViewTransientManagerBean.class, ViewMatcherProducer.class, CurrentViewProducer.class})
class ClientWindowManagerTest implements ShouldHandleObjectContracts<ClientWindowManager> {

    @Inject
    @Getter
    private ClientWindowManager underTest;

    @Inject
    private PortalTestConfiguration configuration;

    /**
     * Checks whether the correct user is produced.
     */
    @Test
    void shouldHandleMaxInactiveInterval() {
        configuration.update(PORTAL_SESSION_MAX_INACTIVE_INTERVAL, "10");
        assertEquals(10, underTest.getMaxInactiveInterval());
        assertTrue(underTest.isRenderTimeoutForm());
    }

    /**
     * Checks whether the correct user is produced.
     */
    @Test
    void shouldHandleNullMaxInactiveInterval() {
        configuration.update(PORTAL_SESSION_MAX_INACTIVE_INTERVAL, "0");
        assertEquals(0, underTest.getMaxInactiveInterval());
        assertFalse(underTest.isRenderTimeoutForm());
    }

}
