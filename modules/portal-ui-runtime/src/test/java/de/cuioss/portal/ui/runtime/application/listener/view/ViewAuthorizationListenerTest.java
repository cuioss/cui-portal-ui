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

import static de.cuioss.portal.configuration.PortalConfigurationKeys.VIEW_ROLE_RESTRICTION_PREFIX;
import static de.cuioss.portal.ui.test.configuration.PortalNavigationConfiguration.DESCRIPTOR_HOME;
import static de.cuioss.portal.ui.test.configuration.PortalNavigationConfiguration.DESCRIPTOR_LOGIN;
import static de.cuioss.portal.ui.test.configuration.PortalNavigationConfiguration.VIEW_HOME_LOGICAL_VIEW_ID;
import static de.cuioss.tools.collect.CollectionLiterals.mutableList;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;

import org.jboss.weld.junit5.auto.AddBeanClasses;
import org.junit.jupiter.api.Test;

import de.cuioss.portal.configuration.PortalConfigurationSource;
import de.cuioss.portal.core.test.mocks.authentication.PortalTestUserProducer;
import de.cuioss.portal.core.test.mocks.configuration.PortalTestConfiguration;
import de.cuioss.portal.ui.api.authentication.UserNotAuthorizedException;
import de.cuioss.portal.ui.api.exception.ExceptionAsEvent;
import de.cuioss.portal.ui.api.listener.view.PhaseExecution;
import de.cuioss.portal.ui.api.listener.view.PortalRestoreViewListener;
import de.cuioss.portal.ui.runtime.application.configuration.ViewConfiguration;
import de.cuioss.portal.ui.runtime.application.view.DefaultViewRestrictionManager;
import de.cuioss.portal.ui.runtime.application.view.matcher.ViewMatcherProducer;
import de.cuioss.portal.ui.test.junit5.EnablePortalUiEnvironment;
import de.cuioss.test.valueobjects.junit5.contracts.ShouldHandleObjectContracts;
import lombok.Getter;

@EnablePortalUiEnvironment
@AddBeanClasses({ PortalTestUserProducer.class, ViewConfiguration.class, ViewMatcherProducer.class,
        DefaultViewRestrictionManager.class })
class ViewAuthorizationListenerTest implements ShouldHandleObjectContracts<ViewAuthorizationListener> {

    @Inject
    private PortalTestUserProducer portalUserProducerMock;

    @Inject
    @PortalRestoreViewListener(PhaseExecution.AFTER_PHASE)
    @Getter
    private ViewAuthorizationListener underTest;

    @Inject
    @PortalConfigurationSource
    private PortalTestConfiguration configuration;

    private UserNotAuthorizedException authorizedException;

    @Test
    void shouldPassOnGuestViews() {
        assertNull(authorizedException);

        portalUserProducerMock.authenticated(false);
        underTest.handleView(DESCRIPTOR_LOGIN);
        assertNull(authorizedException);
    }

    @Test
    void shouldPassOnNotRestrictedViews() {
        assertNull(authorizedException);

        portalUserProducerMock.authenticated(true);
        underTest.handleView(DESCRIPTOR_HOME);
        assertNull(authorizedException);
    }

    @Test
    void shouldPassOnAuthorizedUser() {
        configuration.put(VIEW_ROLE_RESTRICTION_PREFIX + "testRole", VIEW_HOME_LOGICAL_VIEW_ID);
        configuration.put(VIEW_ROLE_RESTRICTION_PREFIX + "testRole2", VIEW_HOME_LOGICAL_VIEW_ID);
        configuration.fireEvent();

        assertNull(authorizedException);

        portalUserProducerMock.roles(mutableList("testRole", "testRole2"));
        underTest.handleView(DESCRIPTOR_HOME);
        assertNull(authorizedException);
    }

    @Test
    void shouldFailOnNotAuthorizedUserBecauseMissingRole() {
        configuration.put(VIEW_ROLE_RESTRICTION_PREFIX + "anotherRole", VIEW_HOME_LOGICAL_VIEW_ID);
        configuration.fireEvent();

        assertNull(authorizedException);

        portalUserProducerMock.roles(mutableList("testRole", "testRole2"));
        underTest.handleView(DESCRIPTOR_HOME);

        assertNotNull(authorizedException);
    }

    void actOnExceptionToCatchEvent(@Observes final ExceptionAsEvent catchEvent) {
        final var exception = catchEvent.getException();
        authorizedException = exception instanceof UserNotAuthorizedException unae ? unae : null;
    }

}
