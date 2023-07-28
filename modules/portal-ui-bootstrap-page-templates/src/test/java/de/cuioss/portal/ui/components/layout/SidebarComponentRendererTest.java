package de.cuioss.portal.ui.components.layout;

import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlOutputText;

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
