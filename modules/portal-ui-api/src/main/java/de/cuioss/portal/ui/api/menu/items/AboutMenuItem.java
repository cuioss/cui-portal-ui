package de.cuioss.portal.ui.api.menu.items;

import javax.enterprise.context.Dependent;

import de.cuioss.portal.ui.api.menu.PortalMenuItem;
import de.cuioss.portal.ui.api.menu.PortalNavigationMenuItemSingleImpl;
import de.cuioss.portal.ui.api.ui.pages.AboutPage;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * <p>
 * Default representation of the help menu item. The order is 48. It references
 * {@link UserMenuItem#MENU_ID} as parentId and {@link AboutPage#OUTCOME} as
 * outcome.
 * </p>
 *
 * @author Oliver Wolff
 */
@PortalMenuItem
@Dependent
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AboutMenuItem extends PortalNavigationMenuItemSingleImpl {

    private static final long serialVersionUID = 1452093785009425867L;

    /** The label Key for this component. */
    public static final String LABEL_KEY = "com.icw.ehf.commons.portal.menu.about.label";

    /** The icon for this component. */
    public static final String ICON = "cui-icon-circle_question_mark";

    /** The string based id for this menu item. */
    public static final String MENU_ID = "aboutMenuItem";

    /**
     * Constructor.
     */
    public AboutMenuItem() {
        super.setIconStyleClass(ICON);
        super.setId(MENU_ID);
        super.setLabelKey(LABEL_KEY);
        super.setOutcome(AboutPage.OUTCOME);
    }
}
