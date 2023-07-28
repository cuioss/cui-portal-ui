package de.cuioss.portal.ui.runtime.support;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;

import de.cuioss.jsf.test.mock.application.ThemeConfigurationMock;
import de.cuioss.portal.ui.api.theme.PortalThemePersistencesService;
import de.cuioss.portal.ui.api.theme.ThemePersistenceService;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Mock variant of {@link ThemePersistenceService}
 *
 * @author Oliver Wolff
 */
@PortalThemePersistencesService
@ApplicationScoped
@Alternative
@EqualsAndHashCode
@ToString
public class PortalThemePersistenceServiceMock implements Serializable, ThemePersistenceService {

    private static final long serialVersionUID = -6613799095497133030L;

    @Getter
    private final List<String> availableThemes = ThemeConfigurationMock.AVAILABLE_THEMES;

    @Getter
    private String theme = ThemeConfigurationMock.DEFAULT_THEME;

    @Override
    public void saveTheme(final String newTheme) {
        if (!availableThemes.contains(newTheme)) {
            throw new IllegalArgumentException("Given theme '" + newTheme + "' must be one of " + availableThemes);
        }
        theme = newTheme;
    }

}
