package de.cuioss.portal.ui.runtime.application.history;

import static de.cuioss.portal.configuration.PortalConfigurationKeys.HISTORY_EXCLUDE_PARAMETER;
import static de.cuioss.portal.configuration.PortalConfigurationKeys.HISTORY_VIEW_EXCLUDE_PARAMETER;

import java.util.List;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import de.cuioss.jsf.api.application.history.HistoryConfiguration;
import de.cuioss.jsf.api.application.history.impl.HistoryConfigurationImpl;
import de.cuioss.jsf.api.application.view.matcher.ViewMatcher;
import de.cuioss.portal.configuration.PortalConfigurationKeys;
import de.cuioss.portal.ui.api.configuration.types.ConfigAsViewMatcher;
import de.cuioss.portal.ui.api.ui.pages.HomePage;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Default {@link HistoryConfiguration} for the CDI-Portal, defaulting to:
 * <ul>
 * <li>fallbackOutcome = {@link HomePage#OUTCOME}</li>
 * <li>excludeParameter = derived by web.xml with the key
 * {@link PortalConfigurationKeys#HISTORY_EXCLUDE_PARAMETER}</li>
 * <li>excludeFromHistoryMatcher = derived by web.xml with the key
 * {@link PortalConfigurationKeys#HISTORY_VIEW_EXCLUDE_PARAMETER}</li>
 * <li>historySize=10</li>
 * <li>excludeFacesParameter=true</li>
 * </ul>
 *
 * @author Oliver Wolff
 */
@Dependent
@Named(HistoryConfigurationImpl.BEAN_NAME)
@EqualsAndHashCode
@ToString
public class DefaultHistoryConfiguration implements HistoryConfiguration {

    private static final long serialVersionUID = 8178547799619418410L;

    @Getter
    private String fallback;

    @Getter
    private final String fallbackOutcome = HomePage.OUTCOME;

    @Getter
    private final int historySize = HistoryConfigurationImpl.DEFAULT_HISTORY_SIZE;

    @Getter
    private final boolean excludeFacesParameter = true;

    @Getter
    @Inject
    @ConfigProperty(name = HISTORY_EXCLUDE_PARAMETER)
    private List<String> excludeParameter;

    @Getter
    @Inject
    @ConfigAsViewMatcher(name = HISTORY_VIEW_EXCLUDE_PARAMETER)
    private ViewMatcher excludeFromHistoryMatcher;

}
