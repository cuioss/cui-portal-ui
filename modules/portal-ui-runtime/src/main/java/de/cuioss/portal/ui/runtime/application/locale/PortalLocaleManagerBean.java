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

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

import javax.enterprise.context.Dependent;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Alternative;
import javax.enterprise.inject.Produces;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Provider;

import de.cuioss.portal.core.locale.PortalLocale;
import de.cuioss.portal.ui.api.locale.LocaleChangeEvent;
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
@EqualsAndHashCode(of = "locale")
@ToString(of = "locale")
public class PortalLocaleManagerBean implements Serializable {

    private static final long serialVersionUID = -3555387539352353982L;

    @Inject
    private LocaleResolverService resolverService;

    private Locale locale;

    @Inject
    @LocaleChangeEvent
    private Event<Locale> localeChangeEvent;

    @Inject
    private Provider<FacesContext> facesContextProvider;

    /**
     * Producer method for {@link Locale} identified by {@link PortalLocale}
     *
     * @return the corresponding user specific locale.
     */
    @Produces
    @PortalLocale
    @Dependent
    Locale produceClientLocale() {
        return resolveUserLocale();
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
    public Locale resolveUserLocale() {
        if (null == locale) {
            locale = resolverService.resolveUserLocale();
        }
        return locale;
    }

}
