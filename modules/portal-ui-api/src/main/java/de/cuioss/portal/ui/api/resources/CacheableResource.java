package de.cuioss.portal.ui.api.resources;

import java.util.HashMap;
import java.util.Map;

import javax.faces.application.ResourceHandler;
import javax.faces.context.FacesContext;

/**
 * Abstract resource allowing basic caching functionality.
 *
 * @author Matthias Walliczek
 */
public abstract class CacheableResource extends CuiResource {

    private static final String HEADER_E_TAG = "ETag";

    private static final String HEADER_IF_NONE_MATCH = "If-None-Match";

    /**
     * Generate a unique string for the current resource file version.
     *
     * @return a string unique for the current resource file version.
     */
    protected abstract String getETag();

    @Override
    public Map<String, String> getResponseHeaders() {
        final Map<String, String> responseHeaders = new HashMap<>();
        if (getETag() != null) {
            responseHeaders.put(HEADER_E_TAG, getETag());
        }
        return responseHeaders;
    }

    @Override
    public boolean userAgentNeedsUpdate(final FacesContext context) {
        final var requestHeaders = context.getExternalContext().getRequestHeaderMap();

        return (!requestHeaders.containsKey(HEADER_IF_NONE_MATCH) || !getETag().equals(requestHeaders.get(HEADER_IF_NONE_MATCH)));
    }

    /**
     * Create resource path to be appended after context path
     */
    protected String determineResourcePath() {
        final var path = new StringBuilder(ResourceHandler.RESOURCE_IDENTIFIER);
        path.append('/');
        path.append(getLibraryName());
        path.append('/');
        path.append(getResourceName());
        return path.toString();
    }
}
