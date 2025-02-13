package de.cuioss.portal.ui.runtime.application.resources;

import de.cuioss.test.jsf.junit5.EnableJsfEnvironment;
import de.cuioss.test.jsf.mocks.CuiMockResourceHandler;
import de.cuioss.test.jsf.util.JsfEnvironmentConsumer;
import de.cuioss.test.jsf.util.JsfEnvironmentHolder;
import lombok.Getter;
import lombok.Setter;
import lombok.val;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

@EnableJsfEnvironment
class PortalViewResourceHandlerTest implements JsfEnvironmentConsumer {

    public static final String HELLO_WORLD_XHTML = "/hello/world.xhtml";
    public static final String NOT_THERE_XHTML = "/notThere.xhtml";
    @Setter
    @Getter
    private JsfEnvironmentHolder environmentHolder;

    private final CuiMockResourceHandler resourceHandler = new CuiMockResourceHandler();

    @Test
    void shouldIgnoreDefaultAndTemplates() {
        PortalViewResourceHandler underTest = new PortalViewResourceHandler(resourceHandler);
        assertEquals(CuiMockResourceHandler.DUMMY_URL + "/", underTest.createViewResource(getFacesContext(), "/").getURL().toString());
        assertEquals(CuiMockResourceHandler.DUMMY_URL + "/templates/hey", underTest.createViewResource(getFacesContext(), "/templates/hey").getURL().toString());
    }

    @Test
    void shouldHandleAvailableViews() {
        PortalViewResourceHandler underTest = new PortalViewResourceHandler(resourceHandler);
        final var result = underTest.createViewResource(getFacesContext(), HELLO_WORLD_XHTML);
        assertInstanceOf(PortalViewResourceHolder.class, result);
        assertTrue(result.getURL().toString().endsWith(PortalViewResourceHandler.LOOKUP_PREFIX + HELLO_WORLD_XHTML), result.getURL().toString());
        final var cached = underTest.createViewResource(getFacesContext(), HELLO_WORLD_XHTML);
        assertEquals(result, cached);
        assertSame(result, cached, "Should be the same instance");
    }

    @Test
    void shouldIgnoreMissingViews() {
        PortalViewResourceHandler underTest = new PortalViewResourceHandler(resourceHandler);
        final var result = underTest.createViewResource(getFacesContext(), NOT_THERE_XHTML);
        assertFalse(result instanceof PortalViewResourceHolder);
        assertEquals(CuiMockResourceHandler.DUMMY_URL + NOT_THERE_XHTML, result.getURL().toString());
        final var cached = underTest.createViewResource(getFacesContext(), NOT_THERE_XHTML);
        assertEquals(CuiMockResourceHandler.DUMMY_URL + NOT_THERE_XHTML, cached.getURL().toString());
    }
}