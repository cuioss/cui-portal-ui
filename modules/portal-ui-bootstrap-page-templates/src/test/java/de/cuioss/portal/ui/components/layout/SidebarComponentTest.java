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
