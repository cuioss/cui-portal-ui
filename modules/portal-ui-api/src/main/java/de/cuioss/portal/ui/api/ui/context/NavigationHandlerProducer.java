package de.cuioss.portal.ui.api.ui.context;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.faces.application.Application;
import javax.faces.application.NavigationHandler;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Produces a applicationScoped {@link NavigationHandler} instance
 *
 * @author Oliver Wolff
 */
@ApplicationScoped
public class NavigationHandlerProducer {

    @Inject
    private Application application;

    /**
     * @return the derived {@link NavigationHandler}
     */
    @Produces
    @CuiNavigationHandler
    @ApplicationScoped
    @Named
    NavigationHandler getNavigationHandler() {
        return application.getNavigationHandler();
    }
}
