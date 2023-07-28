/**
 * Contains
 * {@link de.icw.cui.portal.configuration.initializer.ApplicationInitializer}
 * that are responsible for dealing with corner cases within the portal, e.g.
 * {@link de.icw.cui.portal.configuration.application.templating.PortalViewMapper}
 * that needs to be initialized after the configuration system is up and running
 * and before JSF-specific classes like
 * {@link javax.faces.application.Application} access the corresponding beans
 * using the {@link javax.el.ELResolver}
 *
 * @author Oliver Wolff
 */
package de.cuioss.portal.ui.runtime.application.listener.afterconfiguration;
