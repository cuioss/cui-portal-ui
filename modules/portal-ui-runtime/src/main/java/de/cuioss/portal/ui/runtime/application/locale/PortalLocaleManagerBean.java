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
import javax.inject.Named;
import javax.inject.Provider;

import de.cuioss.jsf.api.application.locale.LocaleProducer;
import de.cuioss.jsf.api.application.locale.LocaleProducerImpl;
import de.cuioss.portal.core.locale.PortalLocale;
import de.cuioss.portal.ui.api.locale.LocaleChangeEvent;
import de.cuioss.portal.ui.api.locale.LocaleResolverService;
import de.cuioss.portal.ui.api.locale.PortalLocaleResolver;
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
 * acts as an implementation of {@link LocaleProducer}: <em>Caution:</em>:
 * Within CDI-beans the only way to access the locale is injecting
 * {@link PortalLocale}. The producer is only meant as legacy bridge.
 *
 * @author Oliver Wolff
 */
@Named(LocaleProducerImpl.BEAN_NAME)
@SessionScoped
@EqualsAndHashCode(of = "locale")
@ToString(of = "locale")
public class PortalLocaleManagerBean implements Serializable, LocaleResolverService {

    private static final long serialVersionUID = -3555387539352353982L;

    @Inject
    @PortalLocaleResolver
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
        return getLocale();
    }

    @Override
    public List<Locale> getAvailableLocales() {
        return this.resolverService.getAvailableLocales();
    }

    @Override
    public void saveUserLocale(final Locale localeValue) {
        this.locale = localeValue;
        this.facesContextProvider.get().getViewRoot().setLocale(this.locale);
        this.resolverService.saveUserLocale(this.locale);
        this.localeChangeEvent.fire(this.locale);
    }

    @Override
    public Locale getLocale() {
        if (null == locale) {
            this.locale = this.resolverService.getLocale();
        }
        return locale;
    }

}
