package de.cuioss.portal.ui.runtime.application.customization;

import javax.faces.application.Resource;

/**
 * To be used by {@link CustomizationResourceHandler}.
 *
 * @author Matthias Walliczek
 *
 */
public interface ResourceProducer {

    /**
     * @param resourceName
     * @param libraryName
     * @return the resource with given resourceName and libraryName if found,
     *         otherwise null
     */
    Resource retrieveResource(String resourceName, String libraryName);

}
