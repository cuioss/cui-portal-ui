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
import de.cuioss.portal.configuration.util.ConfigurationHelper;
import de.cuioss.portal.ui.api.configuration.types.ConfigAsViewMatcher;
import de.cuioss.tools.string.MoreStrings;
import de.cuioss.tools.string.Splitter;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.Produces;
import jakarta.enterprise.inject.spi.InjectionPoint;

/**
 * Provides specific producer methods for elements not covered by the standard
 * configuration converter
 *
 * @author Oliver Wolff
 */
@ApplicationScoped
public class ViewMatcherProducer {

    @Produces
    @Dependent
    @ConfigAsViewMatcher(name = "unused")
    ViewMatcher produceViewMatcher(final InjectionPoint injectionPoint) {
        final var metaData = ConfigurationHelper.resolveAnnotation(injectionPoint, ConfigAsViewMatcher.class)
                .orElseThrow(() -> new IllegalStateException("Type must provide annotation ConfigAsViewMatcher"));
        final var configuredValue = ConfigurationHelper.resolveConfigProperty(metaData.name()).orElse(null);
        if (MoreStrings.isEmpty(configuredValue)) {
            return new EmptyViewMatcher(false);
        }
        return new ViewMatcherImpl(
                Splitter.on(metaData.separator()).trimResults().omitEmptyStrings().splitToList(configuredValue));
    }

}
