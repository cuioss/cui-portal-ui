package de.cuioss.portal.ui.api.templating;

import java.io.Serializable;
import java.util.List;

/**
 * Utilized for statically extending the default {@link MultiViewMapper} defined
 * by portal-ui-api. Provides information which views are to be handled by which
 * concrete faces-Directory, see package-info for details.
 *
 * @author Oliver Wolff
 */
public interface StaticViewDescriptor extends Serializable {

    /**
     * @return a List of names of the templates to be handles by this concrete
     *         descriptor.
     */
    List<String> getHandledViews();

    /**
     * @return the name of the faces-Directory the templates within this descriptor
     *         belong to. It must not end with '/'
     */
    String getViewPath();
}
