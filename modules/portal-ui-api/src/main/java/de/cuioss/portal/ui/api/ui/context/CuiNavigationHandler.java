package de.cuioss.portal.ui.api.ui.context;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.faces.application.NavigationHandler;
import javax.inject.Qualifier;

/**
 * Identifier for the {@link NavigationHandler}
 *
 * @author Oliver Wolff
 */
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER })
public @interface CuiNavigationHandler {

}
