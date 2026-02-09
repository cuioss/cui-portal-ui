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
package de.cuioss.portal.ui.runtime.application.templating;

import de.cuioss.portal.ui.api.templating.PortalMultiTemplatingMapper;
import de.cuioss.portal.ui.runtime.application.templating.support.AdditionalTemplates;
import de.cuioss.test.valueobjects.junit5.contracts.ShouldHandleObjectContracts;
import jakarta.inject.Inject;
import lombok.Getter;
import org.jboss.weld.junit5.auto.AddBeanClasses;
import org.jboss.weld.junit5.auto.EnableAutoWeld;
import org.junit.jupiter.api.Test;

import static de.cuioss.portal.ui.runtime.application.templating.PortalTemplateMapperSimpleTest.MASTER_DEFAULT;
import static de.cuioss.portal.ui.runtime.application.templating.PortalTemplateMapperSimpleTest.MASTER_HORIZONTAL_CENTER;
import static de.cuioss.portal.ui.runtime.application.templating.PortalTemplateMapperSimpleTest.NOT_THERE;
import static de.cuioss.portal.ui.runtime.application.templating.PortalTemplateMapperSimpleTest.PORTAL;
import static de.cuioss.portal.ui.runtime.application.templating.PortalTemplateMapperSimpleTest.ROOT;
import static de.cuioss.portal.ui.runtime.application.templating.PortalTemplateMapperSimpleTest.TECHNICAL_ROOT;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@EnableAutoWeld
@AddBeanClasses({PortalCoreTemplates.class, AdditionalTemplates.class})
class PortalTemplateMapperExtendedTest implements ShouldHandleObjectContracts<PortalTemplateMapper> {

    public static final String ADDITIONAL_PROVIDER = "additional/";

    public static final String ADDITIONAL_TEMPLATE = "module.xhtml";

    @Inject
    @PortalMultiTemplatingMapper
    @Getter
    private PortalTemplateMapper underTest;

    @Test
    void shouldInitCorrectly() {
        // To be controlled through PortalCoreTemplates
        assertTrue(underTest.resolveTemplatePath(TECHNICAL_ROOT).getPath().endsWith(PORTAL + TECHNICAL_ROOT));
        assertTrue(underTest.resolveTemplatePath(MASTER_DEFAULT).getPath().endsWith(PORTAL + MASTER_DEFAULT));
        assertTrue(underTest.resolveTemplatePath(MASTER_HORIZONTAL_CENTER).getPath()
                .endsWith(PORTAL + MASTER_HORIZONTAL_CENTER));

        // To be controlled through AdditionalTemplates
        assertTrue(underTest.resolveTemplatePath(ROOT).getPath().endsWith(ADDITIONAL_PROVIDER + ROOT));
        assertTrue(underTest.resolveTemplatePath(ADDITIONAL_TEMPLATE).getPath()
                .endsWith(ADDITIONAL_PROVIDER + ADDITIONAL_TEMPLATE));
    }

    @Test
    void shouldFailOnNoneExistingResource() {
        assertThrows(IllegalArgumentException.class, () -> underTest.resolveTemplatePath(NOT_THERE));
    }
}
