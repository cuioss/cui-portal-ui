package de.cuioss.portal.ui.api.ui.pages;

import java.io.Serializable;

/**
 * Specifies the page bean backing the preferences page.
 *
 * @author Oliver Wolff
 */
@SuppressWarnings("squid:S1214") // We allow constants in the page interfaces, because they belong together
                                 // (coherence).
public interface PreferencesPage extends Serializable {

    /**
     * Bean name for looking up instances.
     */
    String BEAN_NAME = "preferencesPageBean";

    /**
     * The outcome used for navigation to the preferences page.
     */
    String OUTCOME = "preferences";

}
