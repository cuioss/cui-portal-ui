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
 * <p>
 * Provides the handling of the unified {@link java.util.ResourceBundle}
 * analogous to
 * {@link de.cuioss.jsf.api.application.bundle.CuiJSfResourceBundleLocator}.
 * </p>
 * <h2>Usage</h2>
 * <p>
 * The central element is
 * {@link de.cuioss.portal.common.bundle.PortalResourceBundleBean}
 * It is of type {@link java.util.ResourceBundle} and unifies all configured
 * {@link java.util.ResourceBundle}s for the portal. In order to use it within a
 * bean use:
 * </p>
 *
 * <pre>
 *
 * &#064;Inject
 * &#064;UnifiedResourceBundle
 * private ResourceBundle resourceBundle;
 * </pre>
 * <p>
 * It is exposed as well as named bean "msgs" and can therefore used within
 * xhtml as standard {@link java.util.ResourceBundle}:
 *
 * <pre>
 * {@code #(msgs['page.dashboard.title'])}
 * </pre>
 *
 * <h2>Configuration</h2>
 * <p>
 * Extending the {@link java.util.ResourceBundle}s is quite easy on a module
 * level. You only need to provide instance of
 * {@link de.cuioss.portal.common.bundle.ResourceBundleLocator}
 * actual configuration will be done with
 * {@link de.cuioss.portal.common.bundle.ResourceBundleRegistry}
 * </p>
 */
package de.cuioss.portal.ui.runtime.application.bundle;
