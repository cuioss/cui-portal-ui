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
package de.cuioss.portal.ui.runtime.application.configuration;

import de.cuioss.portal.common.priority.PortalPriorities;
import de.cuioss.portal.configuration.PortalConfigurationKeys;
import de.cuioss.portal.configuration.types.ConfigAsLocale;
import de.cuioss.portal.configuration.types.ConfigAsLocaleList;
import de.cuioss.tools.logging.CuiLogger;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Provider;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

import static de.cuioss.portal.configuration.PortalConfigurationKeys.LOCALES_AVAILABLE;
import static de.cuioss.portal.configuration.PortalConfigurationKeys.LOCALE_DEFAULT;
import static de.cuioss.tools.collect.CollectionLiterals.immutableList;

/**
 * Defines the locale configuration for the CUI-portal. Instead of using the
 * built-in mechanics of jsf we use a custom one in order to configure it with
 * our property based configuration system. It consumes the properties
 * {@link PortalConfigurationKeys#LOCALE_DEFAULT} and
 * {@link PortalConfigurationKeys#LOCALES_AVAILABLE}
 *
 * @author Oliver Wolff
 */
@Priority(PortalPriorities.PORTAL_CORE_LEVEL)
@ApplicationScoped
@EqualsAndHashCode(of = {"defaultLocale", "availableLocales"})
@ToString(of = {"defaultLocale", "availableLocales"})
public class LocaleConfiguration implements Serializable {

    private static final long serialVersionUID = 4249111201638474035L;

    private static final CuiLogger log = new CuiLogger(LocaleConfiguration.class);

    @Inject
    @ConfigAsLocale(name = LOCALE_DEFAULT, defaultToSystem = false)
    private Provider<Locale> localeDefaultProvider;

    @Inject
    @ConfigAsLocaleList(name = LOCALES_AVAILABLE, defaultToSystem = false)
    private Provider<List<Locale>> localesAvailableProvider;

    @Getter
    private Locale defaultLocale;

    @Getter
    private List<Locale> availableLocales;

    /**
     * Configures the locales
     */
    @PostConstruct
    public void init() {
        try {
            defaultLocale = localeDefaultProvider.get();
        } catch (final IllegalArgumentException e) {
            log.warn("Invalid configuration found for {} defaulting to {}", LOCALE_DEFAULT, Locale.ENGLISH);
            defaultLocale = Locale.ENGLISH;
        }

        try {
            availableLocales = localesAvailableProvider.get();
        } catch (final IllegalArgumentException e) {
            log.warn("Invalid configuration found for {} defaulting to {}", LOCALES_AVAILABLE, defaultLocale);
            availableLocales = immutableList(defaultLocale);
        }
    }

}
