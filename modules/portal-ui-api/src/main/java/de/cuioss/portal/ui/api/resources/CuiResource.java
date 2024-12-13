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
package de.cuioss.portal.ui.api.resources;

import jakarta.faces.application.Resource;
import jakarta.faces.application.ResourceHandler;
import jakarta.faces.context.FacesContext;
import lombok.ToString;

import java.util.Objects;

/**
 * Base Resource using
 * {{@link #calculateRequestPath(String, String, FacesContext)}} to calculate
 * {@link #getRequestPath()} and {@link #getURL()}.
 *
 * @author Matthias Walliczek
 */
@ToString
public abstract class CuiResource extends Resource {

    /**
     * Calculate the request path to a given resource to be used a link target.
     *
     * @param resourceName identifying the resource
     * @param libraryName  identifying the library
     * @param context      The current {@link FacesContext}
     * @return the request path (relative, without host name)
     */
    public static String calculateRequestPath(String resourceName, String libraryName, FacesContext context) {
        String uri;

        uri = ResourceHandler.RESOURCE_IDENTIFIER + '/' + resourceName;
        if (null != libraryName) {
            uri += "?ln=" + libraryName;
        }
        return context.getApplication().getViewHandler().getResourceURL(context, uri);
    }

    @Override
    public String getRequestPath() {
        return calculateRequestPath(getResourceName(), getLibraryName(), FacesContext.getCurrentInstance());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getResourceName(), getLibraryName());
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof final CuiResource other)
            return Objects.equals(getResourceName(), other.getResourceName())
                    && Objects.equals(getLibraryName(), other.getLibraryName());
        return false;
    }
}
