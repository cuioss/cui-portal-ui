package de.cuioss.portal.ui.api.resources;

import java.util.HashMap;
import java.util.Map;

import javax.faces.context.FacesContext;

/**
 * Abstract resource disabling caching.
 *
 * @author Matthias Walliczek
 */
public abstract class NonCachableResource extends CuiResource {

    private static final String HEADER_ACCEPT = "Accept";
    private static final String HEADER_CACHE_CONTROL = "Cache-Control";

    @Override
    public Map<String, String> getResponseHeaders() {
        Map<String, String> responseHeaders = new HashMap<>();
        responseHeaders.put(HEADER_ACCEPT, "public");
        responseHeaders.put(HEADER_CACHE_CONTROL, "max-age=0");
        return responseHeaders;
    }

    @Override
    public boolean userAgentNeedsUpdate(final FacesContext context) {
        return true;
    }

}
