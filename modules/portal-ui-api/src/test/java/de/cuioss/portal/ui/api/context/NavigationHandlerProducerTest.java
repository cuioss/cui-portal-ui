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
package de.cuioss.portal.ui.api.context;

import de.cuioss.test.jsf.junit5.EnableJsfEnvironment;
import de.cuioss.test.jsf.producer.JsfObjectsProducer;
import jakarta.faces.application.NavigationHandler;
import jakarta.inject.Inject;
import jakarta.inject.Provider;
import org.jboss.weld.junit5.auto.AddBeanClasses;
import org.jboss.weld.junit5.auto.EnableAutoWeld;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

@EnableJsfEnvironment
@EnableAutoWeld
@AddBeanClasses({NavigationHandlerProducer.class, JsfObjectsProducer.class})
class NavigationHandlerProducerTest {

    @Inject
    @CuiNavigationHandler
    private Provider<NavigationHandler> handlerProvider;

    @Inject
    private NavigationHandlerProducer navigationHandlerProducer;

    @Test
    void shouldProduce() {
        assertInstanceOf(NavigationHandler.class, handlerProvider.get());
        assertInstanceOf(NavigationHandler.class, navigationHandlerProducer.getNavigationHandler());
    }

}
