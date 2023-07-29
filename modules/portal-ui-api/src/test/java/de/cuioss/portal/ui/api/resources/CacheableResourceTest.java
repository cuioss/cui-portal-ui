package de.cuioss.portal.ui.api.resources;

import static de.cuioss.portal.ui.api.resources.CacheableResource.HEADER_IF_NONE_MATCH;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.cuioss.test.generator.Generators;
import de.cuioss.test.jsf.junit5.EnableJsfEnvironment;
import de.cuioss.test.jsf.util.JsfEnvironmentConsumer;
import de.cuioss.test.jsf.util.JsfEnvironmentHolder;
import de.cuioss.test.valueobjects.junit5.contracts.ShouldBeNotNull;
import de.cuioss.test.valueobjects.junit5.contracts.ShouldImplementEqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EnableJsfEnvironment
class CacheableResourceTest implements ShouldBeNotNull<CacheableResource>,
        ShouldImplementEqualsAndHashCode<CacheableResource>, JsfEnvironmentConsumer {

    @Setter
    @Getter
    private JsfEnvironmentHolder environmentHolder;

    private String etag;

    @BeforeEach
    void resetEtag() {
        etag = Generators.letterStrings(2, 10).next();
    }

    @Override
    public CacheableResource getUnderTest() {
        var resource = new CacheableResource() {

            @Override
            public URL getURL() {
                return null;
            }

            @Override
            public InputStream getInputStream() throws IOException {
                return null;
            }

            @Override
            protected String getETag() {
                return etag;
            }
        };
        resource.setLibraryName("library");
        resource.setResourceName("resource");
        return resource;
    }

    @Test
    void shouldDetectWheterUANeedsNoUpdate() {
        var resource = getUnderTest();
        assertTrue(resource.userAgentNeedsUpdate(environmentHolder.getFacesContext()));

        environmentHolder.getRequestConfigDecorator().setRequestHeader(HEADER_IF_NONE_MATCH, HEADER_IF_NONE_MATCH);
        assertTrue(resource.userAgentNeedsUpdate(environmentHolder.getFacesContext()));

    }

    @Test
    void shouldDetectWheterUANeedsUpdate() {
        var resource = getUnderTest();

        environmentHolder.getRequestConfigDecorator().setRequestHeader(HEADER_IF_NONE_MATCH, etag);
        assertFalse(resource.userAgentNeedsUpdate(environmentHolder.getFacesContext()));

    }

    @Test
    void shoultHandleEtagIfPresent() {
        var resource = getUnderTest();
        var header = resource.getResponseHeaders();
        assertTrue(header.containsKey(CacheableResource.HEADER_E_TAG));
        assertTrue(header.containsValue(etag));

    }

    @Test
    void shouldDetermineresourcePath() {
        assertEquals("/javax.faces.resource/library/resource", getUnderTest().determineResourcePath());
    }

    @Test
    void shoultHandleEtagIfMotPresent() {
        var resource = new CacheableResource() {

            @Override
            public URL getURL() {
                return null;
            }

            @Override
            public InputStream getInputStream() throws IOException {
                return null;
            }

            @Override
            protected String getETag() {
                return null;
            }
        };

        var header = resource.getResponseHeaders();
        assertFalse(header.containsKey(CacheableResource.HEADER_E_TAG));
        assertFalse(header.containsValue(etag));
    }
}