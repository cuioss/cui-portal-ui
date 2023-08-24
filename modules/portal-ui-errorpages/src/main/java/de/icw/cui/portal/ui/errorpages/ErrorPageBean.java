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
package de.icw.cui.portal.ui.errorpages;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.annotation.Priority;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletResponse;

import de.cuioss.jsf.api.servlet.ServletAdapterUtil;
import de.cuioss.portal.configuration.common.PortalPriorities;
import de.cuioss.portal.core.storage.MapStorage;
import de.cuioss.portal.core.storage.PortalSessionStorage;
import de.cuioss.portal.ui.api.exception.DefaultErrorMessage;
import de.cuioss.portal.ui.api.ui.pages.ErrorPage;
import de.cuioss.portal.ui.api.ui.pages.PortalCorePagesError;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Portal base implementation of {@link ErrorPage} <em>Note:</em> It is assumed
 * that the {@link DefaultErrorMessage} can be derived from the
 * {@link PortalSessionStorage} with the key
 * {@link DefaultErrorMessage#LOOKUP_KEY}. While retrieving the
 * {@link DefaultErrorMessage} it will implicitly be removed.
 *
 * @author Oliver Wolff
 */
@PortalCorePagesError
@RequestScoped
@Priority(PortalPriorities.PORTAL_CORE_LEVEL)
@Named(ErrorPage.BEAN_NAME)
@EqualsAndHashCode(of = "message", doNotUseGetters = true)
@ToString(of = "message", doNotUseGetters = true)
public class ErrorPageBean implements ErrorPage {

    private static final long serialVersionUID = -3785494532638995890L;

    @Inject
    @PortalSessionStorage
    private MapStorage<Serializable, Serializable> mapStorage;

    @Inject
    private FacesContext context;

    @Getter
    private DefaultErrorMessage message;

    /**
     * Retrieve and removes the {@link DefaultErrorMessage} found under the key
     * {@link DefaultErrorMessage#LOOKUP_KEY}
     */
    @PostConstruct
    public void init() {
        if (mapStorage.containsKey(DefaultErrorMessage.LOOKUP_KEY)) {
            message = (DefaultErrorMessage) mapStorage.remove(DefaultErrorMessage.LOOKUP_KEY);
        }
        ServletAdapterUtil.getResponse(context).setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }

    @Override
    public boolean isMessageAvailable() {
        return null != message;
    }

}
