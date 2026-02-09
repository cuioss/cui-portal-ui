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
import de.cuioss.jsf.api.servlet.ServletAdapterUtil;
import de.cuioss.portal.authentication.AuthenticatedUserInfo;
import de.cuioss.portal.authentication.facade.AuthenticationResults;
import de.cuioss.portal.authentication.facade.LoginResult;
import de.cuioss.portal.ui.test.configuration.PortalNavigationConfiguration;
import de.cuioss.uimodel.nameprovider.IDisplayNameProvider;
import de.cuioss.uimodel.nameprovider.LabeledKey;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

@RequestScoped
public class TestLoginPage extends AbstractLoginPageBean {

    @Serial
    private static final long serialVersionUID = -3147741576450873465L;

    @Getter
    @Setter
    private AuthenticatedUserInfo userInfo;

    @Inject
    private FacesContext facesContext;

    @Getter
    @Setter
    private boolean simulateLoginError;

    @Override
    protected LoginResult doLogin(final HttpServletRequest servletRequest) {
        if (simulateLoginError) {
            return AuthenticationResults.invalidResultKey("OOPS ... something went wrong ... ",
                    userInfo != null ? userInfo.getIdentifier() : null, null);
        }
        return AuthenticationResults.validResult(userInfo);
    }

    @Override
    protected void handleLoginFailed(final IDisplayNameProvider<?> errorMessage) {
    }

    String doLogin() {
        return loginAction(
                () -> ViewIdentifier.getFromViewDesciptor(PortalNavigationConfiguration.DESCRIPTOR_HOME, null),
                ServletAdapterUtil.getRequest(facesContext), facesContext);
    }

}
