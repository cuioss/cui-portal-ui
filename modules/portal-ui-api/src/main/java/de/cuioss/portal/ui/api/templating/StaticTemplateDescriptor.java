package de.cuioss.portal.ui.api.templating;

import java.io.Serializable;
import java.util.List;

/**
 * Utilized for statically extending the default {@link MultiTemplatingMapper}
 * defined by cui-portal-core-cdi-impl. Provides information which template are
 * to be handled by which concrete Template-Directory, see package-info for
 * details.
 *
 * @author Oliver Wolff
 */
public interface StaticTemplateDescriptor extends Serializable {

    /**
     * @return a List of names of the templates to be handles by this concrete
     *         descriptor.
     */
    List<String> getHandledTemplates();

    /**
     * @return the name of the Template-Directory the templates within this
     *         descriptor belong to. It must not end with '/'
     */
    String getTemplatePath();
}
