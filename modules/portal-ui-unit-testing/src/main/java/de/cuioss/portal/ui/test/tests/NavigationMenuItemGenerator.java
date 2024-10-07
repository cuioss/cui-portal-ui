/*
 * Copyright 2023 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * https://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.cuioss.portal.ui.test.tests;

import de.cuioss.jsf.api.components.model.menu.NavigationMenuItem;
import de.cuioss.jsf.api.components.model.menu.NavigationMenuItemContainerImpl;
import de.cuioss.jsf.api.components.model.menu.NavigationMenuItemSeparatorImpl;
import de.cuioss.jsf.api.components.model.menu.NavigationMenuItemSingleImpl;
import de.cuioss.test.generator.TypedGenerator;

import java.util.List;

import static de.cuioss.test.generator.Generators.booleans;
import static de.cuioss.test.generator.Generators.fixedValues;
import static de.cuioss.test.generator.Generators.integers;
import static de.cuioss.test.generator.Generators.letterStrings;
import static de.cuioss.tools.collect.CollectionLiterals.immutableList;

/**
 * Generates an instance of a {@linkplain NavigationMenuItem} implementation.
 *
 * @author Sven Haag
 */
public class NavigationMenuItemGenerator implements TypedGenerator<NavigationMenuItem> {

    private static final TypedGenerator<String> stringGenerator = letterStrings(1, 5);

    private static final List<Class<? extends NavigationMenuItem>> possibleItems = immutableList(
            NavigationMenuItemSingleImpl.class, NavigationMenuItemContainerImpl.class,
            NavigationMenuItemSeparatorImpl.class);

    @Override
    public NavigationMenuItem next() {
        NavigationMenuItem item = null;

        final Class<? extends NavigationMenuItem> navMenuItemClass = fixedValues(possibleItems).next();
        if (navMenuItemClass == NavigationMenuItemSingleImpl.class) {
            item = new NavigationMenuItemSingleImpl(integers(1, 100).next());
            final var navigationMenuItemSingle = (NavigationMenuItemSingleImpl) item;
            navigationMenuItemSingle.setId(stringGenerator.next());
            navigationMenuItemSingle.setRendered(booleans().next());
            navigationMenuItemSingle.setDisabled(booleans().next());
            navigationMenuItemSingle.setTitleKey(stringGenerator.next());
            navigationMenuItemSingle.setTitleValue(stringGenerator.next());
            navigationMenuItemSingle.setIconStyleClass(stringGenerator.next());

            ((NavigationMenuItemSingleImpl) item).setOnClickAction(stringGenerator.next());
            ((NavigationMenuItemSingleImpl) item).setOutcome(stringGenerator.next());
        } else if (navMenuItemClass == NavigationMenuItemContainerImpl.class) {
            item = new NavigationMenuItemContainerImpl(integers(1, 100).next());
            final var navigationMenuItemContainer = (NavigationMenuItemContainerImpl) item;
            navigationMenuItemContainer.setId(stringGenerator.next());
            navigationMenuItemContainer.setRendered(booleans().next());
            navigationMenuItemContainer.setDisabled(booleans().next());
            navigationMenuItemContainer.setTitleKey(stringGenerator.next());
            navigationMenuItemContainer.setTitleValue(stringGenerator.next());
            navigationMenuItemContainer.setIconStyleClass(stringGenerator.next());
        } else if (navMenuItemClass == NavigationMenuItemSeparatorImpl.class) {
            item = new NavigationMenuItemSeparatorImpl(integers(1, 100).next());
            final var navigationMenuItemSeparator = (NavigationMenuItemSeparatorImpl) item;
            navigationMenuItemSeparator.setId(stringGenerator.next());
            navigationMenuItemSeparator.setRendered(booleans().next());
            navigationMenuItemSeparator.setDisabled(booleans().next());
            navigationMenuItemSeparator.setTitleKey(stringGenerator.next());
            navigationMenuItemSeparator.setTitleValue(stringGenerator.next());
            navigationMenuItemSeparator.setIconStyleClass(stringGenerator.next());
        }

        return item;
    }

    @Override
    public Class<NavigationMenuItem> getType() {
        return NavigationMenuItem.class;
    }
}
