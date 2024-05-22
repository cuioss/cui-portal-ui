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
package de.cuioss.portal.ui.runtime.application.view;

import static de.cuioss.portal.ui.test.configuration.PortalNavigationConfiguration.VIEW_HOME_LOGICAL_VIEW_ID;
import static de.cuioss.portal.ui.test.configuration.PortalNavigationConfiguration.VIEW_LOGIN_LOGICAL_VIEW_ID;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import jakarta.inject.Inject;

import org.jboss.weld.junit5.auto.AddBeanClasses;
import org.junit.jupiter.api.Test;

import de.cuioss.portal.ui.api.ui.context.CurrentViewProducer;
import de.cuioss.portal.ui.runtime.application.configuration.ViewConfiguration;
import de.cuioss.portal.ui.runtime.application.view.matcher.ViewMatcherProducer;
import de.cuioss.portal.ui.test.junit5.EnablePortalUiEnvironment;
import de.cuioss.test.jsf.util.JsfEnvironmentConsumer;
import de.cuioss.test.jsf.util.JsfEnvironmentHolder;
import de.cuioss.test.valueobjects.junit5.contracts.ShouldHandleObjectContracts;
import lombok.Getter;
import lombok.Setter;

@EnablePortalUiEnvironment
@AddBeanClasses({ ViewConfiguration.class, CurrentViewProducer.class, ViewMatcherProducer.class })
class ViewTransientManagerBeanTest
        implements ShouldHandleObjectContracts<ViewTransientManagerBean>, JsfEnvironmentConsumer {

    @Setter
    @Getter
    private JsfEnvironmentHolder environmentHolder;

    @Inject
    @Getter
    private ViewTransientManagerBean underTest;

    @Test
    void shouldProvideTransientForLogin() {
        getRequestConfigDecorator().setViewId(VIEW_LOGIN_LOGICAL_VIEW_ID);
        assertTrue(underTest.isTransientView());
    }

    @Test
    void shouldProvideNotTransientForHome() {
        getRequestConfigDecorator().setViewId(VIEW_HOME_LOGICAL_VIEW_ID);
        assertFalse(underTest.isTransientView());
    }

}
