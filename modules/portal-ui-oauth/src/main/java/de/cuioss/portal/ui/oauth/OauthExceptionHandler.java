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
package de.cuioss.portal.ui.oauth;

import java.io.Serializable;

import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.NavigationHandler;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;

import de.cuioss.jsf.api.application.message.MessageProducer;
import de.cuioss.jsf.api.common.view.ViewDescriptor;
import de.cuioss.portal.common.bundle.ResourceBundleWrapper;
import de.cuioss.portal.core.storage.MapStorage;
import de.cuioss.portal.core.storage.PortalSessionStorage;
import de.cuioss.portal.ui.api.exception.DefaultErrorMessage;
import de.cuioss.portal.ui.api.exception.ExceptionAsEvent;
import de.cuioss.portal.ui.api.exception.HandleOutcome;
import de.cuioss.portal.ui.api.exception.PortalExceptionHandler;
import de.cuioss.portal.ui.api.context.CuiCurrentView;
import de.cuioss.portal.ui.api.context.CuiNavigationHandler;

/**
 * @author Matthias Walliczek
 */
@RequestScoped
public class OauthExceptionHandler implements PortalExceptionHandler {

    private static final String OAUTH_ERROR_OUTCOME = "oauth-error";

    @Inject
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
    private ResourceBundleWrapper resourceBundle;

    protected DefaultErrorMessage createErrorMessage(final String messageKey) {
        return new DefaultErrorMessage("", "", resourceBundle.getString(messageKey), currentView.getViewId());
    }

    @Override
    public void handle(ExceptionAsEvent exceptionEvent) {
        DefaultErrorMessage.addErrorMessageToSessionStorage(
                createErrorMessage(exceptionEvent.getException().getMessage()), sessionStorage);
        navigationHandler.handleNavigation(facesContext, null, OAUTH_ERROR_OUTCOME);
        exceptionEvent.handled(HandleOutcome.REDIRECT);

    }

}
