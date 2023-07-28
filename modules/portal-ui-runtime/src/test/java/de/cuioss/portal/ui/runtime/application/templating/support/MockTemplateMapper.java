package de.cuioss.portal.ui.runtime.application.templating.support;

import java.net.URL;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;

import de.cuioss.portal.ui.api.templating.MultiTemplatingMapper;
import de.cuioss.portal.ui.api.templating.PortalMultiTemplatingMapper;
import de.cuioss.tools.io.FileLoaderUtility;
import lombok.Getter;
import lombok.Setter;

@PortalMultiTemplatingMapper
@ApplicationScoped
@Alternative
@SuppressWarnings("javadoc")
public class MockTemplateMapper implements MultiTemplatingMapper {

    private static final long serialVersionUID = 5885265658897055337L;

    @Getter
    @Setter
    private String basePath;

    @Override
    public URL resolveTemplatePath(final String requestedResource) {
        return FileLoaderUtility.getLoaderForPath(basePath + requestedResource).getURL();
    }

}
