package de.cuioss.portal.ui.api.menu;

import java.util.ArrayList;
import java.util.List;

import de.cuioss.jsf.api.components.model.menu.NavigationMenuItem;
import de.cuioss.jsf.api.components.model.menu.NavigationMenuItemContainer;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Oliver Wolff
 *
 */
@EqualsAndHashCode(callSuper = true)
public class PortalNavigationMenuItemContainerImpl extends PortalNavigationMenuItemImplBase
        implements NavigationMenuItemContainer {

    private static final long serialVersionUID = 2451583664984874108L;

    @Getter
    @Setter
    private List<NavigationMenuItem> children = new ArrayList<>();

}
