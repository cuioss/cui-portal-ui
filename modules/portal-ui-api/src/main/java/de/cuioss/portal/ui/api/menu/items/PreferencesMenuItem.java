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
package de.cuioss.portal.ui.api.menu.items;

import jakarta.enterprise.context.Dependent;

import de.cuioss.portal.ui.api.menu.PortalMenuItem;
import de.cuioss.portal.ui.api.menu.PortalNavigationMenuItemSingleImpl;
import de.cuioss.portal.ui.api.ui.pages.PreferencesPage;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serial;

/**
 * <p>
 * Default representation of the preferences menu-item. The order is 20. It
 * references {@link UserMenuItem#MENU_ID} as parentId and
 * {@link PreferencesPage#OUTCOME} as outcome.
 * </p>
 *
 * @author Oliver Wolff
 */
@PortalMenuItem
@Dependent
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class PreferencesMenuItem extends PortalNavigationMenuItemSingleImpl {

    @Serial
    private static final long serialVersionUID = 1452093785009425867L;

    /** The label Key for this component. */
    public static final String LABEL_KEY = "cui.commons.portal.menu.preferences.label";

    /** The title Key for this component. */
    public static final String TITLE_KEY = "cui.commons.portal.menu.preferences.title";

    /** The icon for this component. */
    public static final String ICON = "cui-icon-settings";

    /** The string based id for this menu item. */
    public static final String MENU_ID = "preferencesMenuItem";

    /**
     * Constructor.
     */
    public PreferencesMenuItem() {
        super.setIconStyleClass(ICON);
        super.setId(MENU_ID);
        super.setLabelKey(LABEL_KEY);
        super.setTitleKey(TITLE_KEY);
        super.setOutcome(PreferencesPage.OUTCOME);
    }
}
