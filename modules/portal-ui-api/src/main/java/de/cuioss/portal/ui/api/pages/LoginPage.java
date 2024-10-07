/*
 * Copyright 2023 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * https://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.cuioss.portal.ui.api.pages;

import de.cuioss.portal.authentication.model.UserStore;
import de.cuioss.portal.configuration.PortalConfigurationKeys;
import de.cuioss.portal.ui.api.history.HistoryManager;
import de.cuioss.uimodel.application.LoginCredentials;
import jakarta.enterprise.context.RequestScoped;

import java.io.Serializable;
import java.util.List;

/**
 * Specifies the page bean for the form-login. It should usually be
 * {@link RequestScoped} in order to be used with non-transient views.
 *
 * @author Oliver Wolff
 */
@SuppressWarnings("squid:S1214") // We allow constants in the page interfaces, because they belong together
                                 // (coherence).
public interface LoginPage extends Serializable {

    /**
     * Bean name for looking up instances.
     */
    String BEAN_NAME = "loginPageBean";

    /**
     * The outcome used for navigation to the login page.
     */
    String OUTCOME = "login";

    /** URL Parameter / Local Storage key name */
    String KEY_USERSTORE = "userstore";

    /** URL Parameter / Local Storage key name */
    String KEY_USERNAME = "username";

    /** Local Storage key name */
    String KEY_REMEMBER_ME = "remember_me";

    /**
     * Init-View-Action used for checking whether the user needs to be redirected.
     * In the default implementation this is the case if the user is already
     * authenticated. See {@link PortalConfigurationKeys#PAGES_LOGIN_ENTER_STRATEGY}
     * for details
     *
     * @return null if no redirect is necessary otherwise corresponding outcome
     */
    String initViewAction();

    /**
     * @return the {@link LoginCredentials} to be filled by the form.
     */
    LoginCredentials getLoginCredentials();

    /**
     * @return the list of {@link UserStore} top be used. It is meant to be
     *         displayed within the drop-down labeled "UserStore"
     */
    List<UserStore> getAvailableUserStores();

    /**
     * @return boolean indicating whether to display a dropdown for choosing a
     *         ldapserver, in other words {@link #getAvailableUserStores()
     *         .getSize()} > 1
     */
    boolean isShouldDisplayUserStoreDropdown();

    /**
     * Executes the login.
     *
     * @return String outcome, depending on the login-status. On successful login it
     *         returns "home", in case of not successful login it returns null.
     *         <em>Caution:</em> Implementations must support deep-linking, saying a
     *         requested url must be called after successful login. This is usually
     *         done by using the {@link HistoryManager}
     */
    String login();

    /**
     * @return the string representing the component to be focused: either
     *         "username" or "password"
     */
    String getFocusComponent();

}
