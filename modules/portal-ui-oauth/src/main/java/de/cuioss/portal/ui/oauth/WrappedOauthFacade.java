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
package de.cuioss.portal.ui.oauth;

import de.cuioss.jsf.api.application.navigation.ViewIdentifier;
import de.cuioss.portal.authentication.oauth.Oauth2AuthenticationFacade;

import java.io.Serializable;
import java.util.Map;

/**
 * Wrapper for the {@link Oauth2AuthenticationFacade} using JSF specific
 * providers and provide simple methods to the user.
 *
 * @author Matthias Walliczek
 */
public interface WrappedOauthFacade {

    /**
     * Retrieve a token for the default scopes. May cause a redirect to the oauth
     * server.
     *
     * @return the accessToken is available, otherwise null is returned, a redirect
     * to the oauth server is issued, and the page is accessed a second
     * time.
     */
    String retrieveToken();

    /**
     * Retrieve a token for the given scopes. May cause a redirect to the oauth
     * server.
     *
     * @param scopes the scopes as space separated list.
     * @return the accessToken is available, otherwise null is returned, a redirect
     * to the oauth server is issued, and the page is accessed a second
     * time.
     */
    String retrieveToken(String scopes);

    /**
     * Handles a {@link MissingScopesException} by retrieving the missing scopes and
     * triggering a redirect to the oauth server if necessary.
     *
     * @param e              The exception to handle
     * @param viewParameters A map of view parameters to keep during the oauth
     *                       redirect
     */
    void handleMissingScopesException(MissingScopesException e, Map<String, Serializable> viewParameters);

    /**
     * Handles a {@link MissingScopesException} by retrieving the missing scopes and
     * triggering a redirect to the oauth server if necessary.
     *
     * @param e              The exception to handle
     * @param initialScopes  The initial scopes to be added to the missing scopes
     * @param viewParameters A map of view parameters to keep during the oauth
     *                       redirect
     */
    void handleMissingScopesException(MissingScopesException e, String initialScopes,
            Map<String, Serializable> viewParameters);

    /**
     * Retrieve the view parameters stored by
     * {@link #handleMissingScopesException(MissingScopesException, Map)} after the
     * redirect to the oauth server
     *
     * @return a map of view parameters
     */
    Map<String, Serializable> retrieveViewParameters();

    /**
     * Retrieve the target view from the history manager. Because of problems with
     * the window scope after redirect from oauth server, use a session stored target
     * view if present.
     *
     * @return
     */
    ViewIdentifier retrieveTargetView();

    /**
     * Store the target view in the session if not already present to handle deep
     * linking
     */
    void preserveCurrentView();
}
