package de.cuioss.portal.ui.api.ui.context;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.faces.event.PhaseId;
import javax.inject.Qualifier;

import de.cuioss.jsf.api.common.view.ViewDescriptor;

/**
 * Identifier for the current jsf view, the representation is
 * {@link ViewDescriptor}.
 *
 * <em>Caution: </em> The scope is RequestScoped, therefore you must not inject
 * this before {@link PhaseId#RESTORE_VIEW}
 *
 * @author Oliver Wolff
 */
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER })
public @interface CuiCurrentView {

}
