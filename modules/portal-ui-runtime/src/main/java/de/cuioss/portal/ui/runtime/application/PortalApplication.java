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
package de.cuioss.portal.ui.runtime.application;

import de.cuioss.portal.common.bundle.PortalResourceBundleBean;
import de.cuioss.portal.common.cdi.PortalBeanManager;
import de.cuioss.portal.ui.runtime.application.configuration.LocaleConfiguration;
import de.cuioss.tools.logging.CuiLogger;
import de.cuioss.uimodel.application.CuiProjectStage;
import jakarta.faces.application.Application;
import jakarta.faces.application.ApplicationWrapper;
import jakarta.faces.application.ProjectStage;
import jakarta.faces.context.FacesContext;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Iterator;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Decorate the standard {@link Application} in order to bridge the portal
 * specific implementations with the JSF-specific ones. Currently, these are:
 * <ul>
 * <li>{@link Application#getProjectStage()}</li>
 * <li>{@link Application#getDefaultLocale()}</li>
 * <li>{@link Application#getSupportedLocales()}</li>
 * </ul>
 *
 * @author Oliver Wolff
 * @author Sven Haag
 */
@RequiredArgsConstructor
public class PortalApplication extends ApplicationWrapper {

    private static final CuiLogger LOGGER = new CuiLogger(PortalApplication.class);

    @Getter
    private final Application wrapped;

    @Getter(lazy = true)
    private final CuiProjectStage portalProjectStage = PortalBeanManager.resolveRequiredBean(CuiProjectStage.class);

    @Getter(lazy = true)
    private final LocaleConfiguration portalLocaleConfiguration = PortalBeanManager
            .resolveRequiredBean(LocaleConfiguration.class);

    @Getter(lazy = true)
    private final PortalResourceBundleBean portalResourceBundleBean = PortalBeanManager
            .resolveRequiredBean(PortalResourceBundleBean.class);

    @Override
    public ProjectStage getProjectStage() {
        var stage = getPortalProjectStage();
        if (stage.isDevelopment()) {
            return ProjectStage.Development;
        }
        if (stage.isTest()) {
            return ProjectStage.SystemTest;
        }
        return ProjectStage.Production;
    }

    @Override
    public Locale getDefaultLocale() {
        return getPortalLocaleConfiguration().getDefaultLocale();
    }

    @Override
    public Iterator<Locale> getSupportedLocales() {
        return getPortalLocaleConfiguration().getAvailableLocales().iterator();
    }

    @Override
    public ResourceBundle getResourceBundle(FacesContext ctx, String name) {
        if (PortalResourceBundleBean.BUNDLE_NAME.equals(name)) {
            LOGGER.debug("Requesting PortalResourceBundleBean");
            return getPortalResourceBundleBean();
        }
        return super.getResourceBundle(ctx, name);
    }

}
