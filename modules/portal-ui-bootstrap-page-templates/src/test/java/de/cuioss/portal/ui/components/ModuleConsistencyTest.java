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
package de.cuioss.portal.ui.components;

import org.junit.jupiter.api.Disabled;

import de.cuioss.portal.core.test.tests.BaseModuleConsistencyTest;

/**
 * Tests the complete cdi environment / wiring
 *
 * @author Oliver Wolff
 */
class ModuleConsistencyTest extends BaseModuleConsistencyTest {

    @Override
    @Disabled("Currently there is the need to portal-core-impl modules. This needs to be fixed: PortalHistoryManager")
    protected void shouldStartUpContainer() {
    }
}
