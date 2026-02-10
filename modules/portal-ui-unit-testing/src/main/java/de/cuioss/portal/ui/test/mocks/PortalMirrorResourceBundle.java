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
package de.cuioss.portal.ui.test.mocks;

import de.cuioss.portal.common.bundle.ResourceBundleWrapper;
import jakarta.enterprise.context.Dependent;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serial;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Set;

/**
 * Mock variant of {@link ResourceBundleWrapper}. Simulate
 * {@link #getString(String)} by simply returning
 * the given key. Calls to {@link #getKeys()} will return an empty list.
 *
 * @author Oliver Wolff
 */
@Dependent
@EqualsAndHashCode(callSuper = false)
@ToString
public class PortalMirrorResourceBundle implements ResourceBundleWrapper {

    @Serial
    private static final long serialVersionUID = 3953649686127640297L;

    @Override
    public Enumeration<String> getKeys() {
        return Collections.emptyEnumeration();
    }

    @Override
    public String getString(String key) {
        return key;
    }

    @Override
    public Set<String> keySet() {
        return Collections.emptySet();
    }

    @Override
    public String getBundleContent() {
        return getClass().getName();
    }
}
