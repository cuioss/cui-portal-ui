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
package de.cuioss.portal.ui.runtime.application.configuration;

import static de.cuioss.portal.configuration.PortalConfigurationKeys.NON_SECURED_VIEWS;
import static de.cuioss.portal.configuration.PortalConfigurationKeys.SUPPRESSED_VIEWS;
import static de.cuioss.portal.configuration.PortalConfigurationKeys.TRANSIENT_VIEWS;
import static de.cuioss.portal.ui.test.configuration.PortalNavigationConfiguration.DESCRIPTOR_HOME;
import static de.cuioss.portal.ui.test.configuration.PortalNavigationConfiguration.DESCRIPTOR_LOGIN;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.inject.Inject;

import org.jboss.weld.junit5.auto.AddBeanClasses;
import org.jboss.weld.junit5.auto.EnableAutoWeld;
import org.junit.jupiter.api.Test;

import de.cuioss.jsf.api.application.view.matcher.EmptyViewMatcher;
import de.cuioss.portal.configuration.PortalConfigurationSource;
import de.cuioss.portal.core.test.junit5.EnablePortalConfiguration;
import de.cuioss.portal.core.test.mocks.configuration.PortalTestConfiguration;
import de.cuioss.portal.ui.runtime.application.view.matcher.ViewMatcherProducer;
import de.cuioss.test.valueobjects.junit5.contracts.ShouldHandleObjectContracts;
import lombok.Getter;

@EnableAutoWeld
@EnablePortalConfiguration
@AddBeanClasses({ ViewMatcherProducer.class })
class ViewConfigurationTest implements ShouldHandleObjectContracts<ViewConfiguration> {

    @Inject
    @Getter
    private ViewConfiguration underTest;

    @Inject
    @PortalConfigurationSource
    private PortalTestConfiguration configuration;

    @Test
    void shouldProvideDefaultConfiguration() {
        final var nonSecuredViewMatcher = underTest.getNonSecuredViewMatcher();
        assertNotNull(nonSecuredViewMatcher);
        assertNotEquals(EmptyViewMatcher.class, nonSecuredViewMatcher.getClass());
        assertTrue(nonSecuredViewMatcher.match(DESCRIPTOR_LOGIN));
        assertFalse(nonSecuredViewMatcher.match(DESCRIPTOR_HOME));

        final var transientViewMatcher = underTest.getTransientViewMatcher();
        assertNotNull(transientViewMatcher);
        assertNotEquals(EmptyViewMatcher.class, transientViewMatcher.getClass());
        assertTrue(transientViewMatcher.match(DESCRIPTOR_LOGIN));
        assertFalse(transientViewMatcher.match(DESCRIPTOR_HOME));

        final var suppressedViewMatcher = underTest.getSuppressedViewMatcher();
        assertNotNull(suppressedViewMatcher);
        assertEquals(EmptyViewMatcher.class, suppressedViewMatcher.getClass());
    }

    @Test
    void shouldReconfigureOnEvent() {
        // Do not reconfigure on no change
        configuration.fireEvent();
        shouldProvideDefaultConfiguration();

        // reconfigure to empty elements
        configuration.put(NON_SECURED_VIEWS, "");
        configuration.put(TRANSIENT_VIEWS, "");
        configuration.put(SUPPRESSED_VIEWS, "");
        configuration.fireEvent();
        assertEquals(EmptyViewMatcher.class, underTest.getNonSecuredViewMatcher().getClass());
        assertEquals(EmptyViewMatcher.class, underTest.getTransientViewMatcher().getClass());
        assertEquals(EmptyViewMatcher.class, underTest.getSuppressedViewMatcher().getClass());

        // None Secured matcher only
        configuration.fireEvent(NON_SECURED_VIEWS, "/");
        assertNotEquals(EmptyViewMatcher.class, underTest.getNonSecuredViewMatcher().getClass());
        assertEquals(EmptyViewMatcher.class, underTest.getTransientViewMatcher().getClass());
        assertEquals(EmptyViewMatcher.class, underTest.getSuppressedViewMatcher().getClass());

        // transient matcher
        configuration.fireEvent(TRANSIENT_VIEWS, "/");
        assertNotEquals(EmptyViewMatcher.class, underTest.getNonSecuredViewMatcher().getClass());
        assertNotEquals(EmptyViewMatcher.class, underTest.getTransientViewMatcher().getClass());
        assertEquals(EmptyViewMatcher.class, underTest.getSuppressedViewMatcher().getClass());

        // suppressed matcher
        configuration.fireEvent(SUPPRESSED_VIEWS, "/");
        assertNotEquals(EmptyViewMatcher.class, underTest.getNonSecuredViewMatcher().getClass());
        assertNotEquals(EmptyViewMatcher.class, underTest.getTransientViewMatcher().getClass());
        assertNotEquals(EmptyViewMatcher.class, underTest.getSuppressedViewMatcher().getClass());

    }

}
