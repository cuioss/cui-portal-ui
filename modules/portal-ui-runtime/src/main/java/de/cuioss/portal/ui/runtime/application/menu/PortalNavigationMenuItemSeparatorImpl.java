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
import de.cuioss.jsf.api.components.model.menu.NavigationMenuItemSeparator;
import de.cuioss.portal.ui.api.menu.PortalNavigationMenuConfigParser;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.Serial;
import java.util.Map;

/**
 * @author Oliver Wolff
 *
 */
@RequiredArgsConstructor
public class PortalNavigationMenuItemSeparatorImpl extends PortalNavigationMenuConfigParser
        implements NavigationMenuItemSeparator {

    @Serial
    private static final long serialVersionUID = -4539137375412075634L;

    @Getter
    private final String id;

    @Getter(value = AccessLevel.PROTECTED)
    private final Map<String, String> menuConfig;

    @Override
    public String getResolvedTitle() {
        return null;
    }

    @Override
    public String getTitleKey() {
        return null;
    }

    @Override
    public String getTitleValue() {
        return null;
    }

    @Override
    public boolean isTitleAvailable() {
        return false;
    }

    @Override
    public String getIconStyleClass() {
        return null;
    }

    @Override
    public int compareTo(final NavigationMenuItem other) {
        return getOrder().compareTo(other.getOrder());
    }
}
