package de.cuioss.portal.ui.api.ui.pages;

import java.io.Serializable;

import de.cuioss.portal.core.storage.PortalSessionStorage;
import de.cuioss.portal.ui.api.exception.DefaultErrorMessage;

/**
 * Specifies the page bean backing the error page.
 *
 * @author Oliver Wolff
 */
@SuppressWarnings("squid:S1214") // We allow constants in the page interfaces, because they belong together
                                 // (coherence).
public interface ErrorPage extends Serializable {

    /**
     * Bean name for looking up instances.
     */
    String BEAN_NAME = "errorPageBean";

    /**
     * The outcome used for navigation to the error page.
     */
    String OUTCOME = "error";

    /**
     * @return the message derived by {@link PortalSessionStorage}
     */
    DefaultErrorMessage getMessage();

    /**
     * @return flag indicating whether a message is available / to be displayed
     */
    boolean isMessageAvailable();
}
