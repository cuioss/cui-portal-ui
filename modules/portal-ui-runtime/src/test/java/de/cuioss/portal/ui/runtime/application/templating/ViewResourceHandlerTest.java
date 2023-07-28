package de.cuioss.portal.ui.runtime.application.templating;

import javax.inject.Inject;

import org.jboss.weld.junit5.auto.EnableAlternatives;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.cuioss.portal.ui.api.templating.PortalMultiTemplatingMapper;
import de.cuioss.portal.ui.api.templating.PortalMultiViewMapper;
import de.cuioss.portal.ui.runtime.application.templating.support.MockTemplateMapper;
import de.cuioss.portal.ui.runtime.application.templating.support.MockViewMapper;
import de.cuioss.portal.ui.test.junit5.EnablePortalUiEnvironment;
import de.cuioss.test.jsf.mocks.CuiMockResourceHandler;
import de.cuioss.test.jsf.util.JsfEnvironmentConsumer;
import de.cuioss.test.jsf.util.JsfEnvironmentHolder;
import de.cuioss.test.valueobjects.junit5.contracts.ShouldBeNotNull;
import io.smallrye.common.constraint.Assert;
import lombok.Getter;
import lombok.Setter;

@EnablePortalUiEnvironment
@EnableAlternatives({ MockTemplateMapper.class, MockViewMapper.class })
class ViewResourceHandlerTest implements ShouldBeNotNull<ViewResourceHandler>, JsfEnvironmentConsumer {

    @Setter
    @Getter
    private JsfEnvironmentHolder environmentHolder;

    private static final String VIEWS_BASE_PATH = "META-INF/faces/test/";

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
        Assert.assertNotNull(result);
        Assert.assertTrue(result.getURL().toString().startsWith(CuiMockResourceHandler.DUMMY_URL));
    }

    @Test
    void shouldMapTemplates() {
        templateMapper.setBasePath(TEMPLATES_BASE_BATH);

        var result = underTest.createViewResource(getFacesContext(), "/templates/dummy.xhtml");
        Assert.assertNotNull(result);
        Assert.assertTrue(result.getURL().toString().endsWith(TEMPLATES_BASE_BATH + "dummy.xhtml"));
    }

    @Test
    void shouldMapViews() {
        viewMapper.setBasePath(VIEWS_BASE_PATH);
        var result = underTest.createViewResource(getFacesContext(), "/faces/dummy.xhtml");
        Assert.assertNotNull(result);
        Assert.assertTrue(result.getURL().toString().endsWith(VIEWS_BASE_PATH + "dummy.xhtml"));
    }

}
