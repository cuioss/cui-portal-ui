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
package de.cuioss.portal.ui.runtime.application.templating;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import jakarta.inject.Inject;

import org.jboss.weld.junit5.auto.AddBeanClasses;
import org.jboss.weld.junit5.auto.EnableAutoWeld;
import org.junit.jupiter.api.Test;

import de.cuioss.portal.ui.api.templating.PortalMultiTemplatingMapper;
import de.cuioss.test.valueobjects.junit5.contracts.ShouldHandleObjectContracts;
import lombok.Getter;

@EnableAutoWeld
@AddBeanClasses(PortalTemplates.class)
class PortalTemplateMapperSimpleTest implements ShouldHandleObjectContracts<PortalTemplateMapper> {

    public static final String PORTAL = "/META-INF/templates/portal/";

    public static final String TECHNICAL_ROOT = "technical_root.xhtml";

    public static final String ROOT = "root.xhtml";

    public static final String MASTER_DEFAULT = "master.xhtml";

    public static final String MASTER_HORIZONTAL_CENTER = "master_centered.xhtml";

    public static final String NOT_THERE = "not.there.xhtml";

    @Inject
    @PortalMultiTemplatingMapper
    @Getter
    private PortalTemplateMapper underTest;

    @Test
    void shouldInitCorrectly() {
        assertTrue(underTest.resolveTemplatePath(TECHNICAL_ROOT).getPath().endsWith(PORTAL + TECHNICAL_ROOT));
        assertTrue(underTest.resolveTemplatePath(ROOT).getPath().endsWith(PORTAL + ROOT));
        assertTrue(underTest.resolveTemplatePath(MASTER_DEFAULT).getPath().endsWith(PORTAL + MASTER_DEFAULT));
        assertTrue(underTest.resolveTemplatePath(MASTER_HORIZONTAL_CENTER).getPath()
                .endsWith(PORTAL + MASTER_HORIZONTAL_CENTER));
    }

    @Test
    void shouldFailOnNoneExistingResource() {
        assertThrows(IllegalArgumentException.class, () -> underTest.resolveTemplatePath(NOT_THERE));
    }
}
