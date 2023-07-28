package de.cuioss.portal.ui.runtime.application.menu;

import java.util.Map;

import de.cuioss.jsf.api.components.model.menu.NavigationMenuItem;
import de.cuioss.jsf.api.components.model.menu.NavigationMenuItemSeparator;
import de.cuioss.portal.ui.api.menu.PortalNavigationMenuConfigParser;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author Oliver Wolff
 *
 */
@RequiredArgsConstructor
public class PortalNavigationMenuItemSeparatorImpl extends PortalNavigationMenuConfigParser
        implements NavigationMenuItemSeparator {

    private static final long serialVersionUID = -4539137375412075634L;

    @Getter
    private final String id;

    @Getter(value = AccessLevel.PROTECTED)
    private final Map<String, String> menuConfig;

    @Override
    public String getResolvedTitle() {
        return null;
    }

    @Override
    public String getTitleKey() {
        return null;
    }

    @Override
    public String getTitleValue() {
        return null;
    }

    @Override
    public boolean isTitleAvailable() {
        return false;
    }

    @Override
    public String getIconStyleClass() {
        return null;
    }

    @Override
    public boolean isDisabled() {
        return false;
    }

    @Override
    public int compareTo(final NavigationMenuItem other) {
        return getOrder().compareTo(other.getOrder());
    }
}
