package de.cuioss.portal.ui.api.ui.context;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;
import javax.inject.Named;

/**
 * Produces a applicationScoped {@link NavigationHandler} instance
 *
 * @author Oliver Wolff
 */
@ApplicationScoped
public class NavigationHandlerProducer {

    /**
     * @return the derived {@link NavigationHandler}
     */
    @Produces
    @CuiNavigationHandler
    @ApplicationScoped
    @Named
    NavigationHandler getNavigationHandler() {
        return FacesContext.getCurrentInstance().getApplication().getNavigationHandler();
    }
}
