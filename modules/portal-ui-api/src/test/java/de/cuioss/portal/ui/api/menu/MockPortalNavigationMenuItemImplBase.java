package de.cuioss.portal.ui.api.menu;

import javax.enterprise.context.Dependent;

@Dependent
public class MockPortalNavigationMenuItemImplBase extends PortalNavigationMenuItemImplBase {

    public static final String ID = "mock";
    private static final long serialVersionUID = 4979985201723205870L;

    @Override
    public String getId() {
        return ID;
    }
}
