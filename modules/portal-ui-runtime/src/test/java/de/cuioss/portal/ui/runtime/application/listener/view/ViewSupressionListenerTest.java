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

import static de.cuioss.portal.ui.test.configuration.PortalNavigationConfiguration.DESCRIPTOR_HOME;
import static de.cuioss.portal.ui.test.configuration.PortalNavigationConfiguration.DESCRIPTOR_LOGIN;
import static de.cuioss.portal.ui.test.configuration.PortalNavigationConfiguration.DESCRIPTOR_NOT_THERE;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;

import org.jboss.weld.junit5.auto.AddBeanClasses;
import org.junit.jupiter.api.Test;

import de.cuioss.portal.configuration.PortalConfigurationKeys;
import de.cuioss.portal.configuration.PortalConfigurationSource;
import de.cuioss.portal.core.test.mocks.configuration.PortalTestConfiguration;
import de.cuioss.portal.ui.api.exception.ExceptionAsEvent;
import de.cuioss.portal.ui.api.listener.view.PhaseExecution;
import de.cuioss.portal.ui.api.listener.view.PortalRestoreViewListener;
import de.cuioss.portal.ui.runtime.application.view.ViewSuppressedException;
import de.cuioss.portal.ui.runtime.application.view.matcher.ViewMatcherProducer;
import de.cuioss.portal.ui.test.junit5.EnablePortalUiEnvironment;
import de.cuioss.test.valueobjects.junit5.contracts.ShouldHandleObjectContracts;
import lombok.Getter;

@EnablePortalUiEnvironment
@AddBeanClasses({ ViewMatcherProducer.class })
class ViewSupressionListenerTest implements ShouldHandleObjectContracts<ViewSupressionListener> {

    @Inject
    @PortalRestoreViewListener(PhaseExecution.AFTER_PHASE)
    @Getter
    private ViewSupressionListener underTest;

    @Inject
    @PortalConfigurationSource
    private PortalTestConfiguration configuration;

    private ViewSuppressedException viewSuppressedException = null;

    @Test
    void shouldAlwaysReturnFalseOnDefault() {
        underTest.handleView(DESCRIPTOR_HOME);
        underTest.handleView(DESCRIPTOR_NOT_THERE);
    }

    @Test
    void shouldThrowViewSuppressedException() {
        assertNull(viewSuppressedException);
        configuration.fireEvent(PortalConfigurationKeys.SUPPRESSED_VIEWS, "/");
        underTest.handleView(DESCRIPTOR_LOGIN);
        assertNotNull(viewSuppressedException);
    }

    void actOnExceptionToCatchEvent(@Observes final ExceptionAsEvent catchEvent) {
        viewSuppressedException = (ViewSuppressedException) catchEvent.getException();
    }

}
