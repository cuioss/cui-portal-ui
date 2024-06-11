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
package de.cuioss.portal.ui.runtime.application.locale;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Locale;

import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.context.SessionScoped;
import jakarta.enterprise.event.Event;
import jakarta.enterprise.inject.Alternative;
import jakarta.enterprise.inject.Produces;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.inject.Provider;

import de.cuioss.portal.common.locale.LocaleChangeEvent;
import de.cuioss.portal.common.locale.PortalLocale;
import de.cuioss.portal.ui.api.PortalCoreBeanNames;
import de.cuioss.portal.ui.api.locale.LocaleResolverService;
import de.cuioss.portal.ui.runtime.application.locale.impl.PortalLocaleResolverServiceImpl;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * The {@link PortalLocaleManagerBean} is about tracking and interaction of
 * client specific locales. The implementation is a fixed (CDI-) scoped bean. In
 * order to change behavior the extension point is an implementation of
 * {@link LocaleResolverService} defined as an {@link Alternative} for
 * {@link PortalLocaleResolver}. The default implementation
 * {@link PortalLocaleResolverServiceImpl} uses the jsf based in standard
 * behavior. In addition it provides a producer method for {@link PortalLocale}
 * To put in other words: This bean provides a session-scoped cache for locale,
 * the used service is agnostic of state or bean specific types. In addition it
 * acts as an producer of {@link PortalLocale}: <em>Caution:</em>: Within
 * CDI-beans the only way to access the locale is injecting
 * {@link PortalLocale}. The producer is only meant as legacy bridge.
 *
 * @author Oliver Wolff
 */
@SessionScoped
@Named(PortalCoreBeanNames.PORTAL_LOCALE_MANAGER)
@EqualsAndHashCode(of = "locale")
@ToString(of = "locale")
public class PortalLocaleManagerBean implements Serializable {

    @Serial
    private static final long serialVersionUID = -3555387539352353982L;

    @Inject
    LocaleResolverService resolverService;

    private Locale locale;

    @Inject
    @LocaleChangeEvent
    Event<Locale> localeChangeEvent;

    @Inject
    Provider<FacesContext> facesContextProvider;

    /**
     * Producer method for {@link Locale} identified by {@link PortalLocale}
     *
     * @return the corresponding user specific locale.
     */
    @Produces
    @PortalLocale
    @Dependent
    Locale produceClientLocale() {
        return getUserLocale();
    }

    /** @see PortalLocaleResolverServiceImpl#getAvailableLocales() */
    public List<Locale> getAvailableLocales() {
        return resolverService.getAvailableLocales();
    }

    /** @see PortalLocaleResolverServiceImpl#saveUserLocale(Locale) */
    public void saveUserLocale(final Locale localeValue) {
        locale = localeValue;
        facesContextProvider.get().getViewRoot().setLocale(locale);
        resolverService.saveUserLocale(locale);
        localeChangeEvent.fire(locale);
    }

    /** @see PortalLocaleResolverServiceImpl#resolveUserLocale() */
    public Locale getUserLocale() {
        if (null == locale) {
            locale = resolverService.resolveUserLocale();
        }
        return locale;
    }

}
