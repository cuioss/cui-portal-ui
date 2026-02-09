/**
 * <p>
 * Provides classes for configuring the cui defined theming.
 * </p>
 * <h2>Theming within the Portal</h2>
 * <p>
 * The theming is implemented by loading different css files that represent a
 * concrete theme.
 * </p>
 * <p>
 * The actual themes are created by the styling project. They are named
 * application-default.css, application-black.css, .. and are to be found in the
 * library "de.cuioss.portal.css"
 * </p>
 * <p>
 * {@link de.cuioss.portal.ui.runtime.application.theme.ThemeResourceHandler} Is
 * the actual {@link jakarta.faces.application.ResourceHandler} delivering the css
 * to the client. In order to do so it needs instances of
 * {@link de.cuioss.portal.ui.runtime.application.theme.UserThemeBean} to be
 * present. It defines which theme to be loaded and creates the actual name of
 * the corresponding css to be delivered.<br>
 * The resourceHandler needs to be registered in the application element of the
 * faces-config:
 *
 * <pre>
 * {@code <resource-handler>de.cuioss.portal.ui.runtime.application.theme.ThemeResourceHandler</resource-handler>}
 * </pre>
 * <p>
 * The configuration keys are:
 * <ul>
 * <li>
 * {@link de.cuioss.portal.configuration.PortalConfigurationKeys#THEME_AVAILABLE}
 * </li>
 * <li>
 * {@link de.cuioss.portal.configuration.PortalConfigurationKeys#THEME_DEFAULT}
 * </li>
 * </ul>
 *
 * @author Oliver Wolff
 */
package de.cuioss.portal.ui.runtime.application.theme;
