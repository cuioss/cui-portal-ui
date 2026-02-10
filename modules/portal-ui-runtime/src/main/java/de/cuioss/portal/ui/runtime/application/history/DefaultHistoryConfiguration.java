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
package de.cuioss.portal.ui.runtime.application.history;

import de.cuioss.jsf.api.application.view.matcher.ViewMatcher;
import de.cuioss.portal.configuration.PortalConfigurationKeys;
import de.cuioss.portal.ui.api.configuration.types.ConfigAsViewMatcher;
import de.cuioss.portal.ui.api.pages.HomePage;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

import static de.cuioss.portal.configuration.PortalConfigurationKeys.HISTORY_EXCLUDE_PARAMETER;
import static de.cuioss.portal.configuration.PortalConfigurationKeys.HISTORY_VIEW_EXCLUDE_PARAMETER;

/**
 * Configuration for {@link de.cuioss.portal.ui.api.history.HistoryManager},
 * defaulting to:
 * <ul>
 * <li>fallbackOutcome = {@link HomePage#OUTCOME}</li>
 * <li>excludeParameter = derived by configuration
 * {@link PortalConfigurationKeys#HISTORY_EXCLUDE_PARAMETER}</li>
 * <li>excludeFromHistoryMatcher = derived by the configuration
 * {@link PortalConfigurationKeys#HISTORY_VIEW_EXCLUDE_PARAMETER}</li>
 * <li>historySize=10</li>
 * <li>excludeFacesParameter=true</li>
 * </ul>
 *
 * @author Oliver Wolff
 */
@ApplicationScoped
@EqualsAndHashCode
@ToString
public class DefaultHistoryConfiguration implements Serializable {

    /**
     * The default size for the history-size
     */
    static final int DEFAULT_HISTORY_SIZE = 10;
    @Serial
    private static final long serialVersionUID = 8178547799619418410L;
    private static final String FALLBACK_OUTCOME = HomePage.OUTCOME;

    private static final int HISTORY_SIZE = DEFAULT_HISTORY_SIZE;

    private static final boolean EXCLUDE_FACES_PARAMETER = true;

    @Getter
    private List<String> excludeParameter;

    @Getter
    private ViewMatcher excludeFromHistoryMatcher;

    protected DefaultHistoryConfiguration() {
        // for CDI proxy
    }

    @Inject
    public DefaultHistoryConfiguration(
            @ConfigProperty(name = HISTORY_EXCLUDE_PARAMETER) List<String> excludeParameter,
            @ConfigAsViewMatcher(name = HISTORY_VIEW_EXCLUDE_PARAMETER) ViewMatcher excludeFromHistoryMatcher) {
        this.excludeParameter = excludeParameter;
        this.excludeFromHistoryMatcher = excludeFromHistoryMatcher;
    }

    public String getFallbackOutcome() {
        return FALLBACK_OUTCOME;
    }

    public int getHistorySize() {
        return HISTORY_SIZE;
    }

    public boolean isExcludeFacesParameter() {
        return EXCLUDE_FACES_PARAMETER;
    }

}
