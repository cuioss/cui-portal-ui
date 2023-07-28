package de.cuioss.portal.ui.runtime.application.locale.impl;

import static de.cuioss.portal.configuration.PortalConfigurationKeys.LOCALE_DEFAULT;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.annotation.Priority;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;

import de.cuioss.portal.configuration.common.PortalPriorities;
import de.cuioss.portal.core.storage.ClientStorage;
import de.cuioss.portal.core.storage.PortalClientStorage;
import de.cuioss.portal.ui.api.locale.LocaleResolverService;
import de.cuioss.portal.ui.api.locale.PortalLocaleResolver;
import de.cuioss.portal.ui.runtime.application.configuration.LocaleConfiguration;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Default implementation of {@link LocaleResolverService} that uses the jsf
 * standard behavior. The actual configuration of the locales is derived from
 * {@link LocaleConfiguration}. If you want to change this you must provide an
 * implementation with a higher {@link Priority}.
 *
 * @author Oliver Wolff
 */
@PortalLocaleResolver
@Priority(PortalPriorities.PORTAL_CORE_LEVEL)
@Named
@SessionScoped
@EqualsAndHashCode(of = { "locale", "availableLocales" }, doNotUseGetters = true)
@ToString(of = { "locale", "availableLocales" }, doNotUseGetters = true)
public class PortalLocaleResolverServiceImpl implements LocaleResolverService, Serializable {

    private static final long serialVersionUID = 2745227675026232302L;

    private Locale locale;

    @Getter
    private List<Locale> availableLocales;

    @Inject
    private Provider<FacesContext> facesContextProvider;

    @Inject
    private LocaleConfiguration localeConfiguration;

    @Inject
    @PortalClientStorage
    private ClientStorage clientStorage;

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
                    "Locale must be one of " + getAvailableLocales() + ", but was " + this.locale);
        }
        this.clientStorage.put(LOCALE_DEFAULT, localeValue.toLanguageTag());
        this.locale = null;
    }

    @Override
    public Locale getLocale() {
        if (null == locale) {

            final var localeFromClientStorage = extractFromClientStorage();
            if (localeFromClientStorage.isPresent()) {
                this.locale = localeFromClientStorage.get();
            } else {
                final var localeFromViewRoot = extractFromViewRoot();
                if (!localeFromViewRoot.isPresent()) {
                    // do not persist "dirty" result in this session scoped bean
                    return localeConfiguration.getDefaultLocale();
                }
                this.locale = localeFromViewRoot.get();
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
            final var storedLocale = Locale.forLanguageTag(this.clientStorage.get(LOCALE_DEFAULT));
            if (getAvailableLocales().contains(storedLocale)) {
                return Optional.of(storedLocale);
            }
        }
        return Optional.empty();
    }

}
