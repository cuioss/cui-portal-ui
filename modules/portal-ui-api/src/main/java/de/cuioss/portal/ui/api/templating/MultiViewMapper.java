package de.cuioss.portal.ui.api.templating;

import java.io.Serializable;
import java.net.URL;

/**
 * See package-info for description.
 *
 * @author Matthias Walliczek
 */
public interface MultiViewMapper extends Serializable {

    /**
     * @param requestedResource must not be null. Represents a concrete view e.g.
     *                          faces/guest/login.xhtml or
     *                          subdirectory/component.xhtml without the technical
     *                          path segments
     * @return an instance of a {@link URL} to access the prefixed resource either
     *         as external file or as classpath resource, e.g. portal/root.xhtml or
     *         portal/subdirectory/component.xhtml respectively
     */
    URL resolveViewPath(String requestedResource);
}
