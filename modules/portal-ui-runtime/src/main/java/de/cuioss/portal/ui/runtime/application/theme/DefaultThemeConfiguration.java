package de.cuioss.portal.ui.runtime.application.theme;

import static de.cuioss.portal.configuration.PortalConfigurationKeys.THEME_AVAILABLE;
import static de.cuioss.portal.configuration.PortalConfigurationKeys.THEME_DEFAULT;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import de.cuioss.jsf.api.application.theme.ThemeConfiguration;
import de.cuioss.jsf.api.application.theme.impl.ThemeConfigurationImpl;
import de.cuioss.jsf.api.application.theme.impl.ThemeManager;
import de.cuioss.portal.configuration.PortalConfigurationChangeEvent;
import de.cuioss.portal.configuration.types.ConfigAsList;
import de.cuioss.portal.ui.api.theme.PortalThemeConfiguration;
import de.cuioss.tools.logging.CuiLogger;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Default {@link ThemeConfiguration} reads default-theme and available themes
 * from the configuration system
 *
 * @author Oliver Wolff
 */
@ApplicationScoped
@PortalThemeConfiguration
@Named(ThemeConfigurationImpl.BEAN_NAME)
@EqualsAndHashCode(of = { "availableThemes", "defaultTheme", "cssName", "cssLibrary" }, doNotUseGetters = true)
@ToString(of = { "availableThemes", "defaultTheme", "cssName", "cssLibrary" }, doNotUseGetters = true)
public class DefaultThemeConfiguration implements ThemeConfiguration, Serializable {

    private static final long serialVersionUID = 3077568114159593192L;

    private static final CuiLogger log = new CuiLogger(DefaultThemeConfiguration.class);

    private ThemeManager themeManager;

    @Inject
    @ConfigAsList(name = THEME_AVAILABLE)
    private Provider<List<String>> availableThemesProvider;

    @Inject
    @ConfigProperty(name = THEME_DEFAULT)
    private Provider<String> defaultThemeProvider;

    /** The names of the available themes. */
    @Getter
    private List<String> availableThemes;

    /**
     * Identifying the default theme.
     */
    @Getter
    private String defaultTheme;

    /** Defaults to "application.css" */
    @Getter
    private final String cssName = "application.css";

    /** Defaults to "com.icw.portal.css" */
    @Getter
    private final String cssLibrary = "com.icw.portal.css";

    /**
     * Initializes the bean, see class documentation for details
     */
    @PostConstruct
    public void initBean() {
        doConfigure();
    }

    private void doConfigure() {

        defaultTheme = defaultThemeProvider.get();
        availableThemes = availableThemesProvider.get();
        themeManager = new ThemeManager(this);
    }

    @Override
    public String getCssForThemeName(final String themeName) {
        return themeManager.getCssForThemeName(themeName);
    }

    /**
     * Listener for {@link PortalConfigurationChangeEvent}s. Reconfigures the
     * theme-configuration
     *
     * @param deltaMap
     */
    void configurationChangeEventListener(
            @Observes @PortalConfigurationChangeEvent final Map<String, String> deltaMap) {
        if (deltaMap.containsKey(THEME_DEFAULT) || deltaMap.containsKey(THEME_AVAILABLE)) {
            log.debug("Setting default theme to {} and available themes to {}", defaultThemeProvider.get(),
                    availableThemesProvider.get());
            doConfigure();
        }
    }

}
