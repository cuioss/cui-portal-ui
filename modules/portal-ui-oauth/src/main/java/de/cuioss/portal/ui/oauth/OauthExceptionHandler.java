package de.cuioss.portal.ui.oauth;

import java.io.Serializable;
import java.util.ResourceBundle;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import de.cuioss.jsf.api.application.message.MessageProducer;
import de.cuioss.jsf.api.common.view.ViewDescriptor;
import de.cuioss.portal.core.bundle.PortalResourceBundle;
import de.cuioss.portal.core.storage.MapStorage;
import de.cuioss.portal.core.storage.PortalSessionStorage;
import de.cuioss.portal.ui.api.exception.DefaultErrorMessage;
import de.cuioss.portal.ui.api.exception.ExceptionAsEvent;
import de.cuioss.portal.ui.api.exception.HandleOutcome;
import de.cuioss.portal.ui.api.exception.PortalExceptionHandler;
import de.cuioss.portal.ui.api.message.PortalMessageProducer;
import de.cuioss.portal.ui.api.ui.context.CuiCurrentView;
import de.cuioss.portal.ui.api.ui.context.CuiNavigationHandler;

/**
 * @author Matthias Walliczek
 */
@RequestScoped
public class OauthExceptionHandler implements PortalExceptionHandler {

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

    protected DefaultErrorMessage createErrorMessage(final String messageKey) {
        return new DefaultErrorMessage("", "", this.resourceBundle.getString(messageKey), this.currentView.getViewId());
    }

    @Override
    public void handle(ExceptionAsEvent exceptionEvent) {
        DefaultErrorMessage.addErrorMessageToSessionStorage(
                createErrorMessage(exceptionEvent.getException().getMessage()), this.sessionStorage);
        this.navigationHandler.handleNavigation(this.facesContext, null, OAUTH_ERROR_OUTCOME);
        exceptionEvent.handled(HandleOutcome.REDIRECT);

    }

}
