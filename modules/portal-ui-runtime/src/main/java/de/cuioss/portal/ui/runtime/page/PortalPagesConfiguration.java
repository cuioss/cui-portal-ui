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
package de.cuioss.portal.ui.runtime.page;

import de.cuioss.portal.ui.api.pages.LoginPageStrategy;
import de.cuioss.tools.logging.CuiLogger;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.inject.Provider;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.io.Serial;
import java.io.Serializable;

import static de.cuioss.portal.configuration.PortalConfigurationKeys.PAGES_LOGIN_ENTER_STRATEGY;

/**
 * Provides configurable behavior of pages, e.g. {@link LoginPageStrategy}
 *
 * @author Oliver Wolff
 */
@ApplicationScoped
@Named
@EqualsAndHashCode(of = "loginPageStrategy")
@ToString(of = "loginPageStrategy")
public class PortalPagesConfiguration implements Serializable {

    @Serial
    private static final long serialVersionUID = 6752537251543880784L;

    private static final CuiLogger LOGGER = new CuiLogger(PortalPagesConfiguration.class);

    private Provider<String> loginPageStrategyProvider;

    protected PortalPagesConfiguration() {
        // for CDI proxy
    }

    @Inject
    public PortalPagesConfiguration(
            @ConfigProperty(name = PAGES_LOGIN_ENTER_STRATEGY) Provider<String> loginPageStrategyProvider) {
        this.loginPageStrategyProvider = loginPageStrategyProvider;
    }

    @Getter
    private LoginPageStrategy loginPageStrategy;

    /**
     * Initializes the bean, see class documentation for details
     */
    @PostConstruct
    public void initBean() {
        loginPageStrategy = LoginPageStrategy.getFromString(loginPageStrategyProvider.get());
    }


}
