package de.cuioss.portal.ui.test.tests;

import static de.cuioss.tools.string.MoreStrings.emptyToNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import de.cuioss.jsf.api.components.model.menu.NavigationMenuItem;
import de.cuioss.jsf.api.components.model.menu.NavigationMenuItemSingle;
import de.cuioss.jsf.api.components.model.menu.NavigationMenuLabelProvider;
import lombok.experimental.UtilityClass;

/**
 * Base class for testing instances of {@link NavigationMenuItem}s
 */
@UtilityClass
public class NavigationMenuItemAsserts {

    /**
     * Provides a number of simple assertions for a given {@link NavigationMenuItem}
     *
     * @param item to be checked
     */
    public static void assertBasicAttributes(NavigationMenuItem item) {
        assertNotNull(item, "NavigationMenuItem must not be null");
        assertNotNull(emptyToNull(item.getId()), "Id must not be null nor empty ");

        if (item instanceof NavigationMenuLabelProvider) {
            assertNotNull(emptyToNull(((NavigationMenuLabelProvider) item).getResolvedLabel()),
                    "Resolved label must not be null");
        }

        if (item instanceof NavigationMenuItemSingle) {
            var singleMenuItem = (NavigationMenuItemSingle) item;
            assertNotNull(emptyToNull(singleMenuItem.getOutcome()), "Outcome must not be null");
        }
    }

}
