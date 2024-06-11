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
package de.cuioss.portal.ui.runtime.application.resources;

import jakarta.faces.application.Resource;
import jakarta.faces.application.ResourceHandler;
import jakarta.faces.application.ResourceHandlerWrapper;
import jakarta.faces.application.ResourceWrapper;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Resource Handle does adapt the request ulr for any resource covered by
 * {@link #shouldHandleRequestedResource(String, String)} by adding a current
 * module version to a resource path
 */
@RequiredArgsConstructor
public abstract class AbstractVersionResourceHandler extends ResourceHandlerWrapper {

    @Getter
    private final ResourceHandler wrapped;

    @Override
    public Resource createResource(final String resourceName, final String libraryName) {
        if (shouldHandleRequestedResource(resourceName, libraryName)) {
            return modifiedResource(getWrapped().createResource(resourceName, libraryName));
        }
        return getWrapped().createResource(resourceName, libraryName);
    }

    /**
     * provide module-specific decision if the requested resource should get handled
     * by this resource handler
     */
    protected abstract boolean shouldHandleRequestedResource(String resourceName, String libraryName);

    /**
     * method should provide project-specific version info for module
     */
    protected abstract String getNewResourceVersion();

    /**
     * Add version parameter at the end of the request path from JSF resource.
     */
    private Resource modifiedResource(final Resource wrappedResource) {

        if (wrappedResource == null) {
            return null;
        }

        return new ResourceWrapper(wrappedResource) {

            @Override
            public String getRequestPath() {
                if (super.getRequestPath().contains("&v=")) {
                    return super.getRequestPath();
                }
                return super.getRequestPath() + "&v=" + getNewResourceVersion();
            }

            @Override
            public Resource getWrapped() {
                return wrappedResource;
            }
        };
    }
}
