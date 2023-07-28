package de.cuioss.portal.ui.api.listener.view;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseListener;

/**
 * Determines whether a concrete Portal-listener is to be fired
 * {@link PhaseListener#beforePhase(javax.faces.event.PhaseEvent)} of
 * {@link PhaseListener#afterPhase(javax.faces.event.PhaseEvent)}
 *
 * @author Oliver Wolff
 */
public enum PhaseExecution {
    /**
     * to be fired {@link PhaseListener#beforePhase(javax.faces.event.PhaseEvent)}
     */
    BEFORE_PHASE,

    /**
     * to be fired {@link PhaseListener#afterPhase(javax.faces.event.PhaseEvent)}.
     * It will be called for {@link FacesContext#isPostback()} calls as well.
     */
    AFTER_PHASE,

    /**
     * To be fired {@link PhaseListener#afterPhase(javax.faces.event.PhaseEvent)}
     * for non {@link FacesContext#isPostback()} calls.
     */
    AFTER_PHASE_EXCLUDE_POSTBACK

}
