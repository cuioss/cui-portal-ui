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
package de.cuioss.portal.ui.runtime.application.listener.view;

import static de.cuioss.portal.ui.test.configuration.PortalNavigationConfiguration.DESCRIPTOR_LOGIN;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Provider;

import org.jboss.weld.junit5.auto.AddBeanClasses;
import org.junit.jupiter.api.Test;

import de.cuioss.portal.common.stage.ProjectStage;
import de.cuioss.portal.configuration.PortalConfigurationSource;
import de.cuioss.portal.core.test.mocks.configuration.PortalTestConfiguration;
import de.cuioss.portal.ui.api.configuration.PortalNotConfiguredException;
import de.cuioss.portal.ui.api.exception.ExceptionAsEvent;
import de.cuioss.portal.ui.api.listener.view.PhaseExecution;
import de.cuioss.portal.ui.api.listener.view.PortalRestoreViewListener;
import de.cuioss.portal.ui.test.junit5.EnablePortalUiEnvironment;
import de.cuioss.test.valueobjects.junit5.contracts.ShouldHandleObjectContracts;

@EnablePortalUiEnvironment
@AddBeanClasses({ PortalStageModeListener.class })
class PortalStageModeListenerTest implements ShouldHandleObjectContracts<PortalStageModeListener> {

    @Inject
    @PortalRestoreViewListener(PhaseExecution.AFTER_PHASE)
    private Provider<PortalStageModeListener> underTest;

    @Inject
    @PortalConfigurationSource
    private PortalTestConfiguration configuration;

    private boolean exceptionWasThrown = false;

    @Test
    void shouldThrowPortalNotConfiguredException() {
        configuration.setPortalProjectStage(ProjectStage.CONFIGURATION);
        getUnderTest().handleView(DESCRIPTOR_LOGIN);
        assertTrue(exceptionWasThrown);
    }

    @Test
    void shouldNotThrowPortalNotConfiguredException() {
        getUnderTest().handleView(DESCRIPTOR_LOGIN);
        assertFalse(exceptionWasThrown); // default is production
    }

    void actOnExceptionToCatchEvent(@Observes final ExceptionAsEvent catchEvent) {
        final var exception = catchEvent.getException();
        exceptionWasThrown = exception instanceof PortalNotConfiguredException;
    }

    @Override
    public PortalStageModeListener getUnderTest() {
        return underTest.get();
    }
}
