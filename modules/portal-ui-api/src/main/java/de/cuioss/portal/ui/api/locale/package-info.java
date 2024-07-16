/**
 * Defines ways to unify the handling of locales.
 * {@link de.cuioss.portal.ui.api.locale.LocaleResolverService}
 * <ul>
 * <li>{@link de.cuioss.portal.common.locale.PortalLocale} acts as a marker for the
 * concrete request scoped locale. the portal must provide a corresponding
 * producer.</li>
 * <li>{@link de.cuioss.portal.ui.api.locale.LocaleResolverService}: Defines the
 * interaction part of dealing with locales. This is the intended extension
 * point.</li>
 * </ul>
 */
package de.cuioss.portal.ui.api.locale;
