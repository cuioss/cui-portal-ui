/**
 * Provides the portal specific implementation of
 * {@link com.icw.ehf.cui.application.history.HistoryManager}. Usually the
 * default implementation should suffice, it only needs the outcome. It uses
 * {@link de.icw.cui.portal.configuration.application.history.DefaultHistoryConfiguration}
 * of configuration. {@link de.cuioss.portal.ui.api.ui.pages.HomePage#OUTCOME}
 * 'home' to be set in order to work. If you need a different configuration your
 * module should. In addition the web.xml parameter
 * {@link de.icw.cui.portal.configuration.PortalConfigurationKeys#HISTORY_EXCLUDE_PARAMETER}
 * will be evaluated. If you need a more fine grained control you need to
 * provide an instance of
 * {@link com.icw.ehf.cui.application.history.HistoryConfiguration} annotated
 * with {@link de.cuioss.portal.ui.api.history.PortalHistoryConfiguration}
 */
package de.cuioss.portal.ui.runtime.application.history;
