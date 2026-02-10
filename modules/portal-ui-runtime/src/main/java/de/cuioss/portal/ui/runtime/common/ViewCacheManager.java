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
package de.cuioss.portal.ui.runtime.common;

import de.cuioss.portal.authentication.AuthenticatedUserInfo;
import de.cuioss.portal.authentication.UserChangeEvent;
import de.cuioss.portal.common.locale.LocaleChangeEvent;
import de.cuioss.portal.ui.api.events.PageRefreshEvent;
import de.cuioss.tools.logging.CuiLogger;
import jakarta.enterprise.context.SessionScoped;
import jakarta.enterprise.event.Observes;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.inject.Provider;
import lombok.ToString;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.omnifaces.util.cache.CacheFactory;

import java.io.Serial;
import java.io.Serializable;
import java.util.Locale;

import static de.cuioss.portal.configuration.PortalConfigurationKeys.ENABLE_CACHE;

/**
 * To manage the disabled property of o:cache for the header in root.xhtml and
 * reset the cache if needed.
 */
@SessionScoped
@Named
@ToString(of = "enabled")
public class ViewCacheManager implements Serializable {

    @Serial
    private static final long serialVersionUID = -7261263947597749229L;

    private static final CuiLogger LOGGER = new CuiLogger(ViewCacheManager.class);

    private boolean enabled;
    private Provider<FacesContext> contextProvider;

    protected ViewCacheManager() {
        // for CDI proxy
    }

    @Inject
    public ViewCacheManager(@ConfigProperty(name = ENABLE_CACHE) boolean enabled,
            Provider<FacesContext> contextProvider) {
        this.enabled = enabled;
        this.contextProvider = contextProvider;
    }

    /**
     * @return boolean indicating whether the cache is disabled by configuration
     */
    public boolean isDisabled() {
        return !enabled;
    }

    /**
     * Reset the cache when the authenticated user was changed during the session
     *
     * @param newUserInfo ignored
     */
    void actOnUserChangeEvent(@Observes @UserChangeEvent final AuthenticatedUserInfo newUserInfo) {
        LOGGER.debug("Acting on UserChangeEvent, new: '%s'", newUserInfo);
        resetHeaderCache();
    }

    /**
     * Reset the cache when the locale was changed during the session
     *
     * @param newLocale ignored
     */
    void actOnLocaleChangeEvent(@Observes @LocaleChangeEvent final Locale newLocale) {
        LOGGER.debug("Acting on LocaleChangeEvent, new: '%s'", newLocale);
        resetHeaderCache();
    }

    /**
     * Reset the cache when the page was reloaded (e.g. F5 or reload button).
     *
     * @param pageRefreshEvent ignored
     */
    void actOnPageRefreshEvent(@Observes final PageRefreshEvent pageRefreshEvent) {
        LOGGER.debug("Acting on PageRefreshEvent, new: '%s'", pageRefreshEvent);
        resetHeaderCache();
    }

    private void resetHeaderCache() {
        LOGGER.debug("Resetting cache");
        CacheFactory.getCache(contextProvider.get(), "session").remove("header");
    }

}
