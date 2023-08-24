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
package de.cuioss.portal.ui.runtime.application.templating.support;

import static de.cuioss.tools.collect.CollectionLiterals.immutableList;

import java.util.List;

import javax.annotation.Priority;
import javax.inject.Named;

import de.cuioss.portal.configuration.common.PortalPriorities;
import de.cuioss.portal.ui.api.templating.PortalTemplateDescriptor;
import de.cuioss.portal.ui.api.templating.StaticTemplateDescriptor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Defines the Portal defined templates
 *
 * @author Oliver Wolff
 */
@PortalTemplateDescriptor
@Named
@Priority(PortalPriorities.PORTAL_MODULE_LEVEL)
@EqualsAndHashCode
@ToString
public class AdditionalTemplates implements StaticTemplateDescriptor {

    private static final long serialVersionUID = 1933293647595996193L;

    @Getter
    private final List<String> handledTemplates = immutableList("root.xhtml", "module.xhtml");

    @Getter
    private final String templatePath = "additional";

}
