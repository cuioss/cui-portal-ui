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

import de.cuioss.portal.ui.api.templating.PortalMultiViewMapper;
import de.cuioss.test.juli.junit5.EnableTestLogger;
import de.cuioss.test.valueobjects.junit5.contracts.ShouldHandleObjectContracts;
import jakarta.inject.Inject;
import lombok.Getter;
import org.jboss.weld.junit5.auto.EnableAutoWeld;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;

@EnableAutoWeld
@EnableTestLogger
class PortalViewMapperSimpleTest implements ShouldHandleObjectContracts<PortalViewMapper> {

    public static final String PORTAL = "/META-INF/";

    public static final String NOT_THERE = "not.there.xhtml";

    @Inject
    @PortalMultiViewMapper
    @Getter
    private PortalViewMapper underTest;

    @Test
    void shouldHAndleNoneExistingResource() {
        assertFalse(underTest.resolveViewPath(NOT_THERE).isPresent());
    }

}
