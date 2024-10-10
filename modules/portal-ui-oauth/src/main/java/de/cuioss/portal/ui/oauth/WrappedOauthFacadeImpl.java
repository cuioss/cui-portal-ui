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

import de.cuioss.jsf.api.application.navigation.ViewIdentifier;
import de.cuioss.jsf.api.common.view.ViewDescriptor;
import de.cuioss.portal.authentication.facade.PortalAuthenticationFacade;
import de.cuioss.portal.authentication.oauth.Oauth2AuthenticationFacade;
import de.cuioss.portal.authentication.oauth.Oauth2Configuration;
import de.cuioss.portal.ui.api.context.CuiCurrentView;
import de.cuioss.portal.ui.api.history.HistoryManager;
import de.cuioss.tools.logging.CuiLogger;
import de.cuioss.tools.string.Joiner;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Provider;
import jakarta.servlet.http.HttpServletRequest;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementation of {@link WrappedOauthFacade} using {@link HistoryManager} and
 * {@link HttpServletRequest} to handle redirects to / from oauth server and
 * storage of view parameters and requested views.
 *
 * @author Matthias Walliczek
 */
@ApplicationScoped
@PortalWrappedOauthFacade
public class WrappedOauthFacadeImpl implements WrappedOauthFacade {

    static final String MESSAGES_IDENTIFIER = "oauthMessages";
    private static final CuiLogger log = new CuiLogger(WrappedOauthFacadeImpl.class);
    /**
     * Attribute name for the target view id to be stored in the session.
     */
    private static final String VIEW_IDENTIFIER = "oauthViewIdentifier";
    private static final String PARAMETER_IDENTIFIER = "oauthViewparameter";
    private static final String MESSAGE_GET_ATTRIBUTE_FAILED = "session.getAttribute failed";

    @Inject
    private Provider<HttpServletRequest> servletRequestProvider;

    @Inject
    private Provider<FacesContext> facesContextProvider;

    @Inject
    @CuiCurrentView
    private Provider<ViewDescriptor> currentViewProvider;

    @Inject
    @PortalAuthenticationFacade
    private Oauth2AuthenticationFacade authenticationFacade;

    @Inject
    private Provider<Oauth2Configuration> oauth2ConfigurationProvider;

    @Inject
    private Provider<HistoryManager> historyManagerProvider;

    @Override
    public String retrieveToken() {
        return retrieveToken(oauth2ConfigurationProvider.get().getInitialScopes());
    }

    @Override
    public String retrieveToken(final String scopes) {
        log.trace("retrieveToken for scopes: {}", scopes);
        var currentView = currentViewProvider.get();
        var request = servletRequestProvider.get();
        var token = authenticationFacade.retrieveToken(scopes);
        if (null == token) {
            historyManagerProvider.get().addCurrentUriToHistory(currentView);
            // store the current view in the session to handle problems with window scope
            // after
            // redirect from oauth server
            preserveCurrentView(request);
        }
        return token;
    }

    @Override
    public void handleMissingScopesException(MissingScopesException e, Map<String, Serializable> parameters) {
        log.trace("handleMissingScopesException", e);
        handleMissingScopesException(e, oauth2ConfigurationProvider.get().getInitialScopes(), parameters);
    }

    @Override
    public void handleMissingScopesException(MissingScopesException e, String initialScopes,
                                             Map<String, Serializable> parameters) {
        log.trace(e, "handleMissingScopesException {}", initialScopes);
        var request = servletRequestProvider.get();
        request.getSession().setAttribute(PARAMETER_IDENTIFIER, new HashMap<>(parameters));
        retrieveToken(Joiner.on(' ').join(initialScopes, e.getMissingScopes()));
    }

    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Serializable> retrieveViewParameters() {
        log.trace("retrieveViewParameters");
        Map<String, Serializable> result = new HashMap<>();
        var servletRequest = servletRequestProvider.get();
        try {
            if (null != servletRequest.getSession(false)
                    && null != servletRequest.getSession().getAttribute(PARAMETER_IDENTIFIER)) {
                result = (Map<String, Serializable>) servletRequest.getSession().getAttribute(PARAMETER_IDENTIFIER);
                servletRequest.getSession().removeAttribute(PARAMETER_IDENTIFIER);
                if (null != servletRequest.getSession().getAttribute(MESSAGES_IDENTIFIER)) {
                    var messages = (List<FacesMessage>) servletRequest.getSession().getAttribute(MESSAGES_IDENTIFIER);
                    log.trace("restore messages: {}", messages);
                    messages.forEach(message -> facesContextProvider.get().addMessage(null, message));
                    servletRequest.getSession().removeAttribute(MESSAGES_IDENTIFIER);
                }
            }
        } catch (IllegalStateException e) {
            log.debug(MESSAGE_GET_ATTRIBUTE_FAILED, e);
        }
        return result;
    }

    @Override
    public ViewIdentifier retrieveTargetView() {
        var targetView = historyManagerProvider.get().getCurrentView();
        log.trace("retrieveTargetView from historyManager.getCurrentView(): {}", targetView);
        var servletRequest = servletRequestProvider.get();
        try {
            if (null != servletRequest.getSession(false)
                    && null != servletRequest.getSession().getAttribute(VIEW_IDENTIFIER)) {
                targetView = (ViewIdentifier) servletRequest.getSession().getAttribute(VIEW_IDENTIFIER);
                log.trace("retrieveTargetView servletRequest.getSession().getAttribute(VIEW_IDENTIFIER): {}",
                        targetView);
                servletRequest.getSession().setAttribute(VIEW_IDENTIFIER, null);
                log.trace("retrieveTargetView servletRequest VIEW_IDENTIFIER reset");
            }
        } catch (IllegalStateException e) {
            log.debug(MESSAGE_GET_ATTRIBUTE_FAILED, e);
        }
        return targetView;
    }

    @Override
    public void preserveCurrentView() {
        log.trace("preserveCurrentView");
        var servletRequest = servletRequestProvider.get();
        try {
            if (null != servletRequest.getSession(false)
                    && null == servletRequest.getSession(false).getAttribute(VIEW_IDENTIFIER)) {
                preserveCurrentView(servletRequest);
            }
        } catch (IllegalStateException e) {
            log.debug(MESSAGE_GET_ATTRIBUTE_FAILED, e);
        }
    }

    private void preserveCurrentView(HttpServletRequest servletRequest) {
        log.debug("preserveCurrentView Preserving target {}", historyManagerProvider.get().getCurrentView());
        servletRequest.getSession().setAttribute(VIEW_IDENTIFIER, historyManagerProvider.get().getCurrentView());
        var messages = facesContextProvider.get().getMessageList(null);
        log.trace("preserveCurrentView store messages: {}", messages);
        servletRequest.getSession().setAttribute(MESSAGES_IDENTIFIER, messages);
    }
}
