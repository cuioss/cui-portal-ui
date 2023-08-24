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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.annotation.Priority;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;

import de.cuioss.jsf.api.application.theme.ThemeConfiguration;
import de.cuioss.portal.configuration.common.PortalPriorities;
import de.cuioss.portal.core.bundle.PortalResourceBundle;
import de.cuioss.portal.ui.api.theme.PortalThemeConfiguration;
import de.cuioss.portal.ui.api.ui.context.CuiNavigationHandler;
import de.cuioss.portal.ui.api.ui.pages.PortalCorePagesPreferences;
import de.cuioss.portal.ui.api.ui.pages.PreferencesPage;
import de.cuioss.portal.ui.runtime.application.locale.PortalLocaleManagerBean;
import de.cuioss.portal.ui.runtime.application.theme.PortalThemeManager;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Default Portal Preference Page Bean
 *
 * @author Oliver Wolff
 */
@RequestScoped
@Named(PreferencesPage.BEAN_NAME)
@Priority(PortalPriorities.PORTAL_CORE_LEVEL)
@PortalCorePagesPreferences
@EqualsAndHashCode(of = { "selectedLocale", "selectedTheme" }, doNotUseGetters = true)
@ToString(of = { "selectedLocale", "selectedTheme" }, doNotUseGetters = true)
public class PreferencesPageBean implements PreferencesPage {

    private static final long serialVersionUID = -1270494557240741123L;

    private static final String LOCALE_KEY_PREFIX = "common.locale.";

    @Inject
    private PortalThemeManager portalThemeManager;

    @Inject
    @PortalThemeConfiguration
    private ThemeConfiguration themeConfiguration;

    @Inject
    private PortalLocaleManagerBean localeManagerBean;

    @Getter
    private List<String> availableThemes;

    @Getter
    @Setter
    private String selectedTheme;

    @Getter
    private List<SelectItem> availableLocales;

    @Getter
    @Setter
    private Locale selectedLocale;

    @Inject
    @PortalResourceBundle
    private ResourceBundle resourceBundle;

    @Inject
    private FacesContext facesContext;

    @Inject
    @CuiNavigationHandler
    private NavigationHandler navigationHandler;

    /**
     * Initializes the lists for the drop-down menus.
     */
    @PostConstruct
    public void initBean() {
        this.availableThemes = this.themeConfiguration.getAvailableThemes();
        this.selectedTheme = this.portalThemeManager.getTheme();
        this.availableLocales = new ArrayList<>();
        for (final Locale locale : this.localeManagerBean.getAvailableLocales()) {
            this.availableLocales.add(new SelectItem(locale, this.resourceBundle
                    .getString(new StringBuilder(LOCALE_KEY_PREFIX).append(locale.getLanguage()).toString())));
        }
        this.selectedLocale = this.localeManagerBean.getLocale();
    }

    /**
     * To be called on valueChange for the theme-selection
     *
     * @param changeEvent
     */
    public void themeChangeListener(final ValueChangeEvent changeEvent) {
        this.selectedTheme = (String) changeEvent.getNewValue();
        this.portalThemeManager.saveTheme(this.selectedTheme);
        this.navigationHandler.handleNavigation(this.facesContext, null, PreferencesPage.OUTCOME);
    }

    /**
     * To be called on valueChange for the locale-selection
     *
     * @param changeEvent
     */
    public void localeChangeListener(final ValueChangeEvent changeEvent) {
        this.selectedLocale = (Locale) changeEvent.getNewValue();
        this.localeManagerBean.saveUserLocale(this.selectedLocale);
        this.navigationHandler.handleNavigation(this.facesContext, null, PreferencesPage.OUTCOME);
    }
}
