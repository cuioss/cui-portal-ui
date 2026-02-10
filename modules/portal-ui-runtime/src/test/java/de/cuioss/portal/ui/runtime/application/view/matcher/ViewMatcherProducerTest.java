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
package de.cuioss.portal.ui.runtime.application.view.matcher;

import de.cuioss.jsf.api.application.view.matcher.EmptyViewMatcher;
import de.cuioss.jsf.api.application.view.matcher.ViewMatcher;
import de.cuioss.jsf.api.application.view.matcher.ViewMatcherImpl;
import de.cuioss.portal.core.test.junit5.EnablePortalConfiguration;
import de.cuioss.portal.core.test.mocks.configuration.PortalTestConfiguration;
import de.cuioss.portal.ui.api.configuration.types.ConfigAsViewMatcher;
import de.cuioss.test.valueobjects.junit5.contracts.ShouldBeNotNull;
import jakarta.inject.Inject;
import jakarta.inject.Provider;
import lombok.Getter;
import org.jboss.weld.junit5.auto.EnableAutoWeld;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@EnableAutoWeld
@EnablePortalConfiguration
class ViewMatcherProducerTest implements ShouldBeNotNull<ViewMatcherProducer> {

    private static final String CONFIGURATION_KEY = "configurationKey";
    private static final String LIST_SINGLE_VALUE = "list";
    private static final String LIST_TWO_VALUES = "list, more";

    @Inject
    @Getter
    private ViewMatcherProducer underTest;

    @Inject
    @ConfigAsViewMatcher(name = CONFIGURATION_KEY)
    private Provider<ViewMatcher> injectedViewMatcher;

    @Inject
    private PortalTestConfiguration configuration;

    @Test
    void shouldProduceViewMatcher() {
        // Property initially not there
        assertEquals(EmptyViewMatcher.class, injectedViewMatcher.get().getClass());

        configuration.update(CONFIGURATION_KEY, LIST_SINGLE_VALUE);

        assertEquals(ViewMatcherImpl.class, injectedViewMatcher.get().getClass());

        configuration.update(CONFIGURATION_KEY, LIST_TWO_VALUES);

        assertEquals(ViewMatcherImpl.class, injectedViewMatcher.get().getClass());

        configuration.update(CONFIGURATION_KEY, "");
        assertEquals(EmptyViewMatcher.class, injectedViewMatcher.get().getClass());
    }
}
