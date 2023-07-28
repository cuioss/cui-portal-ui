package de.cuioss.portal.ui.api.ui.pages;

import java.io.Serializable;

/**
 * Specifies the page bean backing the account page.
 *
 * @author Oliver Wolff
 */
@SuppressWarnings("squid:S1214") // We allow constants in the page interfaces, because they belong together
                                 // (coherence).
public interface AccountPage extends Serializable {

    /**
     * Bean name for looking up instances.
     */
    String BEAN_NAME = "accountPageBean";

    /**
     * The outcome used for navigation to the account page.
     */
    String OUTCOME = "account";

}
