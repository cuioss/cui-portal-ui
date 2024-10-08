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

import jakarta.faces.application.Resource;

/**
 * To be used by {@link CustomizationResourceHandler}.
 *
 * @author Matthias Walliczek
 */
public interface ResourceProducer {

    /**
     * @param resourceName
     * @param libraryName
     * @return the resource with given resourceName and libraryName if found,
     * otherwise null
     */
    Resource retrieveResource(String resourceName, String libraryName);

}
