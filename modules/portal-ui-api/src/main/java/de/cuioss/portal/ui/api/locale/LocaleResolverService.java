package de.cuioss.portal.ui.api.locale;

import java.util.List;
import java.util.Locale;

import de.cuioss.jsf.api.application.locale.LocaleProducer;

/**
 * Extension to {@link LocaleProducer} that adds additional methods like access
 * on configured locales and changing the locale on per user basis.
 *
 * @author Oliver Wolff
 */
public interface LocaleResolverService extends LocaleProducer {

    /**
     * @return the list of available locales for the current user.
     */

    List<Locale> getAvailableLocales();

    /**
     * Saves the locale changed by user interaction
     *
     * @param locale to be updated. Must be one of {@link #getAvailableLocales()}.
     *               Otherwise it will throws an {@link IllegalArgumentException}
     */
    void saveUserLocale(Locale locale);
}
