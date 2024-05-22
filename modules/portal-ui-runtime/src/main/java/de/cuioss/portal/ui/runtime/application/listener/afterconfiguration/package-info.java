/**
 * Contains
 * {@link de.cuioss.portal.configuration.initializer.ApplicationInitializer}
 * that are responsible for dealing with corner cases within the portal, e.g.
 * {@link de.cuioss.portal.ui.runtime.application.templating.PortalViewMapper}
 * that needs to be initialized after the configuration system is up and running
 * and before JSF-specific classes like
 * {@link jakarta.faces.application.Application} access the corresponding beans
 * using the {@link jakarta.el.ELResolver}
 *
 * @author Oliver Wolff
 */
package de.cuioss.portal.ui.runtime.application.listener.afterconfiguration;
