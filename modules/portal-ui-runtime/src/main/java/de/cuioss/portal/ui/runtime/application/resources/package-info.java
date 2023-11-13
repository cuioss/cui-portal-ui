/**
 * <h3>Provides classes dealing with resources</h3>
 * <p>
 * The corresponding modifications will only take place in production
 * environments. This will contain choosing the min version of resource and
 * adding a cache-buster to the resource request.
 * </p>
 * <p>
 * In order to work it needs the corresponding beans available:
 * {@link de.cuioss.portal.ui.runtime.application.resources.CuiResourceManager}
 * with the name
 * {@link de.cuioss.portal.ui.runtime.application.resources.CuiResourceManager#BEAN_NAME}
 * and an implementation of
 * {@link de.cuioss.portal.ui.runtime.application.resources.CuiResourceConfiguration}
 * with the name
 * {@link de.cuioss.portal.ui.runtime.application.resources.impl.CuiResourceConfigurationImpl#BEAN_NAME}
 * </p>
 * <p>
 * The
 * {@link de.cuioss.portal.ui.runtime.application.resources.CuiResourceManager}
 * registers itself as an {@link javax.faces.bean.ApplicationScoped} bean
 *
 * @author Oliver Wolff
 *
 */
package de.cuioss.portal.ui.runtime.application.resources;
