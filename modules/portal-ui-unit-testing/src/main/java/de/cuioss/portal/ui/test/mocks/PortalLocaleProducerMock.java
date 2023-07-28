package de.cuioss.portal.ui.test.mocks;

import static de.cuioss.tools.collect.CollectionLiterals.immutableList;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

import de.cuioss.jsf.api.application.locale.LocaleProducer;
import de.cuioss.jsf.api.application.locale.LocaleProducerImpl;
import de.cuioss.portal.core.locale.PortalLocale;
import de.cuioss.portal.ui.api.locale.LocaleChangeEvent;
import de.cuioss.test.jsf.config.BeanConfigurator;
import de.cuioss.test.jsf.config.decorator.BeanConfigDecorator;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Mock version of {@link LocaleProducer}
 *
 * @author Oliver Wolff
 */
@Named(LocaleProducerImpl.BEAN_NAME)
@ApplicationScoped
@EqualsAndHashCode
@ToString
public class PortalLocaleProducerMock implements LocaleProducer, BeanConfigurator, Serializable {

    private static final long serialVersionUID = 901932913924354093L;

    @Getter
    @Setter
    @Produces
    @Dependent
    @PortalLocale
    private Locale locale = Locale.ENGLISH;

    @Getter
    @Setter
    private List<Locale> availableLocales = immutableList(Locale.GERMAN, Locale.ENGLISH);

    @Inject
    @LocaleChangeEvent
    Event<Locale> localeChangeEvent;

    @Override
    public void configureBeans(final BeanConfigDecorator decorator) {
        decorator.register(new PortalLocaleProducerMock());
    }

}
