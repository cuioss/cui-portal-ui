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

import jakarta.faces.component.UIComponent;
import jakarta.faces.component.html.HtmlOutputText;

import org.junit.jupiter.api.Test;

import de.cuioss.jsf.api.components.html.HtmlTreeBuilder;
import de.cuioss.jsf.api.components.html.Node;
import de.cuioss.portal.ui.components.PortalCssClasses;
import de.cuioss.test.jsf.renderer.AbstractComponentRendererTest;

class SidebarComponentRendererTest extends AbstractComponentRendererTest<SidebarComponentRenderer> {

    @Test
    void shouldRenderMinimal() {
        var component = new SidebarComponent();
        var expected = new HtmlTreeBuilder().withNode(Node.NAV).withStyleClass(PortalCssClasses.SIDEBAR);
        assertRenderResult(component, expected.getDocument());
    }

    @Test
    void shouldRenderWithChildren() {
        var component = new SidebarComponent();
        component.getChildren().add(new HtmlOutputText());
        getComponentConfigDecorator().registerMockRendererForHtmlOutputText();
        var expected = new HtmlTreeBuilder().withNode(Node.NAV).withStyleClass(PortalCssClasses.SIDEBAR)
                .withNode("HtmlOutputText");
        assertRenderResult(component, expected.getDocument());
    }

    @Override
    protected UIComponent getComponent() {
        return new SidebarComponent();
    }
}
