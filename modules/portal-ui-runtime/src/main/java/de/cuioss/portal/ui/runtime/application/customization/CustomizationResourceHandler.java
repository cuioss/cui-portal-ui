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

import java.util.Optional;

import jakarta.faces.application.Resource;
import jakarta.faces.application.ResourceHandler;
import jakarta.faces.application.ResourceHandlerWrapper;

import de.cuioss.portal.common.cdi.PortalBeanManager;
import de.cuioss.tools.base.Preconditions;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Allows to override specific resources at the installation. Delegates to
 * {@link ResourceProducer}.
 *
 * @author Matthias Walliczek
 */
@RequiredArgsConstructor
public class CustomizationResourceHandler extends ResourceHandlerWrapper {

    /**
     * Wrapped resource handler.
     */
    @Getter
    private final ResourceHandler wrapped;

    @Override
    @SuppressWarnings("java:S3655") // owolff: False Positive, isPresent is checked properly
    public Resource createResource(final String resourceName, final String libraryName) {
        final Optional<ResourceProducer> resourceProducer = PortalBeanManager.resolveBean(ResourceProducer.class,
                PortalResourceProducer.class);

        Preconditions.checkArgument(resourceProducer.isPresent(),
                PortalBeanManager.createLogMessage(ResourceProducer.class, PortalResourceProducer.class));

        var result = resourceProducer.get().retrieveResource(resourceName, libraryName);
        if (null != result) {
            return result;
        }

        return super.createResource(resourceName, libraryName);
    }

}
