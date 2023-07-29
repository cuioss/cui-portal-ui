package de.cuioss.portal.ui.api.resources;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.faces.context.FacesContext;

import org.junit.jupiter.api.Test;

import de.cuioss.test.valueobjects.junit5.contracts.ShouldBeNotNull;
import de.cuioss.test.valueobjects.junit5.contracts.ShouldImplementEqualsAndHashCode;

class NonCachableResourceTest
        implements ShouldBeNotNull<NonCachableResource>, ShouldImplementEqualsAndHashCode<NonCachableResource> {

    @Override
    public NonCachableResource getUnderTest() {
        var resource = new NonCachableResource() {

            @Override
            public InputStream getInputStream() throws IOException {
                return null;
            }

            @Override
            public URL getURL() {
                return null;
            }

        };
        return resource;
    }

    @Test
    void shouldAddNoCacheHeader() {
        var resource = getUnderTest();
        assertTrue(resource.userAgentNeedsUpdate(FacesContext.getCurrentInstance()));

        var header = resource.getResponseHeaders();
        assertTrue(header.containsKey(NonCachableResource.HEADER_ACCEPT));
        assertTrue(header.containsKey(NonCachableResource.HEADER_CACHE_CONTROL));

        assertTrue(header.containsValue(NonCachableResource.PUBLIC));
        assertTrue(header.containsValue(NonCachableResource.MAX_AGE_0));
    }

}
