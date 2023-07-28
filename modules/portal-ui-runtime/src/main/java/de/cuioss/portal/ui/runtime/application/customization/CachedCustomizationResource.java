package de.cuioss.portal.ui.runtime.application.customization;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;

import javax.faces.context.FacesContext;

import de.cuioss.portal.ui.api.resources.CacheableResource;
import de.cuioss.tools.io.IOStreams;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * To allow caching of {@link CustomizationResource}s in case of
 * {@link ProjectStage#PRODUCTION}
 *
 * @author Matthias Walliczek
 */
@ToString(callSuper = true, of = "eTag", doNotUseGetters = true)
@EqualsAndHashCode(callSuper = true, of = "eTag", doNotUseGetters = true)
public class CachedCustomizationResource extends CacheableResource {

    @Getter
    private final String eTag;

    @Getter
    private final Map<String, String> responseHeaders;

    private final byte[] content;
    private final IOException contentException;
    private final URL url;

    /**
     * Constructor to create a new cached instance of the given resource. Will try
     * to retrieve the content and persist it.
     *
     * @param resource
     */
    public CachedCustomizationResource(final CustomizationResource resource) {
        super.setResourceName(resource.getResourceName());
        super.setLibraryName(resource.getLibraryName());
        super.setContentType(resource.getContentType());
        eTag = resource.getETag();
        responseHeaders = resource.getResponseHeaders();
        byte[] resourceContent = { 0 };
        IOException exceptionOccursOnRead = null;
        try {
            resourceContent = IOStreams.toByteArray(resource.getInputStream());
        } catch (final IOException e) {
            exceptionOccursOnRead = e;
        }
        contentException = exceptionOccursOnRead;
        content = resourceContent;
        url = resource.getURL();
    }

    @Override
    public InputStream getInputStream() throws IOException {
        if (null != contentException) {
            throw contentException;
        }
        return new ByteArrayInputStream(content);
    }

    @Override
    public String getRequestPath() {
        final var context = FacesContext.getCurrentInstance();
        return context.getApplication().getViewHandler().getResourceURL(context, super.determineResourcePath());
    }

    @Override
    public URL getURL() {
        return url;
    }
}
