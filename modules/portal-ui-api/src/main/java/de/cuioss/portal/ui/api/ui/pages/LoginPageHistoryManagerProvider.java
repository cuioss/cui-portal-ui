package de.cuioss.portal.ui.api.ui.pages;

import java.util.Optional;

import de.cuioss.jsf.api.application.history.HistoryManager;
import de.cuioss.jsf.api.application.navigation.ViewIdentifier;
import de.cuioss.uimodel.application.LoginCredentials;

/**
 * {@linkplain LoginPageHistoryManagerProvider} is a decorator for
 * {@linkplain HistoryManager} which provide shortcut methods for login page
 * specific interaction
 *
 * @author i000576
 */
public interface LoginPageHistoryManagerProvider {

    /**
     * Extract userStore and userName from deep link URL
     *
     * @param userStore used as default value
     * @param username  used as default value
     * @return option for {@linkplain LoginCredentials} extract from url parameter,
     *         empty option if parameters are missing
     */
    Optional<LoginCredentials> extractFromDeepLinkingUrlParameter(final String userStore, final String username);

    /**
     * Retrieve current view with cleaned up URL parameter
     *
     * @return {@linkplain ViewIdentifier}
     */
    ViewIdentifier getCurrentViewExcludeUserStoreAndUserName();

    /**
     * Retrieve wrapped Object
     *
     * @return {@linkplain HistoryManager}
     */
    HistoryManager getWrapped();
}
