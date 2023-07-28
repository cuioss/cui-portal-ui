package de.cuioss.portal.ui.api.theme;

import de.cuioss.jsf.api.application.theme.ThemeConfiguration;
import de.cuioss.jsf.api.application.theme.ThemeNameProducer;

/**
 * Simple Interface defining methods for storing / saving userdefined themes.
 *
 * @author Oliver Wolff
 */
public interface ThemePersistenceService extends ThemeNameProducer {

    /**
     * Saves the theme if a user has selected a new one in the preferences. The
     * Service must store it accordingly.
     *
     * @param newTheme to be set, must be one of
     *                 {@link ThemeConfiguration#getAvailableThemes()}. The
     *                 implementation must ensure this.
     */
    void saveTheme(String newTheme);
}
