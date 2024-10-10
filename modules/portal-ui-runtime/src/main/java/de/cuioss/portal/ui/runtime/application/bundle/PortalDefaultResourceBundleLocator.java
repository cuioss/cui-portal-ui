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

import de.cuioss.portal.common.bundle.ResourceBundleLocator;
import de.cuioss.portal.common.priority.PortalPriorities;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.util.Optional;

/**
 * Defines the base bundles to be defined within a portal application:
 * "de.cuioss.portal.ui.i18n.portal-messages"
 *
 * @author Matthias Walliczek
 */
@Priority(PortalPriorities.PORTAL_CORE_LEVEL)
@ApplicationScoped
@EqualsAndHashCode
public class PortalDefaultResourceBundleLocator implements ResourceBundleLocator {

    @Serial
    private static final long serialVersionUID = -8478481710191113463L;

    private static final String PATH = "de.cuioss.portal.ui.i18n.portal-messages";

    @Override
    public Optional<String> getBundlePath() {
        return Optional.of(PATH);
    }

    @Override
    public String toString() {
        return "%s: Path='%s'".formatted(getClass().getName(), PATH);
    }
}
