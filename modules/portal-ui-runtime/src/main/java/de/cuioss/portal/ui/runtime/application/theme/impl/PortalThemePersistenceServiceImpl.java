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
package de.cuioss.portal.ui.runtime.application.theme.impl;

import static de.cuioss.portal.configuration.PortalConfigurationKeys.THEME_DEFAULT;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.annotation.Priority;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Provider;

import de.cuioss.jsf.api.application.theme.ThemeConfiguration;
import de.cuioss.portal.configuration.common.PortalPriorities;
import de.cuioss.portal.core.storage.ClientStorage;
import de.cuioss.portal.core.storage.PortalClientStorage;
import de.cuioss.portal.ui.api.theme.PortalThemeConfiguration;
import de.cuioss.portal.ui.api.theme.PortalThemePersistencesService;
import de.cuioss.portal.ui.api.theme.ThemePersistenceService;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Default implementation that stores the currently active / selected theme
 * within session. Should be replaced by concrete implementation that stores it
 * in some persistence layer. FIXME owolff consider using cookie instead of
 * session as default.
 *
 * @author Oliver Wolff
 */
@PortalThemePersistencesService
@Priority(PortalPriorities.PORTAL_CORE_LEVEL)
@SessionScoped
@EqualsAndHashCode
@ToString
public class PortalThemePersistenceServiceImpl implements Serializable, ThemePersistenceService {

    private static final long serialVersionUID = 5242120351578770611L;

    @Inject
    @PortalThemeConfiguration
    private ThemeConfiguration themeConfiguration;

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
        this.theme = this.themeConfiguration.getDefaultTheme();
        if (this.clientStorage.get().containsKey(THEME_DEFAULT)) {
            final var storedTheme = this.clientStorage.get().get(THEME_DEFAULT);
            if (this.themeConfiguration.getAvailableThemes().contains(storedTheme)) {
                this.theme = storedTheme;
            }
        }
    }

    @Override
    public void saveTheme(final String newTheme) {
        if (!this.themeConfiguration.getAvailableThemes().contains(newTheme)) {
            throw new IllegalArgumentException(
                    "Given theme '" + newTheme + "' must be one of " + this.themeConfiguration.getAvailableThemes());
        }
        this.theme = newTheme;
        this.clientStorage.get().put(THEME_DEFAULT, newTheme);
    }

}
