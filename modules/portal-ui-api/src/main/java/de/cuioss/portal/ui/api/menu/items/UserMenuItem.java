package de.cuioss.portal.ui.api.menu.items;

import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import de.cuioss.jsf.api.components.model.menu.NavigationMenuItemContainerImpl;
import de.cuioss.portal.authentication.AuthenticatedUserInfo;
import de.cuioss.portal.authentication.PortalUser;
import de.cuioss.portal.core.bundle.PortalResourceBundle;
import de.cuioss.portal.ui.api.menu.PortalMenuItem;
import de.cuioss.portal.ui.api.menu.PortalNavigationMenuItemContainerImpl;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * <p>
 * Default representation of the user menu.
 * </p>
 * <em>Caution: </em> the using class must set
 * {@link NavigationMenuItemContainerImpl#setLabelValue(java.lang.String)}
 * because this is dynamically computed.
 *
 * @author Oliver Wolff
 * @author Sven Haag
 */
@PortalMenuItem
@Dependent
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class UserMenuItem extends PortalNavigationMenuItemContainerImpl {

    private static final long serialVersionUID = 1452093785009425867L;

    private static final String USER_MENU_TITLE_KEY = "com.icw.ehf.commons.portal.menu.user.title";

    /** The icon for the user. */
    public static final String ICON = "cui-icon-user";

    /** The string based id for this menu item. */
    public static final String MENU_ID = "userMenuItem";

    @Inject
    @PortalUser
    private AuthenticatedUserInfo userInfo;

    @Inject
    @PortalResourceBundle
    private ResourceBundle resourceBundle;

    /**
     * Initializes the user by setting label-value and Title-value
     */
    @PostConstruct
    public void init() {
        super.setIconStyleClass(ICON);
        super.setId(MENU_ID);
        setLabelValue(userInfo.getDisplayName());
        setTitleValue(String.format(resourceBundle.getString(USER_MENU_TITLE_KEY), userInfo.getDisplayName()));

    }
}
