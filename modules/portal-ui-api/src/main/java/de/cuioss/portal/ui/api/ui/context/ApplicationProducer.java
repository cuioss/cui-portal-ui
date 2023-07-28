package de.cuioss.portal.ui.api.ui.context;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.faces.FactoryFinder;
import javax.faces.application.Application;
import javax.faces.application.ApplicationFactory;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

/**
 * Produces an {@link ApplicationScoped} {@link Application} instance. In order
 * to be independent from individual requests it uses the {@link FactoryFinder}
 * directly instead of using {@link FacesContext#getApplication()}.
 *
 * Mojarra uses {@link ExternalContext#getContext()} which returns the
 * application environment object instance for the current application as an
 * {@link Object}.
 *
 * However, we are interested in the actual
 * <code>de.cuioss.portal.ui.api.application.PortalApplication</code> stored in
 * the
 * <code>de.cuioss.portal.ui.api.application.factory.PortalApplicationFactory</code>.
 *
 * Also see the documentation from Mojarras <a href=
 * "https://github.com/eclipse-ee4j/mojarra/blob/master/impl/src/main/java/javax/faces/application/ApplicationFactory.java">ApplicationFactory</a>.
 *
 * @author Sven Haag, Sven Haag
 */
@ApplicationScoped
public class ApplicationProducer {

    @Produces
    @ApplicationScoped
    Application getApplication() {
        return ((ApplicationFactory) FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY)).getApplication();
    }
}
