package de.cuioss.portal.ui.api.authentication;

import de.cuioss.portal.authentication.AuthenticatedUserInfo;

/**
 * To be fired if a user is not authenticated. It will be derived from
 * {@link AuthenticatedUserInfo#isAuthenticated()}
 *
 * @author Oliver Wolff
 */
public class UserNotAuthenticatedException extends RuntimeException {

    private static final long serialVersionUID = -587461529456917746L;

}
