package de.cuioss.portal.ui.oauth;

import java.io.Serializable;
import java.util.ResourceBundle;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.deltaspike.core.api.exception.control.ExceptionHandler;
import org.apache.deltaspike.core.api.exception.control.Handles;
import org.apache.deltaspike.core.api.exception.control.event.ExceptionEvent;

import de.cuioss.jsf.api.application.message.MessageProducer;
import de.cuioss.jsf.api.common.view.ViewDescriptor;
import de.cuioss.portal.authentication.oauth.OauthAuthenticationException;
import de.cuioss.portal.core.bundle.PortalResourceBundle;
import de.cuioss.portal.core.storage.MapStorage;
import de.cuioss.portal.core.storage.PortalSessionStorage;
import de.cuioss.portal.ui.api.exception.DefaultErrorMessage;
import de.cuioss.portal.ui.api.message.PortalMessageProducer;
import de.cuioss.portal.ui.api.ui.context.CuiCurrentView;
import de.cuioss.portal.ui.api.ui.context.CuiNavigationHandler;

/**
 * @author Matthias Walliczek
 */
@ExceptionHandler
@Named
@RequestScoped
public class OauthExceptionHandler implements Serializable {

    private static final long serialVersionUID = 4117684215250330155L;

    private static final String OAUTH_ERROR_OUTCOME = "oauth-error";

    @Inject
    @PortalMessageProducer
    private MessageProducer messageProducer;

    @Inject
    @CuiCurrentView
    private ViewDescriptor currentView;

    @Inject
    private FacesContext facesContext;

    @Inject
    @CuiNavigationHandler
    private NavigationHandler navigationHandler;

    @Inject
    @PortalSessionStorage
    private MapStorage<Serializable, Serializable> sessionStorage;

    @Inject
    @PortalResourceBundle
    private ResourceBundle resourceBundle;

    void handleOauthAuthenticationException(
            @Handles(ordinal = 2) final ExceptionEvent<OauthAuthenticationException> event) {
        DefaultErrorMessage.addErrorMessageToSessionStorage(createErrorMessage(event.getException().getMessage()),
                this.sessionStorage);
        this.navigationHandler.handleNavigation(this.facesContext, null, OAUTH_ERROR_OUTCOME);
        event.handled();
    }

    protected DefaultErrorMessage createErrorMessage(final String messageKey) {
        return new DefaultErrorMessage("", "", this.resourceBundle.getString(messageKey), this.currentView.getViewId());
    }

}
