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
package de.icw.cui.portal.ui.errorpages;

import org.jboss.weld.environment.se.Weld;

import de.cuioss.portal.core.test.tests.BaseModuleConsistencyTest;
import de.cuioss.test.jsf.producer.JsfObjectsProducers;
import de.cuioss.test.jsf.producer.ServletObjectsFromJSFContextProducers;

class ModuleConsistencyTest extends BaseModuleConsistencyTest {

    @Override
    protected Weld modifyWeldContainer(Weld weld) {
        return weld.addBeanClasses(ServletObjectsFromJSFContextProducers.class, JsfObjectsProducers.class);
    }
}
