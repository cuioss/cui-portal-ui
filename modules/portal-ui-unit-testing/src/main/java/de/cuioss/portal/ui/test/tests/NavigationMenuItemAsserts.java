/*
 * Copyright © 2025 CUI-OpenSource-Software (info@cuioss.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.cuioss.portal.ui.test.tests;

import de.cuioss.jsf.api.components.model.menu.NavigationMenuItem;
import de.cuioss.jsf.api.components.model.menu.NavigationMenuItemSingle;
import de.cuioss.jsf.api.components.model.menu.NavigationMenuLabelProvider;
import lombok.experimental.UtilityClass;

import static de.cuioss.tools.string.MoreStrings.emptyToNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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

        if (item instanceof NavigationMenuLabelProvider provider) {
            assertNotNull(emptyToNull(provider.getResolvedLabel()), "Resolved label must not be null");
        }

        if (item instanceof NavigationMenuItemSingle singleMenuItem) {
            assertNotNull(emptyToNull(singleMenuItem.getOutcome()), "Outcome must not be null");
        }
    }

}
