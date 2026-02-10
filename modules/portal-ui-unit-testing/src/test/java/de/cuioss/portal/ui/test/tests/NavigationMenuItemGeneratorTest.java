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
import de.cuioss.test.generator.Generators;
import org.junit.jupiter.api.Test;

import static de.cuioss.test.generator.Generators.integers;
import static org.junit.jupiter.api.Assertions.assertEquals;

class NavigationMenuItemGeneratorTest {

    @Test
    void shouldGenerateDisjunct() {
        var generator = Generators.asCollectionGenerator(new NavigationMenuItemGenerator());

        int expected = integers(30, 50).next();
        assertEquals(expected, generator.set(expected).size());

    }

    @Test
    void shouldBeCorrectType() {
        assertEquals(NavigationMenuItem.class, new NavigationMenuItemGenerator().getType());
    }

}
