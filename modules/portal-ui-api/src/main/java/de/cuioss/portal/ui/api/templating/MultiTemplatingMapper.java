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
package de.cuioss.portal.ui.api.templating;

import java.io.Serializable;
import java.net.URL;

/**
 * See package-info for description.
 *
 * @author Oliver Wolff
 */
public interface MultiTemplatingMapper extends Serializable {

    /**
     * @param requestedResource must not be null. Represents a concrete template
     *                          e.g. root.xhtml or subdirectory/component.xhtml
     *                          without the technical path segments
     * @return an instance of a {@link URL} to access the prefixed resource either
     * as external file or as classpath resource, e.g. portal/root.xhtml or
     * portal/subdirectory/component.xhtml respectively
     */
    URL resolveTemplatePath(String requestedResource);
}
