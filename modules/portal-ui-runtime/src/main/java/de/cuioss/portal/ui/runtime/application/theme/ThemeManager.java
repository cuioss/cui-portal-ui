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

import de.cuioss.tools.collect.MapBuilder;
import de.cuioss.tools.logging.CuiLogger;
import de.cuioss.tools.string.Splitter;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

import static de.cuioss.tools.base.Preconditions.checkArgument;
import static de.cuioss.tools.base.Preconditions.checkState;
import static de.cuioss.tools.collect.MoreCollections.isEmpty;
import static de.cuioss.tools.string.MoreStrings.isBlank;
import static de.cuioss.tools.string.MoreStrings.requireNotEmpty;

/**
 * Support class for translating theme-names derived from
 * {@link PortalThemeConfiguration} to actual css-file-names
 *
 * @author Oliver Wolff
 */
@EqualsAndHashCode
@ToString
public class ThemeManager implements Serializable {

    static final String CSS_PREFEXI_NAME = Splitter.on('.').splitToList(PortalThemeConfiguration.CSS_NAME).iterator()
            .next() + "-";
    @Serial
    private static final long serialVersionUID = 2368337948482686947L;
    private static final CuiLogger log = new CuiLogger(ThemeManager.class);
    private static final String CSS_SUFFIX = ".css";
    private final Map<String, String> themeNameCssMapping;

    private final List<String> availableThemes;

    private final String defaultTheme;

    /**
     * Constructor.
     *
     * @param themeConfiguration must not be null
     */
    public ThemeManager(final PortalThemeConfiguration themeConfiguration) {
        availableThemes = themeConfiguration.getAvailableThemes();
        defaultTheme = themeConfiguration.getDefaultTheme();
        // Set default and set implementation to immutable.
        checkThemeNameContract(themeConfiguration.getAvailableThemes(), themeConfiguration.getDefaultTheme());
        var mapBuilder = new MapBuilder<String, String>();
        for (String themeName : themeConfiguration.getAvailableThemes()) {
            mapBuilder.put(themeName,
                    CSS_PREFEXI_NAME + themeName.toLowerCase() + CSS_SUFFIX);
        }
        themeNameCssMapping = mapBuilder.toImmutableMap();
    }

    private static void checkThemeNameContract(final List<String> availableThemes, final String defaultTheme) {
        checkArgument(!isEmpty(availableThemes), "no availableThemes provided");
        requireNotEmpty(defaultTheme, "defaultTheme");
        checkState(availableThemes.contains(defaultTheme), "Default theme: '%s' can not be found within '%s'",
                defaultTheme, availableThemes);

    }

    /**
     * Actual 'business' method for getting a concrete application.css from
     *
     * @param themeName to be looked up. If it is null, empty or not part of
     *                  {@link PortalThemeConfiguration#getAvailableThemes()} it
     *                  returns the configured
     *                  {@link PortalThemeConfiguration#getDefaultTheme()}
     * @return the corresponding css name.
     */
    public String getCssForThemeName(final String themeName) {
        return themeNameCssMapping.get(verifyThemeName(themeName));
    }

    private String verifyThemeName(final String themeName) {
        var themeLookupName = themeName;
        if (isBlank(themeName) || !availableThemes.contains(themeName)) {
            themeLookupName = defaultTheme;
            log.debug("No configured theme found for {}, using default theme.", themeLookupName);
        }
        return themeLookupName;
    }

}
