/*
 * Copyright 2023 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * https://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.cuioss.portal.ui.runtime.application.customization;

import de.cuioss.portal.ui.api.resources.CacheableResource;
import de.cuioss.tools.io.IOStreams;
import jakarta.faces.context.FacesContext;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;

/**
 * To allow caching of {@link CustomizationResource}s in case of
 * {@link de.cuioss.portal.common.stage.ProjectStage#PRODUCTION}
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
     * @param resource to be customized
     */
    public CachedCustomizationResource(final CustomizationResource resource) {
        super.setResourceName(resource.getResourceName());
        super.setLibraryName(resource.getLibraryName());
        super.setContentType(resource.getContentType());
        eTag = resource.getETag();
        responseHeaders = resource.getResponseHeaders();
        byte[] resourceContent = {0};
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
