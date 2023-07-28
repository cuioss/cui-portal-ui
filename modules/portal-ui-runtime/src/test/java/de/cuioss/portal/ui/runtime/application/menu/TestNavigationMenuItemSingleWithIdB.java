package de.cuioss.portal.ui.runtime.application.menu;

import javax.enterprise.context.Dependent;

import de.cuioss.portal.ui.api.menu.PortalMenuItem;
import de.cuioss.portal.ui.api.menu.PortalNavigationMenuItemSingleImpl;

@SuppressWarnings("javadoc")
@Dependent
@PortalMenuItem
public class TestNavigationMenuItemSingleWithIdB extends PortalNavigationMenuItemSingleImpl {

    private static final long serialVersionUID = 8837436317203976139L;

    public TestNavigationMenuItemSingleWithIdB() {
        super.setId("B");
    }
}
