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
package de.cuioss.portal.ui.errorpages;

import de.cuioss.portal.core.storage.MapStorage;
import de.cuioss.portal.core.storage.PortalSessionStorage;
import de.cuioss.portal.ui.api.exception.DefaultErrorMessage;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.inject.Provider;
import jakarta.servlet.http.HttpServletResponse;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;

/**
 * Page Bean for the error-code 401 (the request requires HTTP authentication)
 *
 * @author Oliver Wolff
 */
@RequestScoped
@Named
@EqualsAndHashCode(callSuper = true)
public class Http401PageBean extends AbstractHttpErrorPage {

    @Serial
    private static final long serialVersionUID = -2216275532091092216L;

    private final MapStorage<Serializable, Serializable> mapStorage;

    /**
     * CDI proxy constructor.
     */
    protected Http401PageBean() {
        this.mapStorage = null;
    }

    @Inject
    public Http401PageBean(Provider<FacesContext> facesContextProvider,
            @PortalSessionStorage MapStorage<Serializable, Serializable> mapStorage) {
        super(facesContextProvider);
        this.mapStorage = mapStorage;
    }

    @Getter
    private DefaultErrorMessage message;

    /**
     * Initializes the view by determining the requestedUri and logging the
     * errorCode at warn-level
     *
     * @return always {@code null}
     */
    @Override
    public String initView() {
        super.initView();
        if (mapStorage.containsKey(DefaultErrorMessage.LOOKUP_KEY)) {
            message = (DefaultErrorMessage) mapStorage.remove(DefaultErrorMessage.LOOKUP_KEY);
        }
        return null;
    }

    @Override
    protected int getErrorCode() {
        return HttpServletResponse.SC_UNAUTHORIZED;
    }

    /**
     * @return flag indicating whether a message is available / to be displayed
     */
    public boolean isMessageAvailable() {
        return null != message;
    }
}
