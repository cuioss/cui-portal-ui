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

import de.cuioss.portal.ui.api.templating.PortalMultiTemplatingMapper;
import de.cuioss.portal.ui.api.templating.PortalMultiViewMapper;
import de.cuioss.portal.ui.runtime.application.templating.support.MockTemplateMapper;
import de.cuioss.portal.ui.runtime.application.templating.support.MockViewMapper;
import de.cuioss.portal.ui.test.junit5.EnablePortalUiEnvironment;
import de.cuioss.test.jsf.mocks.CuiMockResourceHandler;
import de.cuioss.test.jsf.util.JsfEnvironmentConsumer;
import de.cuioss.test.jsf.util.JsfEnvironmentHolder;
import de.cuioss.test.valueobjects.junit5.contracts.ShouldBeNotNull;
import jakarta.inject.Inject;
import lombok.Getter;
import lombok.Setter;
import org.jboss.weld.junit5.auto.EnableAlternatives;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@EnablePortalUiEnvironment
@EnableAlternatives({ MockTemplateMapper.class, MockViewMapper.class })
class ViewResourceHandlerTest implements ShouldBeNotNull<ViewResourceHandler>, JsfEnvironmentConsumer {

    @Setter
    @Getter
    private JsfEnvironmentHolder environmentHolder;

    private static final String TEMPLATES_BASE_BATH = "META-INF/templates/test/";

    @Getter
    private ViewResourceHandler underTest;

    @Inject
    @PortalMultiTemplatingMapper
    private MockTemplateMapper templateMapper;

    @Inject
    @PortalMultiViewMapper
    private MockViewMapper viewMapper;

    @BeforeEach
    void beforeTest() {
        underTest = new ViewResourceHandler(new CuiMockResourceHandler());
    }

    @Test
    void shouldPassThroughUnmapped() {
        var result = underTest.createViewResource(getFacesContext(), "dummy");
        assertNotNull(result);
        assertTrue(result.getURL().toString().startsWith(CuiMockResourceHandler.DUMMY_URL));
    }

    @Test
    void shouldMapTemplates() {
        templateMapper.setBasePath(TEMPLATES_BASE_BATH);

        var result = underTest.createViewResource(getFacesContext(), "/templates/dummy.xhtml");
        assertNotNull(result);
        assertTrue(result.getURL().toString().endsWith(TEMPLATES_BASE_BATH + "dummy.xhtml"));
    }

}
