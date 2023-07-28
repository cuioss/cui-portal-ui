package de.cuioss.portal.ui.api.listener.view;

import java.io.Serializable;

import javax.faces.event.PhaseId;

import de.cuioss.jsf.api.common.view.ViewDescriptor;

/**
 * Instances of this Listener will be called from the portal as listener for
 * {@link PhaseId#RESTORE_VIEW}. whether before or after is defined by
 * {@link PhaseExecution}. It will pass the current viewId.
 *
 * @author Oliver Wolff
 */
public interface ViewListener extends Serializable {

    /**
     * Command pattern like handler for interacting on a given view. This may be
     * security checks, or sanity checks, e.g. The handler method must explicitly
     * throw an exception or fire an event in order to act.
     *
     * @param viewDescriptor identifying the requested view. Must not be null nor
     *                       empty.
     */
    void handleView(ViewDescriptor viewDescriptor);

    boolean isEnabled();
}
