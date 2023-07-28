package de.cuioss.portal.ui.api.listener.view;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.inject.Qualifier;

/**
 * Marker identifying concrete instances of {@link ViewListener}, that are to be
 * fired at {@link PhaseId#RESTORE_VIEW}
 *
 * @author Oliver Wolff
 */
@Qualifier
@Retention(RUNTIME)
@Target({ TYPE, METHOD, FIELD, PARAMETER })
public @interface PortalRestoreViewListener {

    /**
     * @return {@link PhaseExecution} indicating whether a concrete Portal-listener
     *         is to be fired
     *         {@link PhaseListener#beforePhase(javax.faces.event.PhaseEvent)} of
     *         {@link PhaseListener#afterPhase(javax.faces.event.PhaseEvent)}
     */
    PhaseExecution value();
}
