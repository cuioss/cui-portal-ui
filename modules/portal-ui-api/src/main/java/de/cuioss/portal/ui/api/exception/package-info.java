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
 * Provides methods and structure user for handling of exceptions.
 * <p>
 * In general it is a simplified variant of deltaspikes exception-handling
 * <p>
 * The preferred way of exception handling is:
 * <ul>
 * <li>Fire an event of type
 * {@link de.cuioss.portal.ui.api.exception.ExceptionAsEvent} with the
 * corresponding {@link java.lang.Throwable} as payload</li>
 * <li>Provide an instance of
 * {@link de.cuioss.portal.ui.api.exception.PortalExceptionHandler} as a
 * {@link jakarta.enterprise.context.RequestScoped} bean</li>
 * <li>The rest will be done by the framework. For examples see implementations
 * within 'portal-ui-runtime', e.g.
 * 'de.cuioss.portal.ui.runtime.exception.ViewRelatedExceptionHandler' or
 * 'de.cuioss.portal.ui.oauth.OauthExceptionHandler' in 'portal-ui-oauth'</li>
 * </ul>
 *
 * @author Oliver Wolff
 */
package de.cuioss.portal.ui.api.exception;
