package de.cuioss.portal.ui.api.view;

import java.io.Serializable;
import java.util.Set;

import javax.enterprise.context.SessionScoped;

import de.cuioss.jsf.api.common.view.ViewDescriptor;

/**
 * The ViewRestrictionManager is used for deciding whether a concrete view is
 * authorized for the current users. The implementation are therefore stateful,
 * usually {@link SessionScoped}.
 *
 * @author Oliver Wolff
 */
public interface ViewRestrictionManager extends Serializable {

    /**
     * Determines the roles required for accessing this specific views.
     *
     * @param descriptor identifying the view to be accessed, must not be null
     * @return an immutable {@link Set} of role-names needed to access this views,
     *         may be empty but never {@code null}
     */
    Set<String> getRequiredRolesForView(ViewDescriptor descriptor);

    /**
     * Determines whether the currently logged in user is allowed / authorized to
     * access the given view.
     *
     * @param descriptor identifying the view to be accessed, must not be null
     * @return a boolean indicating whether the current user is authorized to access
     *         the given view {@code true} or not {@code false}
     */
    boolean isUserAuthorized(ViewDescriptor descriptor);

    /**
     * Determines whether the currently logged in user is allowed / authorized to
     * access the given view, identified by the given outcome.
     *
     * @param viewOutcome String outcome identifying a concrete view that should be
     *                    checked
     * @return a boolean indicating whether the current user is authorized to access
     *         the given view {@code true} or not {@code false}
     * @throws IllegalStateException signaling, that the view can not not
     *                               determined, e.g.g there is no navigation-rule
     *                               defined for the given outcome
     */
    boolean isUserAuthorizedForViewOutcome(String viewOutcome);

}
