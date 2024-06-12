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

import de.cuioss.portal.common.bundle.ResourceBundleWrapper;
import de.cuioss.portal.common.priority.PortalPriorities;
import de.cuioss.portal.ui.api.ui.context.CuiNavigationHandler;
import de.cuioss.portal.ui.api.ui.pages.PreferencesPage;
import de.cuioss.portal.ui.runtime.application.locale.PortalLocaleManagerBean;
import de.cuioss.portal.ui.runtime.application.theme.PortalThemeConfiguration;
import de.cuioss.portal.ui.runtime.application.theme.UserThemeBean;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.NavigationHandler;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ValueChangeEvent;
import jakarta.faces.model.SelectItem;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Default Portal Preference Page Bean
 *
 * @author Oliver Wolff
 */
@RequestScoped
@Named(PreferencesPage.BEAN_NAME)
@Priority(PortalPriorities.PORTAL_CORE_LEVEL)
@EqualsAndHashCode(of = {"selectedLocale", "selectedTheme"}, doNotUseGetters = true)
@ToString(of = {"selectedLocale", "selectedTheme"}, doNotUseGetters = true)
public class PreferencesPageBean implements PreferencesPage {

    @Serial
    private static final long serialVersionUID = -1270494557240741123L;

    private static final String LOCALE_KEY_PREFIX = "common.locale.";

    @Inject
    private UserThemeBean userThemeBean;

    @Inject
    private PortalThemeConfiguration themeConfiguration;

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
    private ResourceBundleWrapper resourceBundle;

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
        availableThemes = themeConfiguration.getAvailableThemes();
        selectedTheme = userThemeBean.getTheme();
        availableLocales = new ArrayList<>();
        for (final Locale locale : localeManagerBean.getAvailableLocales()) {
            availableLocales.add(new SelectItem(locale, resourceBundle
                .getString(LOCALE_KEY_PREFIX + locale.getLanguage())));
        }
        selectedLocale = localeManagerBean.getUserLocale();
    }

    /**
     * To be called on valueChange for the theme-selection
     *
     * @param changeEvent
     */
    public void themeChangeListener(final ValueChangeEvent changeEvent) {
        selectedTheme = (String) changeEvent.getNewValue();
        userThemeBean.saveTheme(selectedTheme);
        navigationHandler.handleNavigation(facesContext, null, PreferencesPage.OUTCOME);
    }

    /**
     * To be called on valueChange for the locale-selection
     *
     * @param changeEvent
     */
    public void localeChangeListener(final ValueChangeEvent changeEvent) {
        selectedLocale = (Locale) changeEvent.getNewValue();
        localeManagerBean.saveUserLocale(selectedLocale);
        navigationHandler.handleNavigation(facesContext, null, PreferencesPage.OUTCOME);
    }
}
