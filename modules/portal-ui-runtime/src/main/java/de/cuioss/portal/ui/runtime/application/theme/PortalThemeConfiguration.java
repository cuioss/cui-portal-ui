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

import de.cuioss.portal.configuration.types.ConfigAsList;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

import static de.cuioss.portal.configuration.PortalConfigurationKeys.THEME_AVAILABLE;
import static de.cuioss.portal.configuration.PortalConfigurationKeys.THEME_DEFAULT;

/**
 * Reads default-theme and available themes
 * from the configuration system.
 *
 * @author Oliver Wolff
 */
@ApplicationScoped
@EqualsAndHashCode(of = {"availableThemes", "defaultTheme"}, doNotUseGetters = true)
@ToString(of = {"availableThemes", "defaultTheme"}, doNotUseGetters = true)
public class PortalThemeConfiguration implements Serializable {

    /**
     * The name of the css to be looked for. Caution: it is assumed to end with
     * ".css". The portal/styling assumes the name to be 'application.css'
     */
    public static final String CSS_NAME = "application.css";
    /**
     * The name of the library where the css files are located in. The portal
     * styling assumes 'de.cuioss.portal.css'
     */
    public static final String CSS_LIBRARY = "de.cuioss.portal.css";
    @Serial
    private static final long serialVersionUID = 3077568114159593192L;
    private ThemeManager themeManager;
    /**
     * The (configured) list of available themes.
     */
    @Inject
    @ConfigAsList(name = THEME_AVAILABLE)
    @Getter
    private List<String> availableThemes;
    /**
     * the (configured default theme). It must be one of
     * {@link #getAvailableThemes()}
     */
    @Inject
    @ConfigProperty(name = THEME_DEFAULT)
    @Getter
    private String defaultTheme;

    /**
     * Initializes the bean, see class documentation for details
     */
    @PostConstruct
    public void initBean() {
        themeManager = new ThemeManager(this);
    }

    public String getCssForThemeName(final String themeName) {
        return themeManager.getCssForThemeName(themeName);
    }

}
