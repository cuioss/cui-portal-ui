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

import de.cuioss.portal.ui.api.menu.PortalMenuItem;
import de.cuioss.portal.ui.api.menu.PortalNavigationMenuItemContainerImpl;

import java.io.Serial;

@PortalMenuItem
public class EmptyNavigationContainer extends PortalNavigationMenuItemContainerImpl {

    public static final String LABEL_KEY = "portal.runtime.configuration.settings.menu.label";
    public static final String ICON = "cui-icon-settings";
    /**
     * The string based id for this menu item.
     */
    public static final String MENU_ID = "emptyMenuItem";
    @Serial
    private static final long serialVersionUID = 5073024579976343083L;

    public EmptyNavigationContainer() {
        super.setIconStyleClass(ICON);
        super.setId(MENU_ID);
        super.setLabelKey(LABEL_KEY);
    }

}
