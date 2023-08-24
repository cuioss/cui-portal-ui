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
