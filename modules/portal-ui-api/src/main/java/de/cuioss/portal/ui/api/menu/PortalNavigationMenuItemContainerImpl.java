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
package de.cuioss.portal.ui.api.menu;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

import de.cuioss.jsf.api.components.model.menu.NavigationMenuItem;
import de.cuioss.jsf.api.components.model.menu.NavigationMenuItemContainer;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Oliver Wolff
 *
 */
@EqualsAndHashCode(callSuper = true)
public class PortalNavigationMenuItemContainerImpl extends PortalNavigationMenuItemImplBase
        implements NavigationMenuItemContainer {

    @Serial
    private static final long serialVersionUID = 2451583664984874108L;

    @Getter
    @Setter
    private List<NavigationMenuItem> children = new ArrayList<>();

}
