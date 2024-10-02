/*
 * Copyright 2023 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * https://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.cuioss.portal.ui.api.test.support;

import java.io.Serial;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Set;

import jakarta.enterprise.context.Dependent;

import de.cuioss.portal.common.bundle.ResourceBundleWrapper;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Mock variant of {@link ResourceBundleWrapper}. Simulate
 * {@link #getString(String)} by simply returning
 * the key (like PortalMessageProducerMock). Calls to {@link #getKeys()} will return an
 * empty list.
 *
 * @author Oliver Wolff
 */
@Dependent
@EqualsAndHashCode
@ToString
public class PortalResourceBundleMock implements ResourceBundleWrapper {

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
        return null;
    }

    @Override
    public String getBundleContent() {
        return null;
    }
}
