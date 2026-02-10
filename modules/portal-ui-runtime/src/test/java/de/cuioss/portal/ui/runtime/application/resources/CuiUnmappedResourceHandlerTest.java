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
package de.cuioss.portal.ui.runtime.application.resources;

import de.cuioss.portal.ui.test.junit5.EnablePortalUiEnvironment;
import de.cuioss.test.jsf.mocks.CuiMockResourceHandler;
import de.cuioss.test.jsf.util.JsfEnvironmentConsumer;
import de.cuioss.test.jsf.util.JsfEnvironmentHolder;
import lombok.Getter;
import lombok.Setter;
import org.apache.myfaces.test.mock.MockHttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static jakarta.faces.application.ResourceHandler.RESOURCE_IDENTIFIER;
import static org.junit.jupiter.api.Assertions.*;

@EnablePortalUiEnvironment
class CuiUnmappedResourceHandlerTest implements JsfEnvironmentConsumer {

    @Setter
    @Getter
    private JsfEnvironmentHolder environmentHolder;

    private CuiUnmappedResourceHandler underTest;

    @BeforeEach
    void setUpHandlerTest() {
        CuiMockResourceHandler mockResourceHandler = CuiResourceHandlerTest.setupResourceHandlerMock();
        underTest = new CuiUnmappedResourceHandler(mockResourceHandler);
    }

    @ParameterizedTest
    @CsvSource({"faces/hello.xhtml,false", RESOURCE_IDENTIFIER + "/application.css,true"})
    void shouldDetectRessource(String url, String resourceRequest) {
        boolean resourceRequestBoolean = Boolean.parseBoolean(resourceRequest);
        setRequestURL(url);
        assertEquals(resourceRequestBoolean, underTest.isResourceRequest(getFacesContext()));
        assertDoesNotThrow(() -> underTest.handleResourceRequest(getFacesContext()));
        assertNotNull(underTest.decorateResource(CuiResourceHandlerTest.prepareResource(CuiResourceHandlerTest.CSS_LIBRARY, CuiResourceHandlerTest.APPLICATION_CSS)));
    }

    private void setRequestURL(String requestURL) {
        var request = (MockHttpServletRequest) getExternalContext().getRequest();
        request.setPathElements("faces", "", requestURL, "");
    }

}