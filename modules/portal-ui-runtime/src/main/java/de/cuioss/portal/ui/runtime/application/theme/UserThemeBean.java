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

import static de.cuioss.portal.configuration.PortalConfigurationKeys.THEME_DEFAULT;
import static de.cuioss.tools.base.Preconditions.checkArgument;

import java.io.Serializable;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Provider;

import de.cuioss.portal.common.priority.PortalPriorities;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Priority;

import de.cuioss.portal.core.storage.ClientStorage;
import de.cuioss.portal.core.storage.PortalClientStorage;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Default implementation that stores the currently active / selected theme
 * within session. On storing / retrieving it uses {@link ClientStorage} for
 * accessing the client (default-implementation is cookie)
 *
 * @author Oliver Wolff
 */
@Priority(PortalPriorities.PORTAL_CORE_LEVEL)
@SessionScoped
@EqualsAndHashCode
@ToString
public class UserThemeBean implements Serializable {

    private static final long serialVersionUID = 5242120351578770611L;

    @Inject
    private PortalThemeConfiguration themeConfiguration;

    @Getter
    private String theme;

    @Inject
    @PortalClientStorage
    private Provider<ClientStorage> clientStorage;

    /**
     * Initializer method for the bean
     */
    @PostConstruct
    public void init() {
        theme = themeConfiguration.getDefaultTheme();
        if (clientStorage.get().containsKey(THEME_DEFAULT)) {
            final var storedTheme = clientStorage.get().get(THEME_DEFAULT);
            if (themeConfiguration.getAvailableThemes().contains(storedTheme)) {
                theme = storedTheme;
            }
        }
    }

    /**
     * Saves the theme if a user has selected a new one in the preferences. The
     * Service must store it accordingly.
     *
     * @param newTheme to be set, must be one of
     *                 {@link ThemeConfiguration#getAvailableThemes()}. The
     *                 implementation must ensure this.
     */
    public void saveTheme(final String newTheme) {
        checkArgument(themeConfiguration.getAvailableThemes().contains(newTheme),
                "Given theme '%s' must be one of '%s' ", newTheme, themeConfiguration.getAvailableThemes());
        theme = newTheme;
        clientStorage.get().put(THEME_DEFAULT, newTheme);
    }

    /**
     * Shorthand for calling
     * {@link PortalThemeConfiguration#getCssForThemeName(String)} with the
     * contained themename for the user.
     *
     * @return the resolved css name
     */
    public String resolveFinalThemeCssName() {
        return themeConfiguration.getCssForThemeName(theme);
    }

}
