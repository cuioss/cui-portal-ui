package de.cuioss.portal.ui.api.menu;

import java.util.Map;

import de.cuioss.jsf.api.components.model.menu.NavigationMenuItem;
import de.cuioss.tools.logging.CuiLogger;
import de.cuioss.tools.string.MoreStrings;
import lombok.Getter;

/**
 * Abstract class to implement {@link NavigationMenuItem#getOrder()} and
 * {@link NavigationMenuItem#isRendered()} using a configuration map.
 */
public abstract class PortalNavigationMenuConfigParser {

    private static final CuiLogger log = new CuiLogger(PortalNavigationMenuConfigParser.class);

    private static final String ENABLED_SUFFIX = ".enabled";

    private static final String ORDER_SUFFIX = ".order";

    @Getter(lazy = true)
    private final String parentId = resolveParentId();

    @Getter(lazy = true)
    private final Integer order = resolveOrder();

    /**
     * Evaluates the order to sort the menu items.
     *
     * See also
     * {@link de.icw.cui.portal.configuration.PortalConfigurationKeys#MENU_BASE}.
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
            log.warn("Portal-138: Invalid menu configuration: Order property '"
                    + getMenuConfig().get(getId() + ORDER_SUFFIX) + "' for menu item '" + getId()
                    + "' can not be parsed", e);
            return -1;
        }
    }

    protected abstract Map<String, String> getMenuConfig();

    protected abstract String getId();

    /**
     * Evaluates the rendered attributes
     *
     * Defaults to true if the .order config key is set and .enabled config key is
     * not set or set to true.
     *
     * See also
     * {@link de.icw.cui.portal.configuration.PortalConfigurationKeys#MENU_BASE}.
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
