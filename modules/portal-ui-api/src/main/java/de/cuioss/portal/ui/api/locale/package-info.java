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
