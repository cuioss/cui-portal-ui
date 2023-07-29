package de.cuioss.portal.ui.api.resources;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;

import javax.faces.context.FacesContext;

import org.junit.jupiter.api.Test;

import de.cuioss.test.jsf.junit5.EnableJsfEnvironment;
import de.cuioss.test.valueobjects.junit5.contracts.ShouldBeNotNull;
import de.cuioss.test.valueobjects.junit5.contracts.ShouldImplementEqualsAndHashCode;

@EnableJsfEnvironment
class CuiResourceTest implements ShouldBeNotNull<CuiResource>, ShouldImplementEqualsAndHashCode<CuiResource> {

    @Override
    public CuiResource getUnderTest() {
        var resource = new CuiResource() {

            @Override
            public boolean userAgentNeedsUpdate(FacesContext context) {
                return false;
            }

            @Override
            public URL getURL() {
                return null;
            }

            @Override
            public Map<String, String> getResponseHeaders() {
                return null;
            }

            @Override
            public InputStream getInputStream() throws IOException {
                return null;
            }
        };
        resource.setLibraryName("library");
        resource.setResourceName("resource");
        return resource;
    }

    @Test
    void shouldComputeRequestPath() {
        assertEquals("null/javax.faces.resource/resource?ln=library", getUnderTest().getRequestPath());
    }

}
