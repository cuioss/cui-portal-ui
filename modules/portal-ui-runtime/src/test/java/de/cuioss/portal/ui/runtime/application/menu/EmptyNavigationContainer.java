package de.cuioss.portal.ui.runtime.application.menu;

import de.cuioss.portal.ui.api.menu.PortalMenuItem;
import de.cuioss.portal.ui.api.menu.PortalNavigationMenuItemContainerImpl;

@SuppressWarnings("javadoc")
@PortalMenuItem
public class EmptyNavigationContainer extends PortalNavigationMenuItemContainerImpl {

    private static final long serialVersionUID = 5073024579976343083L;

    public static final String LABEL_KEY = "portal.runtime.configuration.settings.menu.label";

    public static final String ICON = "cui-icon-settings";

    /** The string based id for this menu item. */
    public static final String MENU_ID = "emptyMenuItem";

    public EmptyNavigationContainer() {
        super.setIconStyleClass(ICON);
        super.setId(MENU_ID);
        super.setLabelKey(LABEL_KEY);
    }

}
