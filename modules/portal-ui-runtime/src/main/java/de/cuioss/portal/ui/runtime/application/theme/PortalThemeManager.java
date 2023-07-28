package de.cuioss.portal.ui.runtime.application.theme;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

import de.cuioss.jsf.api.application.theme.ThemeNameProducer;
import de.cuioss.jsf.api.application.theme.impl.ThemeNameProducerImpl;
import de.cuioss.portal.ui.api.theme.PortalThemeNameProducer;
import de.cuioss.portal.ui.api.theme.PortalThemePersistencesService;
import de.cuioss.portal.ui.api.theme.ThemePersistenceService;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Provides as central point for accessing theme related aspects.
 * <ul>
 * <li>It produces instances of {@link ThemeNameProducer}</li>
 * <li>It acts as a session scoped cache for the actual selected theme></li>
 * <li>It separates inline usage (Portal, managed beans) from the extension
 * point, defined with {@link ThemePersistenceService}.</li>
 * </ul>
 *
 * @author Oliver Wolff
 */
@Named
@SessionScoped
@EqualsAndHashCode(of = "theme", doNotUseGetters = true)
@ToString(of = "theme", doNotUseGetters = true)
public class PortalThemeManager implements Serializable, ThemePersistenceService {

    private static final long serialVersionUID = -2817492210744540637L;

    @Inject
    @PortalThemePersistencesService
    private ThemePersistenceService themePersistenceService;

    @Getter
    private String theme;

    /**
     * Initializer method for the bean
     */
    @PostConstruct
    public void init() {
        theme = themePersistenceService.getTheme();
    }

    @Produces
    @Named(ThemeNameProducerImpl.BEAN_NAME)
    @PortalThemeNameProducer
    @RequestScoped
    ThemeNameProducer produceThemeNameProducer() {
        return themePersistenceService;
    }

    @Override
    public void saveTheme(final String newTheme) {
        theme = newTheme;
        themePersistenceService.saveTheme(newTheme);
    }
}
