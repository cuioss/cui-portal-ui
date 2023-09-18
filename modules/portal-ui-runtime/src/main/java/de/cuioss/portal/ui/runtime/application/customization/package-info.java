/**
 * <p>
 * Allows to customize an installation. To activate either create a
 * "customization" folder inside
 * {@link de.icw.cui.portal.configuration.PortalConfigurationKeys#PORTAL_CONFIG_DIR}
 * or configure
 * {@link de.icw.cui.portal.configuration.PortalConfigurationKeys#PORTAL_CUSTOMIZATION_DIR}.
 * </p>
 *
 * Within this folder a specific structure is expected:
 * <ul>
 * <li>/i18n: To customize message properties</li>
 * <li>/resources: To customize resources like images, css, composite
 * components...</li>
 * <li>/templates: To customize templates like master_centered.xhtml</li>
 * <li>/views: To customize views like guest/login.xhtml (which is maintained in
 * cui-portal as /guest/login.xhtml). implicit conventions : overwrite
 * only possible up from /faces folder!</li>
 * </ul>
 *
 */
package de.cuioss.portal.ui.runtime.application.customization;
