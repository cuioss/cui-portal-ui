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
package de.cuioss.portal.ui.api.ui.context;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import javax.faces.application.NavigationHandler;
import javax.inject.Inject;
import javax.inject.Provider;

import org.jboss.weld.junit5.auto.AddBeanClasses;
import org.jboss.weld.junit5.auto.EnableAutoWeld;
import org.junit.jupiter.api.Test;

import de.cuioss.test.jsf.junit5.EnableJsfEnvironment;
import de.cuioss.test.jsf.producer.JsfObjectsProducers;

@EnableJsfEnvironment
@EnableAutoWeld
@AddBeanClasses({ NavigationHandlerProducer.class, JsfObjectsProducers.class })
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
