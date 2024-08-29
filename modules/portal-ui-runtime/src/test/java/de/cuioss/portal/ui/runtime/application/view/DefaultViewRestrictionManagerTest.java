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
package de.cuioss.portal.ui.runtime.application.view;

import de.cuioss.jsf.api.common.view.ViewDescriptor;
import de.cuioss.jsf.api.common.view.ViewDescriptorImpl;
import de.cuioss.portal.core.test.mocks.authentication.PortalTestUserProducer;
import de.cuioss.portal.core.test.mocks.configuration.PortalTestConfiguration;
import de.cuioss.portal.ui.api.pages.HomePage;
import de.cuioss.portal.ui.api.view.PortalViewRestrictionManager;
import de.cuioss.portal.ui.test.junit5.EnablePortalUiEnvironment;
import de.cuioss.test.valueobjects.junit5.contracts.ShouldHandleObjectContracts;
import jakarta.inject.Inject;
import lombok.Getter;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static de.cuioss.portal.configuration.PortalConfigurationKeys.VIEW_ROLE_RESTRICTION_PREFIX;
import static de.cuioss.tools.collect.CollectionLiterals.immutableList;
import static org.junit.jupiter.api.Assertions.*;

@EnablePortalUiEnvironment
class DefaultViewRestrictionManagerTest implements ShouldHandleObjectContracts<DefaultViewRestrictionManager> {

    private static final String ROLE_CONTENT = "content_manager";
    private static final String ROLE_ADMIN = "admin";

    private static final ViewDescriptor ANY_GUEST = new ViewDescriptorImpl("/guest/any.jsf", "/guest/any.xhtml",
        Collections.emptyList());

    private static final ViewDescriptor ANY_CONTENT = new ViewDescriptorImpl("/content/any.jsf", "/content/any.xhtml",
        Collections.emptyList());

    private static final ViewDescriptor ADMIN_CONTENT = new ViewDescriptorImpl("/content/admin.jsf",
        "/content/admin.xhtml", Collections.emptyList());

    @Inject
    @Getter
    @PortalViewRestrictionManager
    private DefaultViewRestrictionManager underTest;

    @Inject
    private PortalTestUserProducer portalUserProducerMock;

    @Inject
    private PortalTestConfiguration configuration;

    @Test
    void shouldHandleEmptyConfiguration() {
        assertTrue(underTest.getRequiredRolesForView(ANY_GUEST).isEmpty());
        assertTrue(underTest.getRequiredRolesForView(ANY_CONTENT).isEmpty());

        assertTrue(underTest.isUserAuthorized(ANY_GUEST));
        assertTrue(underTest.isUserAuthorized(ANY_CONTENT));
    }

    @Test
    void shouldRestrictSingleContent() {
        configuration.update(VIEW_ROLE_RESTRICTION_PREFIX + ROLE_CONTENT, "/content/");

        portalUserProducerMock.roles(immutableList(ROLE_CONTENT));

        assertTrue(underTest.isUserAuthorized(ANY_GUEST));
        assertTrue(underTest.isUserAuthorized(ANY_CONTENT));

        assertTrue(underTest.getRequiredRolesForView(ANY_GUEST).isEmpty());

        assertEquals(1, underTest.getRequiredRolesForView(ANY_CONTENT).size());
        assertTrue(underTest.getRequiredRolesForView(ANY_CONTENT).contains(ROLE_CONTENT));
    }

    @Test
    void shouldRestrictMultipleRoles() {
        configuration.update(VIEW_ROLE_RESTRICTION_PREFIX + ROLE_CONTENT, "/content/",
            VIEW_ROLE_RESTRICTION_PREFIX + ROLE_ADMIN, "/content/admin");

        portalUserProducerMock.roles(immutableList(ROLE_CONTENT, ROLE_ADMIN));

        assertTrue(underTest.getRequiredRolesForView(ANY_GUEST).isEmpty());
        assertEquals(1, underTest.getRequiredRolesForView(ANY_CONTENT).size());
        assertTrue(underTest.getRequiredRolesForView(ANY_CONTENT).contains(ROLE_CONTENT));
        assertTrue(underTest.isUserAuthorized(ANY_GUEST));

        assertEquals(2, underTest.getRequiredRolesForView(ADMIN_CONTENT).size());
        assertTrue(underTest.getRequiredRolesForView(ADMIN_CONTENT).contains(ROLE_CONTENT));
        assertTrue(underTest.getRequiredRolesForView(ADMIN_CONTENT).contains(ROLE_ADMIN));

        assertTrue(underTest.isUserAuthorized(ADMIN_CONTENT));
    }

    @Test
    void shouldNotMatchRoleConfiguredDirectoryToAnyPage() {
        configuration.update(VIEW_ROLE_RESTRICTION_PREFIX + ROLE_ADMIN, "/content/admin/",
            VIEW_ROLE_RESTRICTION_PREFIX + ROLE_CONTENT, "/content/");

        ViewDescriptor pageMatchedToDirectoryPath = new ViewDescriptorImpl("faces/content/administrationPage.jsf",
            "/content/administrationPage.xhtml", Collections.emptyList());

        portalUserProducerMock.roles(immutableList(ROLE_CONTENT));
        assertEquals(1, underTest.getRequiredRolesForView(pageMatchedToDirectoryPath).size(),
            "The role admin defines the restriction on directory which have to end with '/', it shouldn't matched to any page's name");
        assertTrue(underTest.getRequiredRolesForView(pageMatchedToDirectoryPath).contains(ROLE_CONTENT));

        assertTrue(underTest.isUserAuthorized(pageMatchedToDirectoryPath));
    }

    @Test
    void shouldHandleOutcomes() {
        configuration.update(VIEW_ROLE_RESTRICTION_PREFIX + ROLE_CONTENT, "/");

        assertFalse(underTest.isUserAuthorizedForViewOutcome(HomePage.OUTCOME));
    }

    @Test
    void shouldFailToLookupNotExistingOutcome() {
        assertThrows(IllegalStateException.class, () -> underTest.isUserAuthorizedForViewOutcome("not-there"));
    }
}
