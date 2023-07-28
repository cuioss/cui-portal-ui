package de.cuioss.portal.ui.api.ui.pages;

import java.util.function.Supplier;

import de.cuioss.portal.core.storage.ClientStorage;
import de.cuioss.uimodel.application.LoginCredentials;

/**
 * {@linkplain LoginPageClientStorage} is a decorator for
 * {@linkplain ClientStorage} which provide shortcut methods for login page
 * specific interaction
 *
 * @author i000576
 */
public interface LoginPageClientStorage {

    /**
     * {@linkplain Supplier} use {@linkplain ClientStorage} to extract
     * {@linkplain LoginPage#KEY_USERSTORE}, {@linkplain LoginPage#KEY_USERNAME} and
     * {@linkplain LoginPage#KEY_REMEMBER_ME}. The retrieved
     * {@linkplain LoginCredentials} could be still empty if no data is available
     * but never {@code null}
     *
     * @return {@linkplain LoginCredentials}
     */
    Supplier<LoginCredentials> extractFromClientStorage();

    /**
     * Update local stored login credentials according passed throw parameter
     *
     * @param loginCredentials {@linkplain LoginCredentials} must not be
     *                         {@code null}
     * @throws {@linkplain NullPointerException} if parameter is {@code null}
     */
    void updateLocalStored(final LoginCredentials loginCredentials);

    /**
     * Retrieve wrapped
     *
     * @return {@linkplain ClientStorage}
     */
    ClientStorage getWrapped();

}
