package de.cuioss.portal.ui.runtime.application.bundle;

import static de.cuioss.tools.collect.CollectionLiterals.mutableSet;

import java.util.Set;

import javax.annotation.Priority;
import javax.enterprise.context.SessionScoped;

import de.cuioss.jsf.api.application.bundle.ResourceBundleWrapper;
import de.cuioss.portal.configuration.common.PortalPriorities;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@SuppressWarnings("javadoc")
@SessionScoped
@Priority(PortalPriorities.PORTAL_INSTALLATION_LEVEL)
@ToString
@EqualsAndHashCode
public class InstallationResourceBundleWrapperMock implements ResourceBundleWrapper {

    private static final long serialVersionUID = 8403682810059890158L;

    @Override
    public String getMessage(final String key) {
        if ("page.error.title".equals(key)) {
            return "Test";
        }
        throw new IllegalStateException();
    }

    @Override
    public Set<String> keySet() {
        return mutableSet("page.error.title");
    }

    @Override
    public String getBundleContent() {
        return toString();
    }

}
