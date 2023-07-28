/**
 * <p>
 * Provides the handling of the unified {@link java.util.ResourceBundle}
 * analogous to
 * {@link com.icw.ehf.cui.core.api.application.bundle.CuiResourceBundle}.
 * </p>
 * <h2>Usage</h2>
 * <p>
 * The central element is
 * {@link de.icw.cui.portal.configuration.application.bundle.PortalResourceBundleBean}
 * It is of type {@link java.util.ResourceBundle} and unifies all configured
 * {@link java.util.ResourceBundle}s for the portal. In order to use it within a
 * bean use
 *
 * <pre>
 *
 * &#064;Inject
 * &#064;PortalResourceBundle
 * private ResourceBundle resourceBundle;
 * </pre>
 *
 * It is exposed as well as named bean "msgs" and can therefore used within
 * xhtml as standard {@link java.util.ResourceBundle}:
 *
 * <pre>
 * {@code #(msgs['page.dashboard.title'])}
 * </pre>
 *
 * </p>
 * <h2>Configuration</h2>
 * <p>
 * Extending the {@link java.util.ResourceBundle}s is quite easy on a module
 * level. You only need to provide instance of
 * {@link de.cuioss.portal.ui.api.cdi.api.bundle.PortalResourceBundleDescriptor}
 * annotated with
 * {@link de.cuioss.portal.ui.api.cdi.api.bundle.ResourceBundleDescripor}. The
 * actual configuration will be done withn
 * {@link de.icw.cui.portal.configuration.bundles.impl.ResourceBundleRegistryImpl}
 * </p>
 */
package de.cuioss.portal.ui.runtime.application.bundle;
