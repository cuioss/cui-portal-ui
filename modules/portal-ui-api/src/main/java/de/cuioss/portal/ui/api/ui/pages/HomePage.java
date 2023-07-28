package de.cuioss.portal.ui.api.ui.pages;

import java.io.Serializable;

/**
 * Specifies the virtual home page. Currently it solely defines the String
 * outcome.
 *
 * @author Oliver Wolff
 */
@SuppressWarnings("squid:S1214") // We allow constants in the page interfaces, because they belong together
                                 // (coherence).
public interface HomePage extends Serializable {

    /**
     * The outcome used for navigation to the home/start page.
     */
    String OUTCOME = "home";

}
