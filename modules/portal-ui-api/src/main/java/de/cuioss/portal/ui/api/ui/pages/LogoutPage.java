package de.cuioss.portal.ui.api.ui.pages;

import java.io.Serializable;

/**
 * Provides methods for handling the logout. It can be used from the logout page
 * {@link #logoutViewAction()} or by programmatically calling
 * {@link #performLogout()}
 *
 * @author Oliver Wolff
 */
@SuppressWarnings("squid:S1214") // We allow constants in the page interfaces, because they belong together
                                 // (coherence).
public interface LogoutPage extends Serializable {

    /**
     * Bean name for looking up instances.
     */
    String BEAN_NAME = "logoutPageBean";

    /**
     * The outcome used for navigation to the logout page.
     */
    String OUTCOME = "logout";

    /**
     * A view action triggering the logout.
     *
     * @return null in every case, due to signature constraint from jsf-view-action
     */
    String logoutViewAction();

    /**
     * @return null if logout was successful otherwise {@link LoginPage#OUTCOME} in
     *         order to force a login.
     */
    String performLogout();

}
