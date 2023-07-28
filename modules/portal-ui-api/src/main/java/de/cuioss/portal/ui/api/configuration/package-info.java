/**
 * <p>
 * Provides a unified view on the the web.xml based configuration of the
 * CDI-portal. The configuration of the CDI-portal can be done in two ways:
 * </p>
 * <h2>web.xml</h2>
 * <p>
 * This is the simple way to configure the portal by using entries in the
 * web.xml, the corresponding keys / descriptions can be found within
 * {@link de.icw.cui.portal.configuration.PortalConfigurationKeys}. This should
 * suffice the most needs.
 * </p>
 * <h2>CDI-Way</h2>
 * <p>
 * For some cased the file based may not be enough. Therefore you can override
 * the corresponding services providing the corresponding configuration:
 * <ul>
 * <li>Resource Configuration: Provide an instance of
 * {@link com.icw.ehf.cui.application.resources.CuiResourceConfiguration}
 * annotated with
 * {@link de.cuioss.portal.ui.api.configuration.PortalResourceConfiguration}</li>
 * <li>Theme Configuration: Provide an instance of
 * {@link com.icw.ehf.cui.core.api.application.theme.ThemeConfiguration}
 * annotated with
 * {@link de.cuioss.portal.ui.api.theme.PortalThemeConfiguration}</li>
 * <li>History Configuration: Provide an instance of
 * {@link com.icw.ehf.cui.application.history.HistoryConfiguration} annotated
 * with {@link de.cuioss.portal.ui.api.history.PortalHistoryConfiguration}</li>
 * </ul>
 * </p>
 * <h2>Advanced CDI</h2>
 * <p>
 * If you want to replace the complete mechanism of web.xml configuration you
 * need only to create an alternate producer for
 * {@link com.icw.ehf.cui.cdi.api.context.CuiInitParameterMap} containing all
 * corresponding parameter.
 * </p>
 */
package de.cuioss.portal.ui.api.configuration;
