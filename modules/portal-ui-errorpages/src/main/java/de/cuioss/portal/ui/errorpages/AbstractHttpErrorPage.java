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
package de.cuioss.portal.ui.errorpages;

import de.cuioss.jsf.api.servlet.ServletAdapterUtil;
import de.cuioss.tools.logging.CuiLogger;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Provider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;

import static de.cuioss.tools.string.MoreStrings.isEmpty;

/**
 * @author Oliver Wolff
 */
@EqualsAndHashCode
@ToString(exclude = {"facesContextProvider"})
public abstract class AbstractHttpErrorPage implements Serializable {

    static final String JAKARTA_SERVLET_ERROR_REQUEST_URI = "jakarta.servlet.error.request_uri";
    private static final CuiLogger log = new CuiLogger(AbstractHttpErrorPage.class);
    private static final String UNKNOWN = "?";
    @Serial
    private static final long serialVersionUID = -6617663225820801072L;

    @Inject
    @Getter(AccessLevel.PROTECTED)
    private Provider<FacesContext> facesContextProvider;

    @Getter
    private String requestUri;

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
        ServletAdapterUtil.getResponse(context).setStatus(getErrorCode());
        log.warn("Portal-137: Http-Error '{}' for requested-uri '{}' was raised", getErrorCode(), requestUri);
        return null;
    }

    /**
     * @return boolean indicating whether the requestUri could be determined.
     */
    public boolean isRequestUriAvailable() {
        return !UNKNOWN.equals(requestUri);
    }

    private String determineRequestUri(HttpServletRequest request) {
        if (null == request) {
            return UNKNOWN;
        }
        var attribute = request.getAttribute(JAKARTA_SERVLET_ERROR_REQUEST_URI);
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
     * backing bean
     */
    protected abstract int getErrorCode();
}
