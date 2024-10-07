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

import de.cuioss.portal.configuration.PortalConfigurationKeys;
import de.cuioss.tools.base.BooleanOperations;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletResponse;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.io.Serial;

/**
 * Page Bean for the error-code 404 (Resource not found)
 *
 * @author Oliver Wolff
 *
 */
@RequestScoped
@Named
@EqualsAndHashCode(callSuper = true)
public class Http404PageBean extends AbstractHttpErrorPage {

    @Serial
    private static final long serialVersionUID = -2216275532091092216L;

    @Inject
    @ConfigProperty(name = PortalConfigurationKeys.PAGES_ERROR_404_REDIRECT)
    @Getter
    private boolean shouldRedirect;

    @Override
    public String initView() {
        super.initView();
        if (BooleanOperations.isAnyTrue(!shouldRedirect, !isRequestUriAvailable())) {
            shouldRedirect = false;
        }
        return null;
    }

    @Override
    protected int getErrorCode() {
        return HttpServletResponse.SC_NOT_FOUND;
    }

}
