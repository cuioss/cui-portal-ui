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

import de.cuioss.portal.core.test.mocks.authentication.PortalTestUserProducer;
import de.cuioss.portal.ui.api.authentication.UserNotAuthenticatedException;
import de.cuioss.portal.ui.api.exception.ExceptionAsEvent;
import de.cuioss.portal.ui.api.listener.view.PhaseExecution;
import de.cuioss.portal.ui.api.listener.view.PortalRestoreViewListener;
import de.cuioss.portal.ui.runtime.application.configuration.ViewConfiguration;
import de.cuioss.portal.ui.runtime.application.view.matcher.ViewMatcherProducer;
import de.cuioss.portal.ui.test.junit5.EnablePortalUiEnvironment;
import de.cuioss.test.valueobjects.junit5.contracts.ShouldHandleObjectContracts;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import lombok.Getter;
import org.jboss.weld.junit5.auto.AddBeanClasses;
import org.junit.jupiter.api.Test;

import static de.cuioss.portal.ui.test.configuration.PortalNavigationConfiguration.DESCRIPTOR_HOME;
import static de.cuioss.portal.ui.test.configuration.PortalNavigationConfiguration.DESCRIPTOR_LOGIN;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@EnablePortalUiEnvironment
@AddBeanClasses({ PortalTestUserProducer.class, ViewConfiguration.class, ViewMatcherProducer.class })
class ViewAuthenticationListenerTest implements ShouldHandleObjectContracts<ViewAuthenticationListener> {

    @Inject
    private PortalTestUserProducer portalUserProducerMock;

    @Inject
    @PortalRestoreViewListener(PhaseExecution.AFTER_PHASE)
    @Getter
    private ViewAuthenticationListener underTest;

    private UserNotAuthenticatedException authenticatedException;

    @Test
    void shouldPassOnGuestView() {
        underTest.handleView(DESCRIPTOR_LOGIN);
        assertNull(authenticatedException);
    }

    @Test
    void shouldPassOnAuthenticatedUser() {
        assertNull(authenticatedException);
        underTest.handleView(DESCRIPTOR_HOME);
        assertNull(authenticatedException);
    }

    @Test
    void shouldFailOnNotAuthenticatedUser() {
        portalUserProducerMock.authenticated(false);
        assertNull(authenticatedException);
        underTest.handleView(DESCRIPTOR_HOME);
        assertNotNull(authenticatedException);
    }

    void actOnExceptionToCatchEvent(@Observes final ExceptionAsEvent catchEvent) {
        final var exception = catchEvent.getException();
        authenticatedException = exception instanceof UserNotAuthenticatedException unae ? unae : null;
    }

}
