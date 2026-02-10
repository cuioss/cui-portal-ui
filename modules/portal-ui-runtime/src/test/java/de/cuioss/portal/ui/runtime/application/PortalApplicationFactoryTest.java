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
package de.cuioss.portal.ui.runtime.application;

import de.cuioss.test.jsf.junit5.EnableJsfEnvironment;
import jakarta.faces.FactoryFinder;
import jakarta.faces.application.ApplicationFactory;
import jakarta.faces.context.FacesContext;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@EnableJsfEnvironment
class PortalApplicationFactoryTest {

    @Test
    void shouldBuildFromFactoryWithGetMethod() {
        var applicationFactory = (ApplicationFactory) FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);

        var factory = new PortalApplicationFactory(applicationFactory);
        assertNotNull(factory.getWrapped());

        var wrapped = factory.getApplication();

        assertInstanceOf(PortalApplication.class, wrapped);
    }

    @Test
    void shouldBuildFromFactoryWithSetMethod() {
        var application = FacesContext.getCurrentInstance().getApplication();
        var applicationFactory = (ApplicationFactory) FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);

        var factory = new PortalApplicationFactory(applicationFactory);
        factory.setApplication(application);
        assertNotNull(factory.getWrapped());

        var wrapped = factory.getApplication();

        assertInstanceOf(PortalApplication.class, wrapped);
    }

}
