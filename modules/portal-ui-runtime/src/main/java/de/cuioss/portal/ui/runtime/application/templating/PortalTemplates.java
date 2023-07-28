package de.cuioss.portal.ui.runtime.application.templating;

import static de.cuioss.tools.collect.CollectionLiterals.immutableList;

import java.util.List;

import javax.annotation.Priority;
import javax.enterprise.context.Dependent;
import javax.inject.Named;

import de.cuioss.portal.configuration.common.PortalPriorities;
import de.cuioss.portal.ui.api.templating.PortalTemplateDescriptor;
import de.cuioss.portal.ui.api.templating.StaticTemplateDescriptor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Defines the Portal defined templates
 *
 * @author Oliver Wolff
 */
@PortalTemplateDescriptor
@Dependent
@Priority(PortalPriorities.PORTAL_CORE_LEVEL)
@Named
@EqualsAndHashCode
@ToString
public class PortalTemplates implements StaticTemplateDescriptor {

    private static final long serialVersionUID = 1933293647595996193L;

    @Getter
    private final List<String> handledTemplates = immutableList("master.xhtml", "plainView.xhtml",
            "master_centered.xhtml", "http_error_page.xhtml", "technical_root.xhtml", "root.xhtml",
            "layout_footer.xhtml");

    @Getter
    private final String templatePath = "classpath:/META-INF/templates/portal";

}
