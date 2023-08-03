package de.cuioss.portal.ui.runtime.exception;

import java.io.Serializable;
import java.util.Optional;
import java.util.UUID;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;

import org.apache.deltaspike.core.api.exception.control.ExceptionHandler;
import org.apache.deltaspike.core.api.exception.control.Handles;
import org.apache.deltaspike.core.api.exception.control.event.ExceptionEvent;

import de.cuioss.jsf.api.application.navigation.NavigationUtils;
import de.cuioss.jsf.api.common.util.CheckContextState;
import de.cuioss.jsf.api.common.view.ViewDescriptor;
import de.cuioss.portal.configuration.application.PortalProjectStageProducer;
import de.cuioss.portal.core.storage.MapStorage;
import de.cuioss.portal.core.storage.PortalSessionStorage;
import de.cuioss.portal.ui.api.exception.DefaultErrorMessage;
import de.cuioss.portal.ui.api.ui.context.CuiCurrentView;
import de.cuioss.portal.ui.api.ui.context.CuiNavigationHandler;
import de.cuioss.portal.ui.api.ui.pages.ErrorPage;
import de.cuioss.tools.logging.CuiLogger;
import de.cuioss.uimodel.application.CuiProjectStage;

/**
 * Defines the last line of defense, saying displaying the error page with the
 * general message An exception occurred. "Should" not happen often. Every time
 * this happens, a bug should be filed accordingly. This handler is only active
 * if the {@link ProjectStage} is {@link ProjectStage#Production}. on
 * {@link ProjectStage#Development} the default jsf error page will be shown. In
 * case there is no view related to the exception, only logging will occur.
 *
 * @author Oliver Wolff
 */
@ExceptionHandler
@Named
@RequestScoped
public class FallBackExceptionHandler implements Serializable {

    private static final CuiLogger log = new CuiLogger(FallBackExceptionHandler.class);

    private static final long serialVersionUID = -1197300817644970750L;

    private static final String SYSTEM_ERROR = "System error";

    private static final String UNSPECIFIED_EXCEPTION = "Portal-111: An unspecified exception has been caught and handled by fallback strategy while trying to access view ";

    private static final String UNSPECIFIED_EXCEPTION_SUFFIX = ", errorTicket=";

    private static final String UNSPECIFIED_EXCEPTION_WITHOUT_VIEW = "Portal-112: An unspecified exception has been caught and handled by fallback strategy";

    private static final String EXCEPTION_HANDLING_FAILED_DUE_TO_INVALID_SESSION = "Portal-113: Detected an invalidated session, trying to recreate, reason={}";

    private static final String EXCEPTION_HANDLING_FAILED_DUE_TO_INVALID_NEW_SESSION = "Portal-502: Unable to recreate session, reason={}";

    private static final String PORTAL_130_ERROR_ON_ERROR_PAGE = "Portal-130: Previous error occurs on error page. This will lead to damaged output and is a sign of corrupted deployment";

    @Inject
    @PortalSessionStorage
    private Provider<MapStorage<Serializable, Serializable>> sessionStorageProvider;

    @Inject
    @CuiNavigationHandler
    private NavigationHandler navigationHandler;

    @Inject
    @CuiCurrentView
    private ViewDescriptor currentView;

    @Inject
    @PortalProjectStageProducer
    private CuiProjectStage projectStage;

    /**
     * Actual handler, see class documentation for details.
     *
     * @param evt to be handled
     */
    void handleFallBack(@Handles final ExceptionEvent<Throwable> evt) {
        if (projectStage.isDevelopment()) {
            log.debug("Fallback exception handling omitted due to development stage.");
            evt.throwOriginal();
            return;
        }

        final var throwable = evt.getException();
        final var msg = null != throwable.getMessage() ? throwable.getMessage() : throwable.toString();
        final var facesContext = FacesContext.getCurrentInstance();
        if (CheckContextState.isResponseNotComplete(facesContext)
                && !facesContext.getExternalContext().isResponseCommitted()) {
            var errorTicket = UUID.randomUUID().toString();
            log.error(UNSPECIFIED_EXCEPTION + currentView + UNSPECIFIED_EXCEPTION_SUFFIX + errorTicket, throwable);
            final var errorMessage = new DefaultErrorMessage(SYSTEM_ERROR, errorTicket,
                    throwable.getClass().getCanonicalName() + ": " + msg, "");
            var sessionStorage = checkSessionStorage(facesContext);
            // Put message in session scope to access it on error page.
            sessionStorage.ifPresent(serializableSerializableMapStorage -> DefaultErrorMessage
                    .addErrorMessageToSessionStorage(errorMessage, serializableSerializableMapStorage));

            if (redirectWasNotDoneFromErrorPage(facesContext)) {
                navigationHandler.handleNavigation(facesContext, null, ErrorPage.OUTCOME);
            } else {
                log.error(PORTAL_130_ERROR_ON_ERROR_PAGE);
            }

        } else {
            log.error(UNSPECIFIED_EXCEPTION_WITHOUT_VIEW, throwable);
        }
        evt.handled();
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
        } catch (RuntimeException e) {
            log.warn(EXCEPTION_HANDLING_FAILED_DUE_TO_INVALID_SESSION, e.getMessage());
            facesContext.getExternalContext().getSession(true);
            MapStorage<Serializable, Serializable> sessionStorage;
            try {
                sessionStorage = sessionStorageProvider.get();
                sessionStorage.containsKey("testKey");
                return Optional.of(sessionStorage);
            } catch (RuntimeException e1) {
                log.error(EXCEPTION_HANDLING_FAILED_DUE_TO_INVALID_NEW_SESSION, e1.getMessage());
            }
            return Optional.empty();
        }
    }

}
