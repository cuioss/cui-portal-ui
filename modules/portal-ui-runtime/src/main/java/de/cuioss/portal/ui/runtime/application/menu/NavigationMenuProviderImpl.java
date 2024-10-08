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
package de.cuioss.portal.ui.runtime.application.menu;

import de.cuioss.jsf.api.components.model.menu.NavigationMenuItem;
import de.cuioss.jsf.api.components.model.menu.NavigationMenuItemContainer;
import de.cuioss.jsf.api.components.model.menu.NavigationMenuItemSeparator;
import de.cuioss.portal.common.priority.PortalPriorities;
import de.cuioss.portal.configuration.PortalConfigurationKeys;
import de.cuioss.portal.configuration.types.ConfigAsFilteredMap;
import de.cuioss.portal.ui.api.menu.NavigationMenuProvider;
import de.cuioss.portal.ui.api.menu.PortalMenuItem;
import de.cuioss.tools.collect.CollectionBuilder;
import de.cuioss.tools.logging.CuiLogger;
import de.cuioss.tools.string.MoreStrings;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.io.Serial;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static de.cuioss.portal.configuration.PortalConfigurationKeys.MENU_BASE;
import static de.cuioss.tools.collect.CollectionLiterals.immutableList;
import static de.cuioss.tools.collect.CollectionLiterals.mutableList;
import static de.cuioss.tools.string.MoreStrings.isEmpty;

/**
 * Default implementation for {@link NavigationMenuProvider} that acts as a
 * registry for the navigation-menu. The algorithm:
 * <ul>
 * <li>Collect all instances of {@link NavigationMenuItem} that are annotated
 * with {@link PortalMenuItem}</li>
 * <li>Filter all elements with {@link NavigationMenuItem#getParentId()} is
 * {@value PortalConfigurationKeys#MENU_TOP_IDENTIFIER}: This are the
 * root-elements for top-level-menu</li>
 * <li>Add all other menu-items as child to the top-menu elements identified by
 * {@link NavigationMenuItem#getParentId()}</li>
 * </ul>
 *
 * @author Oliver Wolff
 */
@Priority(PortalPriorities.PORTAL_CORE_LEVEL)
@RequestScoped
@Named("navigationMenuProvider")
@EqualsAndHashCode(of = {"navigationMenuRoots"})
@ToString(of = {"navigationMenuRoots"})
public class NavigationMenuProviderImpl implements NavigationMenuProvider {

    @Serial
    private static final long serialVersionUID = 8780699386708876208L;
    private static final CuiLogger LOGGER = new CuiLogger(NavigationMenuProviderImpl.class);
    private final Map<String, List<NavigationMenuItem>> virtualParentContainer = new HashMap<>();
    @Inject
    @PortalMenuItem
    Instance<NavigationMenuItem> injectedItems;
    @Inject
    @ConfigAsFilteredMap(startsWith = MENU_BASE, stripPrefix = true)
    Map<String, String> menuConfig;
    @Getter
    private List<NavigationMenuItem> navigationMenuRoots = new ArrayList<>();
    /**
     * Defines all elements that are rendered.
     */
    private List<NavigationMenuItem> renderedMenuItems = Collections.emptyList();

    /**
     * Initializes the bean by collecting all elements specific to the navigation
     * menu.
     */
    @PostConstruct
    public void initBean() {
        determineMenuStructure();

        handleMenuSeparator();

        LOGGER.debug("sort top level container children");
        var iterator = navigationMenuRoots.listIterator();
        while (iterator.hasNext()) {
            var topLevelItem = iterator.next();
            if (topLevelItem instanceof NavigationMenuItemContainer container) {
                var renderedChildren = container.getChildren().size();
                if (renderedChildren > 0) {
                    Collections.sort(container.getChildren());
                } else {
                    iterator.remove();
                }
            }
        }

        LOGGER.debug("sort top level items");
        Collections.sort(navigationMenuRoots);

        navigationMenuRoots.forEach(navigationMenuItem -> {
            if (navigationMenuItem instanceof NavigationMenuItemContainer container) {
                var children = container.getChildren();
                while (children.get(children.size() - 1) instanceof NavigationMenuItemSeparator) {
                    children.remove(children.size() - 1);
                }
            }
        });
    }

    private void handleMenuSeparator() {
        List<String> handledIds = new ArrayList<>();
        for (final Map.Entry<String, String> menuConfigEntry : menuConfig.entrySet()) {
            if (menuConfigEntry.getKey().startsWith("separator") && menuConfigEntry.getKey().contains(".")) {
                var id = menuConfigEntry.getKey().split("\\.")[0];
                if (!handledIds.contains(id)) {
                    handledIds.add(id);
                    final var separatorMenuItem = new PortalNavigationMenuItemSeparatorImpl(id, menuConfig);
                    if (!separatorMenuItem.isRendered()) {
                        continue;
                    }
                    insertSeparatorMenuItem(separatorMenuItem);

                }
            }
        }
    }

    private void determineMenuStructure() {

        final List<NavigationMenuItem> workList = injectedItems.stream().filter(NavigationMenuItem::isRendered)
                .filter(item -> item.getOrder() > 0).collect(Collectors.toCollection(ArrayList::new));
        renderedMenuItems = immutableList(workList);
        LOGGER.debug("Rendered navigation-items: %s", renderedMenuItems);

        List<NavigationMenuItem> noNavigationMenuRoots = workList.stream()
                .filter(item -> MoreStrings.isEmpty(item.getParentId()))
                .collect(Collectors.toCollection(ArrayList::new));

        LOGGER.debug("Remove all un-rooted");
        workList.removeAll(noNavigationMenuRoots);

        LOGGER.debug("Filter NavigationMenu-Roots items");
        navigationMenuRoots = workList.stream()
                .filter(item -> PortalConfigurationKeys.MENU_TOP_IDENTIFIER.equals(item.getParentId()))
                .collect(Collectors.toCollection(ArrayList::new));

        LOGGER.debug("remove all roots");
        workList.removeAll(navigationMenuRoots);

        LOGGER.debug("Create a combined list with all NavigationMenuItemContainer that may contain a child");
        final var activeContainer = createCombinedContainerList(navigationMenuRoots, noNavigationMenuRoots);

        LOGGER.debug("Add sub items to top-level container items");
        var iterator = workList.listIterator();
        while (iterator.hasNext()) {
            var item = iterator.next();
            final var topLevelItem = activeContainer.stream()
                    .filter(candidate -> item.getParentId().equals(candidate.getId())).findFirst();
            if (topLevelItem.isPresent()) {
                topLevelItem.get().getChildren().add(item);
            } else if (virtualParentContainer.containsKey(item.getParentId())) {
                virtualParentContainer.get(item.getParentId()).add(item);
                Collections.sort(virtualParentContainer.get(item.getParentId()));
            } else {
                virtualParentContainer.put(item.getParentId(), mutableList(item));
            }

            iterator.remove();
        }

    }

    private List<NavigationMenuItemContainer> createCombinedContainerList(final List<NavigationMenuItem> roots,
                                                                          final List<NavigationMenuItem> noRoots) {
        final List<NavigationMenuItemContainer> result = new ArrayList<>();
        for (final NavigationMenuItem item : roots) {
            if (item instanceof NavigationMenuItemContainer container) {
                result.add(container);
            }
        }
        for (final NavigationMenuItem item : noRoots) {
            if (item instanceof NavigationMenuItemContainer container) {
                result.add(container);
            }
        }
        return result;
    }

    private void insertSeparatorMenuItem(final PortalNavigationMenuItemSeparatorImpl separatorMenuItem) {
        final var topLevelItem = getContainerMenuItemById(separatorMenuItem.getParentId());
        if (topLevelItem.isEmpty()) {
            return;
        }
        topLevelItem.get().getChildren().add(separatorMenuItem);
    }

    @Override
    public boolean isDisplayNavigationMenu() {
        return !navigationMenuRoots.isEmpty();
    }

    @Override
    public Optional<NavigationMenuItem> getMenuItemById(String id) {
        if (isEmpty(id)) {
            return Optional.empty();
        }
        return renderedMenuItems.stream().filter(item -> id.equalsIgnoreCase(item.getId())).findFirst();
    }

    @Override
    public List<NavigationMenuItem> getMenuItemsByIds(String... ids) {
        if (null == ids || ids.length == 0) {
            return Collections.emptyList();
        }
        var builder = new CollectionBuilder<NavigationMenuItem>();
        for (String id : ids) {
            var element = getMenuItemById(id);
            element.ifPresent(builder::add);
        }
        return builder.toImmutableList();
    }

    @Override
    public List<NavigationMenuItem> getMenuItemsByParentId(String parentId) {
        if (virtualParentContainer.containsKey(parentId)) {
            return virtualParentContainer.get(parentId);
        }
        return mutableList();
    }

    @Override
    public Optional<NavigationMenuItemContainer> getContainerMenuItemById(String id) {
        var item = getMenuItemById(id);
        if (item.isEmpty() || !(item.get() instanceof NavigationMenuItemContainer)) {
            return Optional.empty();
        }
        return Optional.of((NavigationMenuItemContainer) item.get());
    }
}
