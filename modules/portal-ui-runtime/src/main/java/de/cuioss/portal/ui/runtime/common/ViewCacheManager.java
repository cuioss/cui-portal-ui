package de.cuioss.portal.ui.runtime.common;

import static de.cuioss.portal.configuration.PortalConfigurationKeys.ENABLE_CACHE;

import java.io.Serializable;
import java.util.Locale;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Observes;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.omnifaces.util.cache.CacheFactory;

import de.cuioss.portal.authentication.AuthenticatedUserInfo;
import de.cuioss.portal.authentication.UserChangeEvent;
import de.cuioss.portal.ui.api.events.PageRefreshEvent;
import de.cuioss.portal.ui.api.locale.LocaleChangeEvent;
import lombok.ToString;

/**
 * To manage the disabled property of o:cache for the header in root.xhtml and
 * reset the cache if needed.
 */
@SessionScoped
@Named
@ToString(of = "enabled")
public class ViewCacheManager implements Serializable {

    private static final long serialVersionUID = -7261263947597749229L;

    @Inject
    @ConfigProperty(name = ENABLE_CACHE)
    private boolean enabled;

    /**
     * @return boolean indicating whether the cache is disabled by configuration
     */
    public boolean isDisabled() {
        return !enabled;
    }

    @Inject
    private Provider<FacesContext> contextProvider;

    /**
     * Reset the cache when the authenticated user was changed during the session
     *
     * @param newUserInfo ignored
     */
    void actOnUserChangeEvent(@Observes @UserChangeEvent final AuthenticatedUserInfo newUserInfo) {
        resetHeaderCache();
    }

    /**
     * Reset the cache when the locale was changed during the session
     *
     * @param newLocale ignored
     */
    void actOnLocaleChangeEvent(@Observes @LocaleChangeEvent final Locale newLocale) {
        resetHeaderCache();
    }

    /**
     * Reset the cache when the page was reloaded (e.g. F5 or reload button).
     *
     * @param pageRefreshEvent ignored
     */
    void actOnPageRefreshEvent(@Observes final PageRefreshEvent pageRefreshEvent) {
        resetHeaderCache();
    }

    private void resetHeaderCache() {
        CacheFactory.getCache(contextProvider.get(), "session").remove("header");
    }

}
