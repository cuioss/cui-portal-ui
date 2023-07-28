package de.cuioss.portal.ui.api.menu;

import de.cuioss.jsf.api.components.model.menu.NavigationMenuItemExternalSingle;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Oliver Wolff
 *
 */
@EqualsAndHashCode(callSuper = true)
public class PortalNavigationMenuItemExternalSingleImpl extends PortalNavigationMenuItemImplBase
        implements NavigationMenuItemExternalSingle {

    private static final long serialVersionUID = -1489949340663388532L;

    @Getter
    @Setter
    private String hRef;

    @Getter
    @Setter
    private String target;

    @Getter
    @Setter
    private String onClickAction;
}
