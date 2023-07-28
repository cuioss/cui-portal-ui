package de.cuioss.portal.ui.runtime.application.menu;

import javax.enterprise.context.Dependent;

import de.cuioss.portal.ui.api.menu.PortalMenuItem;
import de.cuioss.portal.ui.api.menu.PortalNavigationMenuItemSingleImpl;

@SuppressWarnings("javadoc")
@Dependent
@PortalMenuItem
public class TestNavigationMenuItemSingleWithIdA extends PortalNavigationMenuItemSingleImpl {

    private static final long serialVersionUID = 2844674417418991339L;

    public TestNavigationMenuItemSingleWithIdA() {
        super.setId("A");
    }
}
