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
package de.cuioss.portal.ui.api.menu;

import de.cuioss.jsf.api.components.model.menu.NavigationMenuItem;
import de.cuioss.tools.logging.CuiLogger;
import de.cuioss.tools.string.MoreStrings;
import lombok.Getter;

import static de.cuioss.portal.ui.api.PortalUiApiLogMessages.*;

import java.util.Map;

/**
 * Abstract class to implement {@link NavigationMenuItem#getOrder()} and
 * {@link NavigationMenuItem#isRendered()} using a configuration map.
 */
public abstract class PortalNavigationMenuConfigParser {

    private static final CuiLogger LOGGER = new CuiLogger(PortalNavigationMenuConfigParser.class);

    private static final String ENABLED_SUFFIX = ".enabled";

    private static final String ORDER_SUFFIX = ".order";

    @Getter(lazy = true)
    private final String parentId = resolveParentId();

    @Getter(lazy = true)
    private final Integer order = resolveOrder();

    /**
     * Evaluates the order to sort the menu items.
     * <p>
     * See also
     * {@link de.cuioss.portal.configuration.PortalConfigurationKeys#MENU_BASE}.
     *
     * @return the order if set or -1 if order is not set.
     */
    public Integer resolveOrder() {
        if (MoreStrings.isEmpty(getMenuConfig().get(getId() + ORDER_SUFFIX))) {
            return -1;
        }
        try {
            return Integer.parseInt(getMenuConfig().get(getId() + ORDER_SUFFIX));
        } catch (NumberFormatException e) {
            LOGGER.warn(WARN.PORTAL_138_INVALID_MENU_ORDER, getMenuConfig().get(getId() + ORDER_SUFFIX), getId());
            return -1;
        }
    }

    protected abstract Map<String, String> getMenuConfig();

    protected abstract String getId();

    /**
     * Evaluates the rendered attributes
     * <p>
     * Defaults to true if the .order config key is set and .enabled config key is
     * not set or set to true.
     * <p>
     * See also
     * {@link de.cuioss.portal.configuration.PortalConfigurationKeys#MENU_BASE}.
     *
     * @return true if the menu item should be rendered.
     */
    public boolean isRendered() {
        if (!getMenuConfig().containsKey(getId() + ORDER_SUFFIX)
                || MoreStrings.isEmpty(getMenuConfig().get(getId() + ORDER_SUFFIX))) {
            return false;
        }
        return !getMenuConfig().containsKey(getId() + ENABLED_SUFFIX)
                || Boolean.parseBoolean(getMenuConfig().get(getId() + ENABLED_SUFFIX));
    }

    private String resolveParentId() {
        var parentKey = getId() + ".parent";
        if (!getMenuConfig().containsKey(parentKey)) {
            return null;
        }
        return getMenuConfig().get(parentKey);
    }

}
