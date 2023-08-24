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

import javax.inject.Inject;

import org.jboss.weld.junit5.auto.EnableAutoWeld;
import org.junit.jupiter.api.Test;

import de.cuioss.portal.ui.api.templating.PortalMultiViewMapper;
import de.cuioss.test.valueobjects.junit5.contracts.ShouldHandleObjectContracts;
import lombok.Getter;

@EnableAutoWeld
class PortalViewMapperSimpleTest implements ShouldHandleObjectContracts<PortalViewMapper> {

    public static final String PORTAL = "/META-INF/faces/";

    public static final String INDEX = "pages/index.xhtml";

    public static final String NOT_THERE = "not.there.xhtml";

    @Inject
    @PortalMultiViewMapper
    @Getter
    private PortalViewMapper underTest;

    @Test
    void shouldInitCorrectly() {
        assertTrue(underTest.resolveViewPath(INDEX).getPath().endsWith(PORTAL + INDEX));
    }

    @Test
    void shouldFailOnNoneExistingResource() {
        assertThrows(NullPointerException.class, () -> {
            assertTrue(underTest.resolveViewPath(NOT_THERE).getPath().endsWith(PORTAL + NOT_THERE));
        });
    }

}
