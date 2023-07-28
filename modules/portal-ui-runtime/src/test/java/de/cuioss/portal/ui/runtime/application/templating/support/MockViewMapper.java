package de.cuioss.portal.ui.runtime.application.templating.support;

import java.net.URL;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;

import de.cuioss.portal.ui.api.templating.MultiViewMapper;
import de.cuioss.portal.ui.api.templating.PortalMultiViewMapper;
import de.cuioss.tools.io.FileLoaderUtility;
import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("javadoc")
@PortalMultiViewMapper
@Alternative
@ApplicationScoped
public class MockViewMapper implements MultiViewMapper {

    private static final long serialVersionUID = -155012364802045126L;

    @Getter
    @Setter
    private String basePath;

    @Override
    public URL resolveViewPath(final String requestedResource) {
        return FileLoaderUtility.getLoaderForPath(basePath + requestedResource).getURL();
    }

}
