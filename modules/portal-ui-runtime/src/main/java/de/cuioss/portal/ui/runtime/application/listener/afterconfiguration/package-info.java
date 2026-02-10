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
 * Contains
 * {@link de.cuioss.portal.configuration.initializer.ApplicationInitializer}
 * that are responsible for dealing with corner cases within the portal, e.g.
 * {@link de.cuioss.portal.ui.runtime.application.templating.PortalViewMapper}
 * that needs to be initialized after the configuration system is up and running
 * and before JSF-specific classes like
 * {@link jakarta.faces.application.Application} access the corresponding beans
 * using the {@link jakarta.el.ELResolver}
 *
 * @author Oliver Wolff
 */
package de.cuioss.portal.ui.runtime.application.listener.afterconfiguration;
