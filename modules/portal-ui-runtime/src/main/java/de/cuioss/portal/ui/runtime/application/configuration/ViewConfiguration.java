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
import de.cuioss.jsf.api.application.view.matcher.ViewMatcher;
import de.cuioss.portal.ui.api.configuration.types.ConfigAsViewMatcher;
import de.cuioss.tools.logging.CuiLogger;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Provider;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;

import static de.cuioss.portal.configuration.PortalConfigurationKeys.*;

/**
 * Reads the View specific configuration from the web.xml and provides
 * corresponding {@link ViewMatcher}
 *
 * @author Oliver Wolff
 */
@ApplicationScoped
@EqualsAndHashCode(of = {"nonSecuredViewMatcher", "transientViewMatcher", "suppressedViewMatcher"})
@ToString(of = {"nonSecuredViewMatcher", "transientViewMatcher", "suppressedViewMatcher"})
public class ViewConfiguration implements Serializable {

    private static final long serialVersionUID = -2477375866558117201L;

    private static final CuiLogger log = new CuiLogger(ViewConfiguration.class);

    /**
     * {@link ViewMatcher} checking for non-secured views, defined with
     * {@link #NON_SECURED_VIEWS}
     */
    @Getter
    private ViewMatcher nonSecuredViewMatcher;

    @Inject
    @ConfigAsViewMatcher(name = NON_SECURED_VIEWS)
    private Provider<ViewMatcher> nonSecuredViewMatcherProvider;

    /**
     * {@link ViewMatcher} checking for transient views, defined with
     * {@link #TRANSIENT_VIEWS}
     */
    @Getter
    private ViewMatcher transientViewMatcher;

    @Inject
    @ConfigAsViewMatcher(name = TRANSIENT_VIEWS)
    private Provider<ViewMatcher> transientViewMatcherProvider;

    /**
     * {@link ViewMatcher} checking for non-secured views, defined with
     * {@link #SUPPRESSED_VIEWS}
     */
    @Getter
    private ViewMatcher suppressedViewMatcher;

    @Inject
    @ConfigAsViewMatcher(name = SUPPRESSED_VIEWS)
    private Provider<ViewMatcher> suppressedViewMatcherProvider;

    /**
     * Initializes the bean, see class documentation for details
     */
    @PostConstruct
    public void initBean() {
        doConfigure();
    }

    private void doConfigure() {
        nonSecuredViewMatcher = nonSecuredViewMatcherProvider.get();
        if (nonSecuredViewMatcher instanceof EmptyViewMatcher) {
            log.warn("The configuration of " + NON_SECURED_VIEWS
                + " results in all views of the application being only accessible for authorized user. If this is intentional you can ignore this warning");
        }
        transientViewMatcher = transientViewMatcherProvider.get();
        suppressedViewMatcher = suppressedViewMatcherProvider.get();
    }

}
