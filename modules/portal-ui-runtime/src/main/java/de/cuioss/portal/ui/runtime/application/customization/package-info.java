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
 * Allows to customize an installation. To activate either create a
 * "customization" folder inside
 * {@link de.cuioss.portal.configuration.PortalConfigurationKeys#PORTAL_CONFIG_DIR}
 * or configure
 * {@link de.cuioss.portal.configuration.PortalConfigurationKeys#PORTAL_CUSTOMIZATION_DIR}.
 * </p>
 * <p>
 * Within this folder a specific structure is expected:
 * <ul>
 * <li>/i18n: To customize message properties</li>
 * <li>/resources: To customize resources like images, css, composite
 * components...</li>
 * <li>/templates: To customize templates like master_centered.xhtml</li>
 * <li>/views: To customize views like guest/login.xhtml (which is maintained in
 * cui-portal as /guest/login.xhtml). implicit conventions : overwrite only
 * possible up from /faces folder!</li>
 * </ul>
 */
package de.cuioss.portal.ui.runtime.application.customization;
