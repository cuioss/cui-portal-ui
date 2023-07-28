package de.cuioss.portal.ui.oauth;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Provider;

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
