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
package de.cuioss.portal.ui.components.layout;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import de.cuioss.jsf.api.components.html.Node;
import de.cuioss.portal.ui.components.PortalCssClasses;
import de.cuioss.portal.ui.components.PortalFamily;
import de.cuioss.test.jsf.component.AbstractUiComponentTest;

class SidebarComponentTest extends AbstractUiComponentTest<SidebarComponent> {

    @Test
    void shouldProvideCorrectStyleClass() {
        assertEquals(PortalCssClasses.SIDEBAR.getStyleClass(), anyComponent().resolveStyleClass().getStyleClass());
    }

    @Test
    void shouldDefaultToNav() {
        assertEquals(Node.NAV, anyComponent().resolveHtmlElement());
    }

    @Test
    void shouldProvideCorrectMetadata() {
        assertEquals(PortalFamily.PORTAL_FAMILY, anyComponent().getFamily());
        assertEquals(PortalFamily.SIDEBAR_RENDERER, anyComponent().getRendererType());
    }

}
