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
 * Provides the deltaspike based exception handler for the portal, dealing with
 * the following exceptions:
 * {@link de.cuioss.portal.ui.runtime.exception.ViewRelatedExceptionHandler}:
 * <ul>
 * <li>{@link jakarta.faces.application.ViewExpiredException}</li>
 * <li>{@link de.cuioss.portal.ui.api.authentication.UserNotAuthenticatedException}</li>
 * <li>{@link de.cuioss.portal.ui.runtime.application.view.ViewSuppressedException}</li>
 * </ul>
 * Dealing means redirecting to
 * {@link de.cuioss.portal.ui.api.pages.HomePage#OUTCOME} or
 * {@link de.cuioss.portal.ui.api.pages.LoginPage#OUTCOME} depending on
 * {@link de.cuioss.portal.authentication.AuthenticatedUserInfo#isAuthenticated()}
 * <p>
 * {@link de.cuioss.portal.ui.runtime.exception.FallBackExceptionHandler}: Last line
 * of defense displaying the error page for all exceptions that are not handled.
 * It is deactivated on
 * {@link jakarta.faces.application.ProjectStage#Development}, because the default
 * facelets error page provides more default information for debugging.
 * </p>
 */
package de.cuioss.portal.ui.runtime.exception;
