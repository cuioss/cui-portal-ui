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

import de.cuioss.portal.ui.api.menu.items.AboutMenuItem;
import de.cuioss.portal.ui.api.menu.items.AccountMenuItem;
import de.cuioss.portal.ui.test.junit5.EnablePortalUiEnvironment;
import org.jboss.weld.junit5.auto.AddBeanClasses;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@EnablePortalUiEnvironment
@AddBeanClasses({AboutMenuItem.class, AccountMenuItem.class})
class PortalNavigationMenuItemsTestTest extends PortalNavigationMenuItemsTest {

    @Test
    void shouldFilterNoType() {
        assertEquals(2, getFilteredInstances().size());
    }


}
