/*
 * Copyright © 2025 CUI-OpenSource-Software (info@cuioss.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.cuioss.portal.ui.runtime.exception;

import de.cuioss.jsf.api.application.navigation.NavigationUtils;
import de.cuioss.jsf.api.common.util.CheckContextState;
import de.cuioss.jsf.api.common.view.ViewDescriptor;
import de.cuioss.portal.core.storage.MapStorage;
import de.cuioss.portal.core.storage.PortalSessionStorage;
import de.cuioss.portal.ui.api.context.CuiCurrentView;
import de.cuioss.portal.ui.api.context.CuiNavigationHandler;
import de.cuioss.portal.ui.api.exception.DefaultErrorMessage;
import de.cuioss.portal.ui.api.exception.ExceptionAsEvent;
import de.cuioss.portal.ui.api.exception.HandleOutcome;
import de.cuioss.portal.ui.api.pages.ErrorPage;
import de.cuioss.tools.logging.CuiLogger;
import de.cuioss.uimodel.application.CuiProjectStage;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.NavigationHandler;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Provider;

import java.io.Serial;
import java.io.Serializable;
import java.util.Optional;
import java.util.UUID;

import static de.cuioss.portal.ui.runtime.PortalUiRuntimeLogMessages.ERROR;
import static de.cuioss.portal.ui.runtime.PortalUiRuntimeLogMessages.WARN;

/**
 * Defines the last line of defense, saying displaying the error page with the
 * general message An exception occurred. "Should" not happen often. Every time
 * this happens, a bug should be filed accordingly. This handler is only active
 * if the {@link CuiProjectStage} is {@link CuiProjectStage#isProduction()}. on
 * {@link CuiProjectStage#isDevelopment()} the default jsf error page will be shown. In
 * case there is no view related to the exception, only logging will occur.
 *
 * @author Oliver Wolff
 */
@RequestScoped
public class FallBackExceptionHandler implements Serializable {

    private static final CuiLogger LOGGER = new CuiLogger(FallBackExceptionHandler.class);
    @Serial
    private static final long serialVersionUID = -1197300817644970750L;
    private static final String SYSTEM_ERROR = "System error";
    private Provider<MapStorage<Serializable, Serializable>> sessionStorageProvider;

    private NavigationHandler navigationHandler;

    private ViewDescriptor currentView;

    private CuiProjectStage projectStage;

    protected FallBackExceptionHandler() {
        // for CDI proxy
    }

    @Inject
    public FallBackExceptionHandler(
            @PortalSessionStorage Provider<MapStorage<Serializable, Serializable>> sessionStorageProvider,
            @CuiNavigationHandler NavigationHandler navigationHandler,
            @CuiCurrentView ViewDescriptor currentView, CuiProjectStage projectStage) {
        this.sessionStorageProvider = sessionStorageProvider;
        this.navigationHandler = navigationHandler;
        this.currentView = currentView;
        this.projectStage = projectStage;
    }

    /**
     * Actual handler, see class documentation for details.
     *
     * @param exceptionEvent to be handled
     * @throws IllegalStateException in cased of projectStage being Development
     */
    public void handleFallBack(final ExceptionAsEvent exceptionEvent) {
        if (exceptionEvent.isHandled()) {
            LOGGER.debug("Given event '%s' already handled, nothing to do here", exceptionEvent);
            return;
        }
        if (projectStage.isDevelopment()) {
            LOGGER.debug("Fallback exception handling omitted due to development stage.");
            exceptionEvent.handled(HandleOutcome.RE_THROWN);
            throw new IllegalStateException(exceptionEvent.getException());
        }

        final var throwable = exceptionEvent.getException();
        final var msg = null != throwable.getMessage() ? throwable.getMessage() : throwable.toString();
        final var facesContext = FacesContext.getCurrentInstance();
        if (CheckContextState.isResponseNotComplete(facesContext)
                && !facesContext.getExternalContext().isResponseCommitted()) {
            var errorTicket = UUID.randomUUID().toString();
            LOGGER.error(throwable, ERROR.PORTAL_111_UNSPECIFIED_EXCEPTION, currentView, errorTicket);
            exceptionEvent.handled(HandleOutcome.LOGGED);
            final var errorMessage = new DefaultErrorMessage(SYSTEM_ERROR, errorTicket,
                    throwable.getClass().getCanonicalName() + ": " + msg, "");
            var sessionStorage = checkSessionStorage(facesContext);
            // Put message in session scope to access it on error page.
            sessionStorage.ifPresent(serializableSerializableMapStorage -> DefaultErrorMessage
                    .addErrorMessageToSessionStorage(errorMessage, serializableSerializableMapStorage));

            if (redirectWasNotDoneFromErrorPage(facesContext)) {
                navigationHandler.handleNavigation(facesContext, null, ErrorPage.OUTCOME);
                exceptionEvent.handled(HandleOutcome.REDIRECT);
            } else {
                LOGGER.error(throwable, ERROR.PORTAL_130_ERROR_ON_ERROR_PAGE);
            }

        } else {
            LOGGER.error(throwable, ERROR.PORTAL_112_UNSPECIFIED_EXCEPTION_NO_VIEW);
            exceptionEvent.handled(HandleOutcome.LOGGED);
        }
    }

    private boolean redirectWasNotDoneFromErrorPage(final FacesContext facesContext) {
        final var errorPageDescriptor = NavigationUtils.lookUpToViewDescriptorBy(facesContext, ErrorPage.OUTCOME);

        return !errorPageDescriptor.getViewId().equals(currentView.getViewId());
    }

    private Optional<MapStorage<Serializable, Serializable>> checkSessionStorage(final FacesContext facesContext) {
        try {
            var sessionStorage = sessionStorageProvider.get();
            sessionStorage.containsKey("testKey");
            return Optional.of(sessionStorage);
            // cui-rewrite:disable InvalidExceptionUsageRecipe
        } catch (RuntimeException e) {
            LOGGER.warn(WARN.EXCEPTION_HANDLING_INVALID_SESSION, e.getMessage());
            facesContext.getExternalContext().getSession(true);
            MapStorage<Serializable, Serializable> sessionStorage;
            try {
                sessionStorage = sessionStorageProvider.get();
                sessionStorage.containsKey("testKey");
                return Optional.of(sessionStorage);
                // cui-rewrite:disable InvalidExceptionUsageRecipe
            } catch (RuntimeException e1) {
                LOGGER.error(e1, ERROR.PORTAL_502_SESSION_RECREATE_FAILED, e1.getMessage());
            }
            return Optional.empty();
        }
    }

}
