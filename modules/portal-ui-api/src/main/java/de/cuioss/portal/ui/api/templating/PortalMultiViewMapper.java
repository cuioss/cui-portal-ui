package de.cuioss.portal.ui.api.templating;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Qualifier;

/**
 * Marker for the portal provided default implementation of
 * {@link MultiViewMapper}. Used for injecting or overriding the portals
 * defaults implementation. It is @ApplicationScoped and
 *
 * @Named(MultiViewMapper.BEAN_NAME) @author Oliver Wolff
 */
@Qualifier
@ApplicationScoped
@Retention(RUNTIME)
@Target({ TYPE, METHOD, FIELD, PARAMETER })
public @interface PortalMultiViewMapper {

}
