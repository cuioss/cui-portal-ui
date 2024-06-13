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

import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.faces.context.FacesContext;

import de.cuioss.jsf.api.application.navigation.NavigationUtils;

import jakarta.annotation.PostConstruct;
import de.cuioss.portal.authentication.oauth.LoginPagePath;
import de.cuioss.portal.ui.api.pages.LoginPage;
import de.cuioss.tools.logging.CuiLogger;

@RequestScoped
public class LoginPagePathProducer {

    private static final CuiLogger log = new CuiLogger(LoginPagePathProducer.class);

    /** Default login page based on CUI-portal-oauth faces-config.xml */
    private static final String DEFAULT = "/guest/login.xhtml";

    @Produces
    @LoginPagePath
    private String loginUrl = DEFAULT;

    /**
     * Initialize the bean.
     */
    @PostConstruct
    public void init() {
        final var context = FacesContext.getCurrentInstance();
        if (null != context) {
            loginUrl = NavigationUtils.lookUpToLogicalViewIdBy(context, LoginPage.OUTCOME);
        } else {
            log.debug("No FacesContext available. Using URI: {}", loginUrl);
        }
    }
}
