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

import de.cuioss.jsf.api.application.view.matcher.EmptyViewMatcher;
import de.cuioss.portal.core.test.junit5.EnablePortalConfiguration;
import de.cuioss.portal.core.test.mocks.configuration.PortalTestConfiguration;
import de.cuioss.portal.ui.runtime.application.view.matcher.ViewMatcherProducer;
import de.cuioss.test.valueobjects.junit5.contracts.ShouldHandleObjectContracts;
import jakarta.inject.Inject;
import lombok.Getter;
import org.jboss.weld.junit5.auto.AddBeanClasses;
import org.jboss.weld.junit5.auto.EnableAutoWeld;
import org.junit.jupiter.api.Test;

import static de.cuioss.portal.ui.test.configuration.PortalNavigationConfiguration.DESCRIPTOR_HOME;
import static de.cuioss.portal.ui.test.configuration.PortalNavigationConfiguration.DESCRIPTOR_LOGIN;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@EnableAutoWeld
@EnablePortalConfiguration
@AddBeanClasses({ViewMatcherProducer.class})
class ViewConfigurationTest implements ShouldHandleObjectContracts<ViewConfiguration> {

    @Inject
    @Getter
    private ViewConfiguration underTest;

    @Inject
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

}
