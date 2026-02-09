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
package de.cuioss.portal.ui.runtime.application.storage;

import de.cuioss.portal.common.priority.PortalPriorities;
import de.cuioss.portal.core.servlet.CuiContextPath;
import de.cuioss.portal.core.storage.ClientStorage;
import de.cuioss.portal.core.storage.PortalClientStorage;
import de.cuioss.tools.string.MoreStrings;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.inject.Provider;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.io.Serial;
import java.util.HashMap;
import java.util.Map;

import static de.cuioss.portal.configuration.PortalConfigurationKeys.CLIENT_STORAGE_COOKIE_MAX_AGE;
import static de.cuioss.portal.ui.api.PortalCoreBeanNames.CLIENT_STORAGE_BEAN_NAME;
import static org.omnifaces.util.Faces.addResponseCookie;
import static org.omnifaces.util.Faces.getRequestCookie;
import static org.omnifaces.util.Faces.removeResponseCookie;

/**
 * Represents the request scoped runtime representation of
 * {@link PortalClientStorage}, using cookies theirs
 * {@link PortalClientStorage}, using cookies as storage.
 *
 * @author Matthias Walliczek
 */
@PortalClientStorage
@RequestScoped
@EqualsAndHashCode
@ToString
@Named(CLIENT_STORAGE_BEAN_NAME)
@Priority(PortalPriorities.PORTAL_CORE_LEVEL)
public class PortalClientStorageImpl implements ClientStorage {

    @Serial
    private static final long serialVersionUID = 1573344347429735050L;

    private final Map<String, String> cache = new HashMap<>();

    @Inject
    @ConfigProperty(name = CLIENT_STORAGE_COOKIE_MAX_AGE)
    private String cookieMaxAge;

    @Inject
    @CuiContextPath
    private Provider<String> contextPathProvider;

    @Override
    public String get(final String key) {
        if (!cache.containsKey(key)) {
            cache.put(key, getRequestCookie(key));
        }
        return cache.get(key);
    }

    @Override
    public String get(final String key, final String defaultValue) {
        final var value = get(key);
        if (MoreStrings.isEmpty(value)) {
            return defaultValue;
        }
        return value;
    }

    @Override
    public void put(final String key, final String object) {
        addResponseCookie(key, object, contextPathProvider.get(), Integer.parseInt(cookieMaxAge));
        cache.put(key, object);
    }

    @Override
    public String remove(final String key) {
        final var result = get(key);
        removeResponseCookie(key, contextPathProvider.get());
        cache.put(key, null);
        return result;
    }

    @Override
    public boolean containsKey(final String key) {
        return null != get(key);
    }

}
