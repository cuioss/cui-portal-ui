package de.cuioss.portal.ui.runtime.application.templating;

import static de.cuioss.portal.ui.runtime.application.templating.PortalTemplateMapperSimpleTest.MASTER_DEFAULT;
import static de.cuioss.portal.ui.runtime.application.templating.PortalTemplateMapperSimpleTest.MASTER_HORIZONTAL_CENTER;
import static de.cuioss.portal.ui.runtime.application.templating.PortalTemplateMapperSimpleTest.NOT_THERE;
import static de.cuioss.portal.ui.runtime.application.templating.PortalTemplateMapperSimpleTest.PORTAL;
import static de.cuioss.portal.ui.runtime.application.templating.PortalTemplateMapperSimpleTest.ROOT;
import static de.cuioss.portal.ui.runtime.application.templating.PortalTemplateMapperSimpleTest.TECHNICAL_ROOT;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.inject.Inject;

import org.jboss.weld.junit5.auto.AddBeanClasses;
import org.jboss.weld.junit5.auto.EnableAutoWeld;
import org.junit.jupiter.api.Test;

import de.cuioss.portal.ui.api.templating.PortalMultiTemplatingMapper;
import de.cuioss.portal.ui.runtime.application.templating.support.AdditionalTemplates;
import de.cuioss.test.valueobjects.junit5.contracts.ShouldHandleObjectContracts;
import lombok.Getter;

@EnableAutoWeld
@AddBeanClasses({ PortalTemplates.class, AdditionalTemplates.class })
class PortalTemplateMapperExtendedTest implements ShouldHandleObjectContracts<PortalTemplateMapper> {

    public static final String ADDITIONAL_PROVIDER = "additional/";

    public static final String ADDITIONAL_TEMPLATE = "module.xhtml";

    @Inject
    @PortalMultiTemplatingMapper
    @Getter
    private PortalTemplateMapper underTest;

    @Test
    void shouldInitCorrectly() {
        // To be controlled through PortalTemplates
        assertTrue(underTest.resolveTemplatePath(TECHNICAL_ROOT).getPath().endsWith(PORTAL + TECHNICAL_ROOT));
        assertTrue(underTest.resolveTemplatePath(MASTER_DEFAULT).getPath().endsWith(PORTAL + MASTER_DEFAULT));
        assertTrue(underTest.resolveTemplatePath(MASTER_HORIZONTAL_CENTER).getPath()
                .endsWith(PORTAL + MASTER_HORIZONTAL_CENTER));

        // To be controlled through AdditionalTemplates
        assertTrue(underTest.resolveTemplatePath(ROOT).getPath().endsWith(ADDITIONAL_PROVIDER + ROOT));
        assertTrue(underTest.resolveTemplatePath(ADDITIONAL_TEMPLATE).getPath()
                .endsWith(ADDITIONAL_PROVIDER + ADDITIONAL_TEMPLATE));
    }

    @Test
    void shouldFailOnNoneExisitigResource() {
        assertThrows(IllegalArgumentException.class, () -> {
            underTest.resolveTemplatePath(NOT_THERE);
        });
    }

}
