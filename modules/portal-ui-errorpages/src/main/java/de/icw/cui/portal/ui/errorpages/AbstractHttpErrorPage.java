package de.icw.cui.portal.ui.errorpages;

import static de.cuioss.tools.string.MoreStrings.isEmpty;

import java.io.Serializable;

import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Provider;
import javax.servlet.http.HttpServletRequest;

import de.cuioss.jsf.api.application.navigation.NavigationUtils;
import de.cuioss.jsf.api.servlet.ServletAdapterUtil;
import de.cuioss.tools.logging.CuiLogger;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * @author Oliver Wolff
 *
 */
@EqualsAndHashCode
@ToString(exclude = { "facesContextProvider" })
public abstract class AbstractHttpErrorPage implements Serializable {

    private static final CuiLogger log = new CuiLogger(AbstractHttpErrorPage.class);

    private static final String UNKNOWN = "?";
    static final String JAVAX_SERVLET_ERROR_REQUEST_URI = "javax.servlet.error.request_uri";

    private static final long serialVersionUID = -6617663225820801072L;

    @Inject
    @Getter(AccessLevel.PROTECTED)
    private Provider<FacesContext> facesContextProvider;

    @Getter
    private String requestUri;

    /** boolean indicating whether the requested view is jsf view or not. */
    @Getter
    private boolean jsfView;

    /**
     * Initializes the view by determining the requestedUri and logging the
     * errorCode at warn-level
     *
     * @return always {@code null}
     */
    public String initView() {
        var context = facesContextProvider.get();
        var request = ServletAdapterUtil.getRequest(context);
        requestUri = determineRequestUri(request);
        jsfView = requestUri.startsWith(NavigationUtils.FACES_VIEW_PREFIX);
        ServletAdapterUtil.getResponse(context).setStatus(getErrorCode());
        log.warn("Portal-137: Http-Error '{}' for requested-uri '{}' was raised, jsfView='{}'", getErrorCode(),
                requestUri, jsfView);
        return null;
    }

    /**
     * @return boolean indicating whether the requestUri could be determined.
     */
    public boolean isRequestUriAvailable() {
        return !UNKNOWN.equals(requestUri);
    }

    /**
     * @param request
     *
     * @return
     */
    private String determineRequestUri(HttpServletRequest request) {
        if (null == request) {
            return UNKNOWN;
        }
        var attribute = request.getAttribute(JAVAX_SERVLET_ERROR_REQUEST_URI);
        if (null == attribute) {
            return UNKNOWN;
        }
        var aString = String.valueOf(attribute);
        if (isEmpty(aString)) {
            return UNKNOWN;
        }
        var context = request.getContextPath();
        if (aString.startsWith(context)) {
            return aString.substring(context.length());
        }
        return aString;
    }

    /**
     * @return the concrete error code. It is usually defined by the concrete view /
     *         backing bean
     */
    protected abstract int getErrorCode();
}
