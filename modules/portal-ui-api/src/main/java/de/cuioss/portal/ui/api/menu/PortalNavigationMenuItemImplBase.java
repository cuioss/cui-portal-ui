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
import de.cuioss.jsf.api.components.support.LabelResolver;
import de.cuioss.portal.configuration.types.ConfigAsFilteredMap;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static de.cuioss.portal.configuration.PortalConfigurationKeys.MENU_BASE;

/**
 * Base implementation for any cdi portal based {@link NavigationMenuItem}
 * implementation.
 */
@EqualsAndHashCode(callSuper = true)
public class PortalNavigationMenuItemImplBase extends PortalNavigationMenuConfigParser implements NavigationMenuItem {

    @Serial
    private static final long serialVersionUID = -7137939377092965593L;

    @Inject
    @ConfigAsFilteredMap(startsWith = MENU_BASE, stripPrefix = true)
    @Getter(value = AccessLevel.PROTECTED)
    Map<String, String> menuConfig;

    @Getter
    @Setter
    private String id;

    @Getter
    @Setter
    private String iconStyleClass;

    @Getter
    @Setter
    private String titleKey;

    @Getter
    @Setter
    private String titleValue;

    @Getter
    @Setter
    private List<String> activeForAdditionalViewId = new ArrayList<>();
    @Getter
    @Setter
    private String labelKey;
    @Getter
    @Setter
    private String labelValue;

    @Override
    public int compareTo(final NavigationMenuItem other) {
        return getOrder().compareTo(other.getOrder());
    }

    @Override
    public String getResolvedTitle() {
        return getResolvedLabel();
    }

    @Override
    public boolean isTitleAvailable() {
        return null != getResolvedTitle();
    }

    /**
     * @return the resolved label
     */
    public String getResolvedLabel() {
        return LabelResolver.builder().withLabelKey(labelKey).withLabelValue(labelValue).build()
                .resolve(FacesContext.getCurrentInstance());
    }

}
