package de.cuioss.portal.ui.runtime.application.view.matcher;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

import de.cuioss.jsf.api.application.view.matcher.EmptyViewMatcher;
import de.cuioss.jsf.api.application.view.matcher.ViewMatcher;
import de.cuioss.jsf.api.application.view.matcher.ViewMatcherImpl;
import de.cuioss.portal.configuration.util.ConfigurationHelper;
import de.cuioss.portal.ui.api.configuration.types.ConfigAsViewMatcher;
import de.cuioss.tools.string.MoreStrings;
import de.cuioss.tools.string.Splitter;

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
