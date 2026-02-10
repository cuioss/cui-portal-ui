/*
 * Copyright © 2025 CUI-OpenSource-Software (info@cuioss.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/**
 * Provides ways to unify the handling of locales. The
 * {@link de.cuioss.portal.ui.runtime.application.locale.PortalLocaleManagerBean}
 * is about tracking and interaction of client-specific locales. The
 * implementation at portal level is a fixed (CDI-) scoped bean.
 * <p>
 * In order to change behavior the extension point is an implementation of
 * {@link de.cuioss.portal.ui.api.locale.LocaleResolverService} The default
 * implementation
 * {@link de.cuioss.portal.ui.runtime.application.locale.impl.PortalLocaleResolverServiceImpl}
 * uses the jsf based in standard behavior.
 * </p>
 * <p>
 * In addition it provides a producer method for
 * {@link de.cuioss.portal.common.locale.PortalLocale} To put in other words: This
 * bean provides a session-scoped cache for locale, the used service is agnostic
 * of state or bean specific types.
 * </p>
 * <p>
 * The
 * {@link de.cuioss.portal.ui.runtime.application.locale.PortalLocaleManagerBean}
 * fires an {@link de.cuioss.portal.common.locale.LocaleChangeEvent} on changing
 * of the locale. If you want to listen to this event, the listener can be
 * implemented like:
 * </p>
 *
 * <pre>
 * {@code void actOnLocaleChangeEven(@Observes @LocaleChangeEvent Locale newLocale)}
 * </pre>
 */
package de.cuioss.portal.ui.runtime.application.locale;
