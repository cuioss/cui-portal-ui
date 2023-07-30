package de.cuioss.portal.ui.test.tests;

import static de.cuioss.test.generator.Generators.integers;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import de.cuioss.jsf.api.components.model.menu.NavigationMenuItem;
import de.cuioss.test.generator.Generators;

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
