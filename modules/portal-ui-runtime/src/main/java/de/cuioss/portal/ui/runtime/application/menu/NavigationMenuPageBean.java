package de.cuioss.portal.ui.runtime.application.menu;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import de.cuioss.jsf.api.components.model.menu.NavigationMenuItem;
import de.cuioss.portal.authentication.AuthenticatedUserInfo;
import de.cuioss.portal.authentication.PortalUser;
import de.cuioss.portal.ui.api.menu.NavigationMenuProvider;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Helper bean used for page access.
 *
 * @author Oliver Wolff
 */
@Named("navigationMenuPageBean")
@RequestScoped
@EqualsAndHashCode(of = { "displayMenu", "navigationMenuItems" })
@ToString(of = { "displayMenu", "navigationMenuItems" })
public class NavigationMenuPageBean implements Serializable {

    private static final long serialVersionUID = 2241686723846198192L;

    @PortalUser
    @Inject
    private AuthenticatedUserInfo userInfo;

    @Inject
    private NavigationMenuProvider menuProvider;

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
        navigationMenuItems = menuProvider.getNavigationMenuRoots();
    }
}
