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

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Provider;

import de.cuioss.jsf.api.application.navigation.NavigationUtils;
import de.cuioss.portal.authentication.oauth.OauthRedirector;

/**
 * Implementation of the {@link OauthRedirector} interface using
 * {@link NavigationUtils#redirect(FacesContext, String)}.
 */
@ApplicationScoped
public class RedirectorImpl implements OauthRedirector {

    @Inject
    private Provider<FacesContext> facesContextProvider;

    @Override
    public void sendRedirect(String url) throws IllegalStateException {
        NavigationUtils.redirect(facesContextProvider.get(), url);
    }
}
