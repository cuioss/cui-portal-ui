package de.cuioss.portal.ui.api.resources;

import java.util.Objects;

import javax.faces.application.Resource;
import javax.faces.context.FacesContext;

import de.cuioss.jsf.api.application.resources.util.ResourceUtil;
import lombok.ToString;

/**
 * Base Resource using {@link ResourceUtil} to calculate
 * {@link #getRequestPath()} and {@link #getURL()}.
 *
 * @author Matthias Walliczek
 */
@ToString
public abstract class CuiResource extends Resource {

    @Override
    public String getRequestPath() {
        return ResourceUtil.calculateRequestPath(getResourceName(), getLibraryName(),
                FacesContext.getCurrentInstance());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getResourceName(), getLibraryName());
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof CuiResource) {
            final var other = (CuiResource) obj;
            return Objects.equals(getResourceName(), other.getResourceName())
                    && Objects.equals(getLibraryName(), other.getLibraryName());
        }
        return false;
    }
}
