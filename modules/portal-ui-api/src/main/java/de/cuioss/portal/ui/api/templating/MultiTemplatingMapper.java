package de.cuioss.portal.ui.api.templating;

import java.io.Serializable;
import java.net.URL;

/**
 * See package-info for description.
 *
 * @author Oliver Wolff
 */
public interface MultiTemplatingMapper extends Serializable {

    /**
     * @param requestedResource must not be null. Represents a concrete template
     *                          e.g. root.xhtml or subdirectory/component.xhtml
     *                          without the technical path segments
     * @return an instance of a {@link URL} to access the prefixed resource either
     *         as external file or as classpath resource, e.g. portal/root.xhtml or
     *         portal/subdirectory/component.xhtml respectively
     */
    URL resolveTemplatePath(String requestedResource);
}
