package de.cuioss.portal.ui.runtime.application.templating.support;

import static de.cuioss.tools.collect.CollectionLiterals.immutableList;

import java.util.List;

import javax.annotation.Priority;
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
@Named
@Priority(PortalPriorities.PORTAL_MODULE_LEVEL)
@EqualsAndHashCode
@ToString
public class AdditionalTemplates implements StaticTemplateDescriptor {

    private static final long serialVersionUID = 1933293647595996193L;

    @Getter
    private final List<String> handledTemplates = immutableList("root.xhtml", "module.xhtml");

    @Getter
    private final String templatePath = "additional";

}
