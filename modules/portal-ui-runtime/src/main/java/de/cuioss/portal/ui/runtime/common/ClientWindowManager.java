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
package de.cuioss.portal.ui.runtime.common;

import static de.cuioss.portal.configuration.PortalConfigurationKeys.PORTAL_SESSION_MAX_INACTIVE_INTERVAL;
import static de.cuioss.portal.configuration.PortalConfigurationKeys.PORTAL_SESSION_TIMEOUT;

import java.io.Serializable;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.inject.Provider;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import de.cuioss.portal.ui.runtime.application.view.ViewTransientManagerBean;
import de.cuioss.tools.logging.CuiLogger;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Session bean to control over timeout.
 *
 * @author Oliver Wolff
 */
@Named(ClientWindowManager.BEAN_NAME)
@SessionScoped
@EqualsAndHashCode(of = "maxInactiveInterval", doNotUseGetters = true)
@ToString(of = "maxInactiveInterval", doNotUseGetters = true)
public class ClientWindowManager implements Serializable {

    private static final CuiLogger LOGGER = new CuiLogger(ClientWindowManager.class);

    /**
     * Bean name for looking up instances using EL.
     */
    public static final String BEAN_NAME = "clientWindowManager";

    private static final long serialVersionUID = 8603571267932838043L;

    @Inject
    @ConfigProperty(name = PORTAL_SESSION_MAX_INACTIVE_INTERVAL)
    Integer maxInactiveInterval;

    @Inject
    @ConfigProperty(name = PORTAL_SESSION_TIMEOUT)
    Integer sessionTimeout;

    @Inject
    Provider<ViewTransientManagerBean> viewTransientManagerProvider;

    /**
     * @return boolean indicating whether to render the timeout-form
     */
    public boolean isRenderTimeoutForm() {
        return !viewTransientManagerProvider.get().isTransientView() && 0 != getMaxInactiveInterval();
    }

    /**
     * @return the interval to be used for determining after which time the
     *         corresponding window should show the timeout-popup
     */
    public int getMaxInactiveInterval() {
        if (maxInactiveInterval > -1) {
            LOGGER.debug("Returning maxInactiveInterval='%s'", maxInactiveInterval);
            return maxInactiveInterval;
        }
        LOGGER.debug("Returning sessionTimeout='%s'", sessionTimeout);
        return sessionTimeout;
    }
}
