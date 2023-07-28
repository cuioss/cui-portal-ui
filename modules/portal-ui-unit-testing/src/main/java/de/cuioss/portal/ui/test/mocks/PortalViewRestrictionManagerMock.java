package de.cuioss.portal.ui.test.mocks;

import static java.util.Objects.requireNonNull;

import java.util.HashSet;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.SessionScoped;

import de.cuioss.jsf.api.common.view.ViewDescriptor;
import de.cuioss.portal.ui.api.view.PortalViewRestrictionManager;
import de.cuioss.portal.ui.api.view.ViewRestrictionManager;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Mock-implementation of {@link ViewRestrictionManager}. It is
 * {@link ApplicationScoped} in order to simplify state management for
 * unit-tests, The portal implementations are usually {@link SessionScoped}
 *
 * @author Oliver Wolff
 */
@PortalViewRestrictionManager
@ApplicationScoped
@EqualsAndHashCode
@ToString
public class PortalViewRestrictionManagerMock implements ViewRestrictionManager {

    private static final long serialVersionUID = 5290360665409615415L;

    /**
     * The required roles to be returned with
     * {@link #getRequiredRolesForView(ViewDescriptor)}
     */
    @Getter
    @Setter
    private Set<String> requiredRoles = new HashSet<>();

    @Setter
    private boolean authorized = true;

    @Override
    public Set<String> getRequiredRolesForView(final ViewDescriptor descriptor) {
        requireNonNull(descriptor);
        return requiredRoles;
    }

    @Override
    public boolean isUserAuthorized(final ViewDescriptor descriptor) {
        requireNonNull(descriptor);
        return authorized;
    }

    @Override
    public boolean isUserAuthorizedForViewOutcome(final String viewOutcome) {
        requireNonNull(viewOutcome);
        return authorized;
    }

}
