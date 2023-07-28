package de.cuioss.portal.ui.runtime.application.view.matcher;

import static org.junit.jupiter.api.Assertions.assertEquals;

import javax.inject.Inject;
import javax.inject.Provider;

import org.jboss.weld.junit5.auto.EnableAutoWeld;
import org.junit.jupiter.api.Test;

import de.cuioss.jsf.api.application.view.matcher.EmptyViewMatcher;
import de.cuioss.jsf.api.application.view.matcher.ViewMatcher;
import de.cuioss.jsf.api.application.view.matcher.ViewMatcherImpl;
import de.cuioss.portal.configuration.PortalConfigurationSource;
import de.cuioss.portal.core.test.junit5.EnablePortalConfiguration;
import de.cuioss.portal.core.test.mocks.configuration.PortalTestConfiguration;
import de.cuioss.portal.ui.api.configuration.types.ConfigAsViewMatcher;
import de.cuioss.test.valueobjects.junit5.contracts.ShouldBeNotNull;
import lombok.Getter;

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
    @PortalConfigurationSource
    private PortalTestConfiguration configuration;

    @Test
    void shouldProduceViewMatcher() {
        // Property initially not there
        assertEquals(EmptyViewMatcher.class, injectedViewMatcher.get().getClass());

        configuration.fireEvent(CONFIGURATION_KEY, LIST_SINGLE_VALUE);

        assertEquals(ViewMatcherImpl.class, injectedViewMatcher.get().getClass());

        configuration.fireEvent(CONFIGURATION_KEY, LIST_TWO_VALUES);

        assertEquals(ViewMatcherImpl.class, injectedViewMatcher.get().getClass());

        configuration.fireEvent(CONFIGURATION_KEY, "");
        assertEquals(EmptyViewMatcher.class, injectedViewMatcher.get().getClass());
    }
}
