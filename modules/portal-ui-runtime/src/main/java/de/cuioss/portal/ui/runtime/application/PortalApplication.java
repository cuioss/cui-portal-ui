package de.cuioss.portal.ui.runtime.application;

import java.util.Iterator;
import java.util.Locale;

import javax.faces.application.Application;
import javax.faces.application.ApplicationWrapper;
import javax.faces.application.ProjectStage;

import de.cuioss.portal.configuration.application.PortalProjectStage;
import de.cuioss.portal.core.cdi.PortalBeanManager;
import de.cuioss.portal.ui.runtime.application.configuration.LocaleConfiguration;
import de.cuioss.tools.logging.CuiLogger;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Decorate the standard {@link Application} in order to bridge the portal
 * specific implementations with the JSF-specific ones. Currently these are:
 * <ul>
 * <li>{@link Application#getProjectStage()}</li>
 * </ul>
 *
 * @author Oliver Wolff
 * @author Sven Haag, Sven Haag
 */
@RequiredArgsConstructor
public class PortalApplication extends ApplicationWrapper {

    private static final CuiLogger log = new CuiLogger(PortalApplication.class);

    private static final String PORTAL_105 = "Portal-105: Unable to load PortalBeanManager CDI-context. Defaults to ProjectStage.Production.";

    @Getter
    private final Application wrapped;

    private PortalProjectStage portalProjectStage;

    private LocaleConfiguration localeConfiguration;

    @Override
    public ProjectStage getProjectStage() {
        if (null == portalProjectStage) {
            try {
                portalProjectStage = PortalBeanManager.resolveBean(PortalProjectStage.class, null)
                        .orElseThrow(() -> new IllegalArgumentException(
                                PortalBeanManager.createErrorMessage(PortalProjectStage.class, null)));
            } catch (IllegalStateException | IllegalArgumentException e) {
                log.warn(PORTAL_105, e);
            }
        }

        if (null == portalProjectStage) {
            return ProjectStage.Production;
        }
        if (portalProjectStage.getProjectStage().isDevelopment()) {
            return ProjectStage.Development;
        }
        if (portalProjectStage.getProjectStage().isTest()) {
            return ProjectStage.SystemTest;
        }

        return ProjectStage.Production;
    }

    private LocaleConfiguration getLocaleConfiguration() {
        if (null == localeConfiguration) {
            localeConfiguration = PortalBeanManager.resolveBean(LocaleConfiguration.class, null)
                    .orElseThrow(() -> new IllegalStateException("Locale configuration not available"));
        }
        return localeConfiguration;
    }

    @Override
    public Locale getDefaultLocale() {
        return getLocaleConfiguration().getDefaultLocale();
    }

    @Override
    public Iterator<Locale> getSupportedLocales() {
        return getLocaleConfiguration().getAvailableLocales().iterator();
    }

}
