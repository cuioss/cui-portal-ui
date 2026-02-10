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

import de.cuioss.jsf.api.components.model.menu.NavigationMenuItemExternalSingle;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;

/**
 * @author Oliver Wolff
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class PortalNavigationMenuItemExternalSingleImpl extends PortalNavigationMenuItemImplBase
        implements NavigationMenuItemExternalSingle {

    @Serial
    private static final long serialVersionUID = -1489949340663388532L;

    @Getter
    @Setter
    private String hRef;

    @Getter
    @Setter
    private String target;

    @Getter
    @Setter
    private String onClickAction;
}
