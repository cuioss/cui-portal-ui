package de.cuioss.portal.ui.api.ui.pages;

import java.io.Serializable;

/**
 * Specifies the page bean backing the about page.
 *
 * @author Oliver Wolff
 */
@SuppressWarnings("squid:S1214") // We allow constants in the page interfaces, because they belong together
                                 // (coherence).
public interface AboutPage extends Serializable {

    /**
     * Bean name for looking up instances.
     */
    String BEAN_NAME = "aboutPageBean";

    /**
     * The outcome used for navigation to the help page.
     */
    String OUTCOME = "about";

}
