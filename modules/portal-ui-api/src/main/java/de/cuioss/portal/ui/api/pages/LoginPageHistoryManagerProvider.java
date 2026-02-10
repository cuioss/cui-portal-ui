/*
 * Copyright © 2025 CUI-OpenSource-Software (info@cuioss.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.cuioss.portal.ui.api.pages;

import de.cuioss.jsf.api.application.navigation.ViewIdentifier;
import de.cuioss.portal.ui.api.history.HistoryManager;
import de.cuioss.uimodel.application.LoginCredentials;

import java.util.Optional;

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
     * empty option if parameters are missing
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
