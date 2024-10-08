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
package de.cuioss.portal.ui.runtime.application.locale.impl;

import de.cuioss.portal.common.priority.PortalPriorities;
import de.cuioss.portal.core.storage.ClientStorage;
import de.cuioss.portal.core.storage.PortalClientStorage;
import de.cuioss.portal.ui.api.locale.LocaleResolverService;
import de.cuioss.portal.ui.runtime.application.configuration.LocaleConfiguration;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.inject.Provider;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static de.cuioss.portal.configuration.PortalConfigurationKeys.LOCALE_DEFAULT;

/**
 * Default implementation of {@link LocaleResolverService} that uses the jsf
 * standard behavior. The actual configuration of the locales is derived from
 * {@link LocaleConfiguration}. If you want to change this you must provide an
 * implementation with a higher {@link Priority}.
 *
 * @author Oliver Wolff
 */
@Priority(PortalPriorities.PORTAL_CORE_LEVEL)
@Named
@SessionScoped
@EqualsAndHashCode(of = {"locale", "availableLocales"}, doNotUseGetters = true)
@ToString(of = {"locale", "availableLocales"}, doNotUseGetters = true)
public class PortalLocaleResolverServiceImpl implements LocaleResolverService, Serializable {

    @Serial
    private static final long serialVersionUID = 2745227675026232302L;
    @Inject
    Provider<FacesContext> facesContextProvider;
    @Inject
    LocaleConfiguration localeConfiguration;
    @Inject
    @PortalClientStorage
    ClientStorage clientStorage;
    private Locale locale;
    @Getter
    private List<Locale> availableLocales;

    /**
     * Initializer method for the bean
     */
    @PostConstruct
    public void initBean() {
        availableLocales = localeConfiguration.getAvailableLocales();
    }

    @Override
    public void saveUserLocale(final Locale localeValue) {
        if (!getAvailableLocales().contains(localeValue)) {
            throw new IllegalArgumentException(
                    "Locale must be one of " + getAvailableLocales() + ", but was " + locale);
        }
        clientStorage.put(LOCALE_DEFAULT, localeValue.toLanguageTag());
        locale = null;
    }

    @Override
    public Locale resolveUserLocale() {
        if (null == locale) {

            final var localeFromClientStorage = extractFromClientStorage();
            if (localeFromClientStorage.isPresent()) {
                locale = localeFromClientStorage.get();
            } else {
                final var localeFromViewRoot = extractFromViewRoot();
                if (localeFromViewRoot.isEmpty()) {
                    // do not persist "dirty" result in this session scoped bean
                    return localeConfiguration.getDefaultLocale();
                }
                locale = localeFromViewRoot.get();
            }
        }
        return locale;
    }

    private Optional<Locale> extractFromViewRoot() {
        var facesContext = facesContextProvider.get();
        if (!facesContext.isReleased()) {
            return Optional.ofNullable(facesContext.getApplication().getViewHandler().calculateLocale(facesContext));
        }
        return Optional.empty();
    }

    private Optional<Locale> extractFromClientStorage() {
        if (clientStorage.containsKey(LOCALE_DEFAULT)) {
            final var storedLocale = Locale.forLanguageTag(clientStorage.get(LOCALE_DEFAULT));
            if (getAvailableLocales().contains(storedLocale)) {
                return Optional.of(storedLocale);
            }
        }
        return Optional.empty();
    }

}
