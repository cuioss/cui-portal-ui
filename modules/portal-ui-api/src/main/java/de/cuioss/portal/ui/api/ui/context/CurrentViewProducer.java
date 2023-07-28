package de.cuioss.portal.ui.api.ui.context;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import de.cuioss.jsf.api.application.navigation.NavigationUtils;
import de.cuioss.jsf.api.common.view.ViewDescriptor;
import de.cuioss.jsf.api.common.view.ViewDescriptorImpl;

/**
 * Produces a requestScoped {@link ViewDescriptor} typed with
 * {@link CuiCurrentView}.
 *
 * @author Oliver Wolff
 */
@ApplicationScoped
public class CurrentViewProducer {

    /**
     * @return the derived {@link ViewDescriptor}
     */
    @Produces
    @Named
    @CuiCurrentView
    @RequestScoped
    ViewDescriptor getCurrentView() {
        final var context = FacesContext.getCurrentInstance();
        if (null == context) {
            return ViewDescriptorImpl.builder().build();
        }
        return NavigationUtils.getCurrentView(context);
    }
}
