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
package de.cuioss.portal.ui.runtime.application;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import jakarta.faces.FactoryFinder;
import jakarta.faces.application.ApplicationFactory;

import org.junit.jupiter.api.Test;

import de.cuioss.test.jsf.junit5.EnableJsfEnvironment;
import de.cuioss.test.jsf.util.JsfEnvironmentConsumer;
import de.cuioss.test.jsf.util.JsfEnvironmentHolder;
import lombok.Getter;
import lombok.Setter;

@EnableJsfEnvironment
class PortalApplicationFactoryTest implements JsfEnvironmentConsumer {

    @Setter
    @Getter
    private JsfEnvironmentHolder environmentHolder;

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
        var applicationFactory = (ApplicationFactory) FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);

        var factory = new PortalApplicationFactory(applicationFactory);
        factory.setApplication(getApplication());
        assertNotNull(factory.getWrapped());

        var wrapped = factory.getApplication();

        assertInstanceOf(PortalApplication.class, wrapped);
    }

}
