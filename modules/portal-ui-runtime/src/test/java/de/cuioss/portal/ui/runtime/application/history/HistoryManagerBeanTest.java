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
package de.cuioss.portal.ui.runtime.application.history;

import static de.cuioss.portal.configuration.PortalConfigurationKeys.HISTORY_VIEW_EXCLUDE_PARAMETER;
import static de.cuioss.portal.ui.test.configuration.PortalNavigationConfiguration.VIEW_HOME_LOGICAL_VIEW_ID;
import static de.cuioss.tools.collect.CollectionLiterals.mutableList;
import static org.junit.jupiter.api.Assertions.assertEquals;

import jakarta.faces.lifecycle.ClientWindowScoped;
import jakarta.inject.Inject;

import org.jboss.weld.junit5.auto.ActivateScopes;
import org.jboss.weld.junit5.auto.AddBeanClasses;
import org.junit.jupiter.api.Test;

import de.cuioss.jsf.api.common.view.ViewDescriptorImpl;
import de.cuioss.portal.configuration.PortalConfigurationSource;
import de.cuioss.portal.core.test.mocks.configuration.PortalTestConfiguration;
import de.cuioss.portal.ui.api.pages.HomePage;
import de.cuioss.portal.ui.runtime.application.view.matcher.ViewMatcherProducer;
import de.cuioss.portal.ui.test.junit5.EnablePortalUiEnvironment;
import de.cuioss.test.jsf.util.JsfEnvironmentConsumer;
import de.cuioss.test.jsf.util.JsfEnvironmentHolder;
import de.cuioss.test.valueobjects.junit5.contracts.ShouldHandleObjectContracts;
import lombok.Getter;
import lombok.Setter;

@EnablePortalUiEnvironment
@AddBeanClasses({ DefaultHistoryConfiguration.class, ViewMatcherProducer.class })
@ActivateScopes(ClientWindowScoped.class)
class HistoryManagerBeanTest implements ShouldHandleObjectContracts<HistoryManagerBean>, JsfEnvironmentConsumer {

    @Inject
    @Getter
    private HistoryManagerBean underTest;

    @Inject
    @PortalConfigurationSource
    private PortalTestConfiguration configuration;

    @Setter
    @Getter
    private JsfEnvironmentHolder environmentHolder;

    private static final String FIRST_NAVIGATION = "/current/view3.jsf";

    public static final String CURRENT_VIEW_XHTML = "current/view.jsf";

    public static final String FALLBACK_VIEW = "/portal/home.jsf";

    public static final String SECOND_VIEW_XHTML = "current/view2.jsf";

    public static final String VETO_VIEW_XHTML = "current/veto.jsf";

    static final ViewDescriptorImpl CURRENT_VIEW = ViewDescriptorImpl.builder().withViewId(CURRENT_VIEW_XHTML)
            .withLogicalViewId(CURRENT_VIEW_XHTML).build();

    static final ViewDescriptorImpl SECOND_VIEW = ViewDescriptorImpl.builder().withViewId(SECOND_VIEW_XHTML)
            .withLogicalViewId(SECOND_VIEW_XHTML).build();

    static final ViewDescriptorImpl VETO_VIEW = ViewDescriptorImpl.builder().withViewId(VETO_VIEW_XHTML)
            .withLogicalViewId(VETO_VIEW_XHTML).build();

    /**
     * Checks the default configuration
     */
    @Test
    void shouldBeConfiguredProperly() {
        assertEquals(VIEW_HOME_LOGICAL_VIEW_ID, underTest.getCurrentView().getViewId());
    }

    @Test
    void shouldAddViewToHistory() {
        assertEntryCount(1, underTest);
        underTest.addCurrentUriToHistory(CURRENT_VIEW);
        assertEntryCount(2, underTest);
        // adding the same view again should not change anything
        underTest.addCurrentUriToHistory(CURRENT_VIEW);
        assertEntryCount(2, underTest);

        getRequestConfigDecorator().setViewId(SECOND_VIEW_XHTML);
        underTest.addCurrentUriToHistory(SECOND_VIEW);
        assertEntryCount(3, underTest);
    }

    @Test
    void shouldLimitStackSizeCorrectly() {
        for (var i = 0; i < 15; i++) {
            final var viewId = i + "/" + CURRENT_VIEW_XHTML;
            final var currentView = ViewDescriptorImpl.builder().withViewId(viewId).withLogicalViewId(viewId).build();
            underTest.addCurrentUriToHistory(currentView);
        }
        // Limit is 10 + fallback
        assertEntryCount(11, underTest);
    }

    @Test
    void shouldPopViewFromHistory() {
        assertEntryCount(1, underTest);
        underTest.addCurrentUriToHistory(CURRENT_VIEW);
        assertEntryCount(2, underTest);
        underTest.addCurrentUriToHistory(SECOND_VIEW);
        assertEntryCount(3, underTest);
        final var current = underTest.popPrevious();
        assertEntryCount(2, underTest);
        assertEquals(CURRENT_VIEW_XHTML, current.getViewId());
        var fallback = underTest.popPrevious();
        assertEntryCount(1, underTest);
        assertEquals(HomePage.OUTCOME, fallback.getOutcome());
        // The last one should always stay
        fallback = underTest.popPrevious();
        assertEntryCount(1, underTest);
        assertEquals(HomePage.OUTCOME, fallback.getOutcome());
    }

    @Test
    void shouldPeekViewFromHistory() {
        assertEntryCount(1, underTest);
        underTest.addCurrentUriToHistory(CURRENT_VIEW);
        assertEntryCount(2, underTest);
        underTest.addCurrentUriToHistory(SECOND_VIEW);
        assertEntryCount(3, underTest);
        var current = underTest.peekPrevious();
        assertEntryCount(3, underTest);
        assertEquals(CURRENT_VIEW_XHTML, current.getViewId());
        // peek should not change the stack
        current = underTest.peekPrevious();
        assertEntryCount(3, underTest);
        assertEquals(CURRENT_VIEW_XHTML, current.getViewId());
    }

    @Test
    void shouldVetoCorrectly() {
        configuration.fireEvent(HISTORY_VIEW_EXCLUDE_PARAMETER, VETO_VIEW_XHTML);
        assertEntryCount(1, underTest);
        underTest.addCurrentUriToHistory(VETO_VIEW);
        assertEntryCount(1, underTest);
        underTest.addCurrentUriToHistory(SECOND_VIEW);
        assertEntryCount(2, underTest);
    }

    @Test
    void shouldReturnFallbackIfOnDeeplinkEnteringScenario() {
        final var fallback = underTest.peekPrevious();
        assertEquals(HomePage.OUTCOME, fallback.getOutcome());
    }

    @Test
    void shouldOnlyAddViewToHistoryIfWasNotLast() {
        final var firstNavigation = ViewDescriptorImpl.builder().withViewId(FIRST_NAVIGATION)
                .withLogicalViewId(FIRST_NAVIGATION).build();
        underTest.addCurrentUriToHistory(firstNavigation);
        underTest.addCurrentUriToHistory(SECOND_VIEW);
        underTest.addCurrentUriToHistory(CURRENT_VIEW);
        // stay on page 3 times
        underTest.addCurrentUriToHistory(CURRENT_VIEW);
        underTest.addCurrentUriToHistory(CURRENT_VIEW);
        underTest.addCurrentUriToHistory(CURRENT_VIEW);
        // pop from history
        assertEquals(SECOND_VIEW_XHTML, underTest.popPrevious().getViewId());
        assertEquals(FIRST_NAVIGATION, underTest.popPrevious().getViewId());
        // is default
        assertEquals(FALLBACK_VIEW, underTest.popPrevious().getViewId());
    }

    /**
     * Verify count of iterable
     *
     * @param expected value
     * @param iterable {@link Iterable}
     */
    public static void assertEntryCount(final int expected, final Iterable<?> iterable) {
        assertEquals(expected, mutableList(iterable).size());
    }

}
