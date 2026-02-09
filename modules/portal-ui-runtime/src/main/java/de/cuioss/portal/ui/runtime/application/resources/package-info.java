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
 * <h3>Provides classes dealing with resources</h3>
 * <p>
 * The corresponding modifications will only take place in production
 * environments. This will contain choosing the min version of resource and
 * adding a cache-buster to the resource request.
 * </p>
 * <p>
 * The {@link de.cuioss.portal.ui.runtime.application.resources.CuiResourceManager}
 * registers itself as an {@link jakarta.enterprise.context.ApplicationScoped} bean
 *
 * @author Oliver Wolff
 */
package de.cuioss.portal.ui.runtime.application.resources;
