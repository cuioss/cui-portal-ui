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
import java.util.List;

/**
 * Utilized for statically extending the default {@link MultiTemplatingMapper}
 * defined by cui-portal-core-cdi-impl. Provides information which template are
 * to be handled by which concrete Template-Directory, see package-info for
 * details.
 *
 * @author Oliver Wolff
 */
public interface StaticTemplateDescriptor extends Serializable {

    /**
     * @return a List of names of the templates to be handles by this concrete
     *         descriptor.
     */
    List<String> getHandledTemplates();

    /**
     * @return the name of the Template-Directory the templates within this
     *         descriptor belong to. It must not end with '/'
     */
    String getTemplatePath();
}
