package de.cuioss.portal.ui.api.menu;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.inject.Qualifier;

import de.cuioss.jsf.api.components.model.menu.NavigationMenuItem;

/**
 * Marker identifying instances of the {@link NavigationMenuItem} that will be
 * autoregistered to instances of {@link NavigationMenuProvider}.
 *
 * @author Oliver Wolff
 */
@Qualifier
@Retention(RUNTIME)
@Target({ TYPE, METHOD, FIELD, PARAMETER })
public @interface PortalMenuItem {

}
