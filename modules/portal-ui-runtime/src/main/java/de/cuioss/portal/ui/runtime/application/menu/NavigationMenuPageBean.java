/*
 * Copyright © 2025 CUI-OpenSource-Software (info@cuioss.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.cuioss.portal.ui.runtime.application.menu;

import de.cuioss.jsf.api.components.model.menu.NavigationMenuItem;
import de.cuioss.portal.authentication.AuthenticatedUserInfo;
import de.cuioss.portal.ui.api.menu.NavigationMenuProvider;
import de.cuioss.tools.logging.CuiLogger;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * Helper bean used for page access.
 *
 * @author Oliver Wolff
 */
@Named("navigationMenuPageBean")
@RequestScoped
@EqualsAndHashCode(of = {"displayMenu", "navigationMenuItems"})
@ToString(of = {"displayMenu", "navigationMenuItems"})
public class NavigationMenuPageBean implements Serializable {

    @Serial
    private static final long serialVersionUID = 2241686723846198192L;

    private static final CuiLogger LOGGER = new CuiLogger(NavigationMenuPageBean.class);

    @Inject
    AuthenticatedUserInfo userInfo;

    @Inject
    NavigationMenuProvider menuProvider;

    @Getter
    private boolean displayMenu = false;

    @Getter
    private List<NavigationMenuItem> navigationMenuItems;

    /**
     * Initializes the bean.
     */
    @PostConstruct
    public void initBean() {
        displayMenu = userInfo.isAuthenticated();
        LOGGER.debug("Setting displayMenu to '%s'", displayMenu);
        navigationMenuItems = menuProvider.getNavigationMenuRoots();
        LOGGER.trace("Resolved Navigation-Items. '%s'", navigationMenuItems);
    }
}
