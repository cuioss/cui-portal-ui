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
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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