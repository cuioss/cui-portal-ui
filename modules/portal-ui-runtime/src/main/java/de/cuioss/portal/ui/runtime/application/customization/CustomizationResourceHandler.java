package de.cuioss.portal.ui.runtime.application.customization;

import java.util.Optional;

import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;
import javax.faces.application.ResourceHandlerWrapper;

import de.cuioss.portal.core.cdi.PortalBeanManager;
import de.cuioss.tools.base.Preconditions;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Allows to override specific resources at the installation. Delegates to
 * {@link ResourceProducer}.
 *
 * @author Matthias Walliczek
 */
@RequiredArgsConstructor
public class CustomizationResourceHandler extends ResourceHandlerWrapper {

    /**
     * Wrapped resource handler.
     */
    @Getter
    private final ResourceHandler wrapped;

    @Override
    @SuppressWarnings("java:S3655") // owolff: False Positive, isPresent is checked properly
    public Resource createResource(final String resourceName, final String libraryName) {
        final Optional<ResourceProducer> resourceProducer = PortalBeanManager.resolveBean(ResourceProducer.class,
                PortalResourceProducer.class);

        Preconditions.checkArgument(resourceProducer.isPresent(),
                PortalBeanManager.createLogMessage(ResourceProducer.class, PortalResourceProducer.class));

        var result = resourceProducer.get().retrieveResource(resourceName, libraryName);
        if (null != result) {
            return result;
        }

        return super.createResource(resourceName, libraryName);
    }

}
