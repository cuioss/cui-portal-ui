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
package de.cuioss.portal.ui.runtime.application.theme;

import org.jboss.weld.junit5.auto.AddBeanClasses;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.cuioss.portal.core.test.mocks.core.PortalClientStorageMock;
import de.cuioss.portal.ui.runtime.support.ResourceHandlerMock;
import de.cuioss.portal.ui.test.junit5.EnablePortalUiEnvironment;
import de.cuioss.test.jsf.util.JsfEnvironmentConsumer;
import de.cuioss.test.jsf.util.JsfEnvironmentHolder;
import de.cuioss.test.valueobjects.junit5.contracts.ShouldBeNotNull;
import lombok.Getter;
import lombok.Setter;

@EnablePortalUiEnvironment
@AddBeanClasses({ PortalThemeConfiguration.class, PortalClientStorageMock.class, UserThemeBean.class })
class ThemeResourceHandlerTest implements ShouldBeNotNull<ThemeResourceHandler>, JsfEnvironmentConsumer {

    @Getter
    private ThemeResourceHandler underTest;

    private ResourceHandlerMock resourceHandlerMock;

    private static final String LIBRARY_NAME = "de.cuioss.portal.css";
    private static final String OTHER_LIBRARY_NAME = "de.cuioss.portal.other";

    private static final String STYLE_CSS = "application.css";

    private static final String STYLE_OTHER = "other.css";
    @Setter
    @Getter
    private JsfEnvironmentHolder environmentHolder;

    @BeforeEach
    void setUpTest() {
        resourceHandlerMock = new ResourceHandlerMock(getApplication().getResourceHandler());
        underTest = new ThemeResourceHandler(resourceHandlerMock);
    }

    @Test
    void shouldReturnDefaultTheme() {
        underTest.createResource(STYLE_CSS, LIBRARY_NAME);
        resourceHandlerMock.assertResourceCreated(PortalThemeConfigurationTest.APPLICATION_DEFAULT_CSS, LIBRARY_NAME);
    }

    @Test
    void shouldPassOnNonApplicationCssCalls() {
        underTest.createResource(STYLE_OTHER, LIBRARY_NAME);
        resourceHandlerMock.assertResourceCreated(STYLE_OTHER, LIBRARY_NAME);
    }

    @Test
    void shouldPassOnOtherLibraryCalls() {
        underTest.createResource(STYLE_CSS, OTHER_LIBRARY_NAME);
        resourceHandlerMock.assertResourceCreated(STYLE_CSS, OTHER_LIBRARY_NAME);
    }

    @Test
    void shouldPassOnOtherCall() {
        underTest.createResource(STYLE_OTHER, OTHER_LIBRARY_NAME);
        resourceHandlerMock.assertResourceCreated(STYLE_OTHER, OTHER_LIBRARY_NAME);
    }

}
