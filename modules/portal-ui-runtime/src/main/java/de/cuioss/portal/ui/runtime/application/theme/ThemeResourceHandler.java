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
package de.cuioss.portal.ui.runtime.application.theme;

import jakarta.faces.application.Resource;
import jakarta.faces.application.ResourceHandler;
import jakarta.faces.application.ResourceHandlerWrapper;

import de.cuioss.portal.common.cdi.PortalBeanManager;
import de.cuioss.tools.logging.CuiLogger;

/**
 * Handles the resolving of the correct theme css. It filters for libraryName=
 * {@link PortalThemeConfiguration#CSS_LIBRARY} and
 * resourceName={@link PortalThemeConfiguration#CSS_NAME}
 *
 * @author Oliver Wolff
 */
public class ThemeResourceHandler extends ResourceHandlerWrapper {

    private static final CuiLogger LOGER = new CuiLogger(ThemeResourceHandler.class);

    /**
     * Constructor.
     *
     * @param wrapped handler
     */
    public ThemeResourceHandler(ResourceHandler wrapped) {
        super(wrapped);
    }

    @Override
    public Resource createResource(String resourceName, String libraryName) {

        if (isRequestForTheme(resourceName, libraryName)) {
            var themeName = PortalBeanManager.resolveRequiredBean(UserThemeBean.class).resolveFinalThemeCssName();
            LOGER.debug("Resolved cssname = %s", themeName);
            return getWrapped().createResource(themeName, libraryName);
        }
        return getWrapped().createResource(resourceName, libraryName);
    }

    private boolean isRequestForTheme(String resourceName, String libraryName) {
        return PortalThemeConfiguration.CSS_LIBRARY.equals(libraryName)
                && PortalThemeConfiguration.CSS_NAME.equals(resourceName);
    }
}
