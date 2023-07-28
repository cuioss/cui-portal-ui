package de.cuioss.portal.ui.api.menu.items;

import javax.enterprise.context.Dependent;

import de.cuioss.portal.ui.api.menu.PortalMenuItem;
import de.cuioss.portal.ui.api.menu.PortalNavigationMenuItemSingleImpl;
import de.cuioss.portal.ui.api.ui.pages.LogoutPage;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * <p>
 * Default representation of the logout menu item. The order is 48. It has
 * <code>null</code> as parentId (top-level element) and
 * {@link LogoutPage#OUTCOME} as outcome.
 * </p>
 *
 * @author Oliver Wolff
 */
@PortalMenuItem
@Dependent
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class LogoutMenuItem extends PortalNavigationMenuItemSingleImpl {

    private static final long serialVersionUID = -1265061305901788409L;

    /** The label Key for this component. */
    public static final String LABEL_KEY = "com.icw.ehf.commons.portal.menu.logout.label";

    /** The title Key for this component. */
    public static final String TITLE_KEY = "com.icw.ehf.commons.portal.menu.logout.title";

    /** The icon for this component. */
    public static final String ICON = "cui-icon-power";

    /** The string based id for this menu item. */
    public static final String MENU_ID = "logoutMenuItem";

    /**
     * Constructor.
     */
    public LogoutMenuItem() {
        super.setIconStyleClass(ICON);
        super.setId(MENU_ID);
        super.setLabelKey(LABEL_KEY);
        super.setTitleKey(TITLE_KEY);
        super.setOutcome(LogoutPage.OUTCOME);
    }
}
