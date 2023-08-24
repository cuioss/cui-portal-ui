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
