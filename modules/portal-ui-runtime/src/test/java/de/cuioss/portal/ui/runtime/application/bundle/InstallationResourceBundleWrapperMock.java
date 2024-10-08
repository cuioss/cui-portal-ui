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
package de.cuioss.portal.ui.runtime.application.bundle;

import de.cuioss.portal.common.bundle.ResourceBundleWrapper;
import de.cuioss.portal.common.priority.PortalPriorities;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.SessionScoped;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serial;
import java.util.Set;

import static de.cuioss.tools.collect.CollectionLiterals.mutableSet;

@SessionScoped
@Priority(PortalPriorities.PORTAL_INSTALLATION_LEVEL)
@ToString
@EqualsAndHashCode
public class InstallationResourceBundleWrapperMock implements ResourceBundleWrapper {

    @Serial
    private static final long serialVersionUID = 8403682810059890158L;

    @Override
    public String getString(final String key) {
        if ("page.error.title".equals(key)) {
            return "Test";
        }
        throw new IllegalStateException();
    }

    @Override
    public Set<String> keySet() {
        return mutableSet("page.error.title");
    }

    @Override
    public String getBundleContent() {
        return toString();
    }

}
