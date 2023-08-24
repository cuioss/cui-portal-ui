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
package de.cuioss.portal.ui.runtime.support;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;

import de.cuioss.jsf.test.mock.application.ThemeConfigurationMock;
import de.cuioss.portal.ui.api.theme.PortalThemePersistencesService;
import de.cuioss.portal.ui.api.theme.ThemePersistenceService;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Mock variant of {@link ThemePersistenceService}
 *
 * @author Oliver Wolff
 */
@PortalThemePersistencesService
@ApplicationScoped
@Alternative
@EqualsAndHashCode
@ToString
public class PortalThemePersistenceServiceMock implements Serializable, ThemePersistenceService {

    private static final long serialVersionUID = -6613799095497133030L;

    @Getter
    private final List<String> availableThemes = ThemeConfigurationMock.AVAILABLE_THEMES;

    @Getter
    private String theme = ThemeConfigurationMock.DEFAULT_THEME;

    @Override
    public void saveTheme(final String newTheme) {
        if (!availableThemes.contains(newTheme)) {
            throw new IllegalArgumentException("Given theme '" + newTheme + "' must be one of " + availableThemes);
        }
        theme = newTheme;
    }

}
