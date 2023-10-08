package de.cuioss.portal.ui.runtime.support;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;
import javax.faces.application.ResourceHandlerWrapper;

public class ResourceHandlerMock extends ResourceHandlerWrapper {

    private String resourceName;
    private String libraryName;

    public ResourceHandlerMock(ResourceHandler resourceHandler) {
        super(resourceHandler);
    }

    @Override
    public Resource createResource(String resourceName, String libraryName) {
        this.resourceName = resourceName;
        this.libraryName = libraryName;
        return super.createResource(resourceName, libraryName);
    }

    public void assertResourceCreated(String expectedResourceName, String expectedLibraryName) {
        assertEquals(expectedResourceName, resourceName);
        assertEquals(expectedLibraryName, libraryName);
    }

    public void assertNoResourceCreated() {
        assertNull(resourceName);
        assertNull(libraryName);
    }
}
