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

import de.cuioss.portal.common.cdi.PortalBeanManager;
import de.cuioss.portal.configuration.PortalConfigurationKeys;
import de.cuioss.portal.ui.api.resources.CacheableResource;
import de.cuioss.tools.logging.CuiLogger;
import de.cuioss.uimodel.application.CuiProjectStage;
import jakarta.faces.context.FacesContext;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.TimeZone;

import static de.cuioss.portal.configuration.util.ConfigurationHelper.resolveConfigProperty;

/**
 * Implementation of a {@link File} based {@link CacheableResource}.
 *
 * @author Matthias Walliczek
 */
@ToString(of = "resourceFile")
@EqualsAndHashCode(of = "resourceFile", callSuper = false)
public final class CustomizationResource extends CacheableResource {

    private static final CuiLogger LOGGER = new CuiLogger(CustomizationResource.class);

    private static final String HEADER_LAST_MODIFIED = "Last-Modified";
    private static final String HEADER_EXPIRES = "Expires";
    private static final String RFC1123_DATE_PATTERN = "EEE, dd MMM yyyy HH:mm:ss zzz";
    private final TimeZone gmt = TimeZone.getTimeZone("GMT");

    private final File resourceFile;

    public CustomizationResource(final File resourceFile, final String resourceName, final String libraryName,
            final String contentType) {
        this.resourceFile = resourceFile;
        setResourceName(resourceName);
        setLibraryName(libraryName);
        setContentType(contentType);
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new FileInputStream(resourceFile);
    }

    @Override
    public Map<String, String> getResponseHeaders() {
        final var responseHeaders = super.getResponseHeaders();
        final var format = new SimpleDateFormat(RFC1123_DATE_PATTERN, Locale.US);
        format.setTimeZone(gmt);
        final long expiresTime;
        final Optional<CuiProjectStage> projectStageBean = PortalBeanManager.resolveBean(CuiProjectStage.class, null);
        if (projectStageBean.isPresent() && projectStageBean.get().isDevelopment()) {
            expiresTime = new Date().getTime();
        } else {
            final var maxAge = Integer.parseInt(
                    resolveConfigProperty(PortalConfigurationKeys.RESOURCE_MAXAGE).orElse("10080")) * 60L * 1000L;
            expiresTime = new Date().getTime() + maxAge;
        }
        responseHeaders.put(HEADER_EXPIRES, format.format(new Date(expiresTime)));
        responseHeaders.put(HEADER_LAST_MODIFIED, format.format(new Date(resourceFile.lastModified())));

        return responseHeaders;
    }

    @Override
    protected String getETag() {
        return "W/\"" + resourceFile.length() + '-' + resourceFile.lastModified() + '"';
    }

    @Override
    public String getRequestPath() {
        final var context = FacesContext.getCurrentInstance();
        return context.getApplication().getViewHandler().getResourceURL(context, super.determineResourcePath());
    }

    @Override
    public URL getURL() {
        try {
            return resourceFile.toURI().toURL();
        } catch (final MalformedURLException e) {
            LOGGER.warn(e, "Portal-145: Customization resource '%s' can not be resolved to an URL", resourceFile);
            throw new IllegalStateException(e);
        }
    }

}
