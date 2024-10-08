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
package de.cuioss.portal.ui.runtime.page;

import de.cuioss.jsf.api.application.navigation.ViewIdentifier;
import de.cuioss.portal.authentication.AuthenticatedUserInfo;
import de.cuioss.portal.authentication.LoginEvent;
import de.cuioss.portal.authentication.PortalLoginEvent;
import de.cuioss.portal.authentication.UserChangeEvent;
import de.cuioss.portal.authentication.facade.AuthenticationResults;
import de.cuioss.portal.ui.api.history.HistoryManager;
import de.cuioss.portal.ui.api.pages.HomePage;
import de.cuioss.portal.ui.api.pages.LoginPage;
import de.cuioss.tools.logging.CuiLogger;
import de.cuioss.uimodel.nameprovider.IDisplayNameProvider;
import de.cuioss.uimodel.nameprovider.LabeledKey;
import de.cuioss.uimodel.result.ResultObject;
import jakarta.enterprise.event.Event;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.io.Serial;
import java.io.Serializable;
import java.util.function.Supplier;

import static de.cuioss.jsf.api.application.navigation.NavigationUtils.getCurrentView;
import static de.cuioss.portal.configuration.PortalConfigurationKeys.PORTAL_SESSION_TIMEOUT;

/**
 * Abstract helper class for {@link LoginPage} supporting deep linking.
 *
 * @author Matthias Walliczek
 */
public abstract class AbstractLoginPageBean implements Serializable {

    private static final CuiLogger LOGGER = new CuiLogger(AbstractLoginPageBean.class);

    @Serial
    private static final long serialVersionUID = 4152088404239660075L;

    private static final LabeledKey UNABLE_TO_LOGIN_MSG = new LabeledKey(AuthenticationResults.KEY_UNABLE_TO_LOGIN);

    @Inject
    @UserChangeEvent
    private Event<AuthenticatedUserInfo> userChangeEvent;

    @Inject
    @ConfigProperty(name = PORTAL_SESSION_TIMEOUT)
    private Integer sessionTimeout;

    @Inject
    @PortalLoginEvent
    private Event<LoginEvent> loginEvent;

    /**
     * Trigger the login action, e.g., after clicking at the "login" button.
     *
     * @param targetSupplier the target to redirect to after successful login, e.g.
     *                       {@link HistoryManager#getCurrentView}. To handle
     *                       conditional navigation, the supplier is executed after
     *                       successful login and fire of the userChangeEvent.
     * @param servletRequest the current servlet request
     * @param facesContext
     * @return
     */
    protected String loginAction(final Supplier<ViewIdentifier> targetSupplier, final HttpServletRequest servletRequest,
                                 final FacesContext facesContext) {
        final var loginResult = doLogin(servletRequest);

        if (!loginResult.isValid()) {
            loginResult.getResultDetail().ifPresent(resultDetail -> handleLoginFailed(resultDetail.getDetail()));
            if (null != loginResult.getResult().getIdentifier()) {
                loginEvent.fire(LoginEvent.builder().action(LoginEvent.Action.LOGIN_FAILED)
                        .username(loginResult.getResult().getIdentifier()).build());
            }
            return null;
        }
        final var newUserInfo = loginResult.getResult();
        if (newUserInfo.isAuthenticated()) {
            LOGGER.debug("user is authenticated");
            userChangeEvent.fire(newUserInfo);
            loginEvent.fire(LoginEvent.builder().action(LoginEvent.Action.LOGIN_SUCCESS).build());
            servletRequest.getSession().setMaxInactiveInterval(sessionTimeout * 60);

            // viewOutcome is never null because home is always the first entry on
            // initialization
            final var target = targetSupplier.get();
            if (!target.getViewId().equalsIgnoreCase(getCurrentView(facesContext).getLogicalViewId())) {
                LOGGER.debug("redirecting to: {}", target);
                target.redirect(facesContext);
                return null;
            }

            LOGGER.debug("redirecting to HomePage");
            return HomePage.OUTCOME;
        }

        LOGGER.debug("user is NOT authenticated");
        handleLoginFailed(UNABLE_TO_LOGIN_MSG);
        if (null != loginResult.getResult().getIdentifier()) {
            loginEvent.fire(LoginEvent.builder().action(LoginEvent.Action.LOGIN_FAILED)
                    .username(newUserInfo.getIdentifier()).build());
        }
        return null;
    }

    /**
     * Trigger the login request to the authentication backend.
     *
     * @param servletRequest the current {@link HttpServletRequest}
     * @return an {@link AuthenticatedUserInfo} that may either be authenticated or
     * unauthenticated. Must never be null! It should always provide the
     * username as identifier, even at failed login attempts!
     */
    protected abstract ResultObject<AuthenticatedUserInfo> doLogin(HttpServletRequest servletRequest);

    /**
     * Handle failed login, e.g. display an error message.
     */
    protected abstract void handleLoginFailed(IDisplayNameProvider<?> errorMessage);

}
