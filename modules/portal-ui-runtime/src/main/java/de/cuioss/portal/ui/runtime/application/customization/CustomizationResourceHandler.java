/*
 * Copyright © 2025 CUI-OpenSource-Software (info@cuioss.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.cuioss.portal.ui.runtime.application.customization;

import de.cuioss.portal.common.cdi.PortalBeanManager;
import jakarta.faces.application.Resource;
import jakarta.faces.application.ResourceHandler;
import jakarta.faces.application.ResourceHandlerWrapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Allows overriding specific resources at the installation. Delegates to {@link ResourceProducer}.
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
    public Resource createResource(final String resourceName, final String libraryName) {
        final ResourceProducer producer = PortalBeanManager.resolveBean(ResourceProducer.class,
                PortalResourceProducer.class)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Unable to resolve bean of type '%s' with qualifier '%s'".formatted(
                                ResourceProducer.class.getName(), PortalResourceProducer.class.getName())));

        var result = producer.retrieveResource(resourceName, libraryName);
        if (null != result) {
            return result;
        }

        return super.createResource(resourceName, libraryName);
    }

}
