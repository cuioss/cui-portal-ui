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
package de.cuioss.portal.ui.api.menu;

import de.cuioss.jsf.api.components.model.menu.NavigationMenuItem;
import de.cuioss.jsf.api.components.model.menu.NavigationMenuItemContainer;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/**
 * Provider for the content of the portal specific navigation menu
 *
 * @author Oliver Wolff
 */
public interface NavigationMenuProvider extends Serializable {

    /**
     * @return the list of {@link NavigationMenuItem} representing the top level
     * element of the navigation menu.
     */
    List<NavigationMenuItem> getNavigationMenuRoots();

    /**
     * @return boolean indicating whether to display the navigation menu at all.
     */
    boolean isDisplayNavigationMenu();

    /**
     * Returns an {@link NavigationMenuItem} by id
     *
     * @param id may be null or empty
     * @return the found {@link NavigationMenuItem} if present,
     * {@link Optional#empty()} if id is {@code null} or none could be found
     */
    Optional<NavigationMenuItem> getMenuItemById(String id);

    List<NavigationMenuItem> getMenuItemsByParentId(String parentId);

    /**
     * Returns an {@link NavigationMenuItemContainer} by id
     *
     * @param id may be null or empty
     * @return the found {@link NavigationMenuItemContainer} if present,
     * {@link Optional#empty()} if id is {@code null} or none could be
     * found, or the found id does not represent a container
     */
    Optional<NavigationMenuItemContainer> getContainerMenuItemById(String id);

    /**
     * Returns a List of {@link NavigationMenuItem} for the given Ids
     *
     * @param ids to be looked up
     * @return the found {@link NavigationMenuItem} if present, an empty list
     * otherwise
     */
    List<NavigationMenuItem> getMenuItemsByIds(String... ids);
}
