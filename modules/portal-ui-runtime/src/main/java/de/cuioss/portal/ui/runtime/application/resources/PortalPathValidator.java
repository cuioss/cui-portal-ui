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
package de.cuioss.portal.ui.runtime.application.resources;

import de.cuioss.http.security.config.SecurityConfiguration;
import de.cuioss.http.security.core.HttpSecurityValidator;
import de.cuioss.http.security.exceptions.UrlSecurityException;
import de.cuioss.http.security.monitoring.SecurityEventCounter;
import de.cuioss.http.security.pipeline.PipelineFactory;
import de.cuioss.tools.logging.CuiLogger;

import static de.cuioss.portal.ui.runtime.PortalUiRuntimeLogMessages.*;

/**
 * Validates resource paths against path traversal and other URL-based attacks
 * using the cui-http security validation framework.
 * <p>
 * This validator is designed to be used wherever user-controlled paths are
 * used to resolve classpath or filesystem resources. It wraps the
 * {@link de.cuioss.http.security.pipeline.URLPathValidationPipeline} with
 * a simple boolean API suitable for resource handler usage.
 *
 * @author Oliver Wolff
 */
public class PortalPathValidator {

    private static final CuiLogger LOGGER = new CuiLogger(PortalPathValidator.class);

    private final HttpSecurityValidator pathValidator;
    private final SecurityEventCounter eventCounter;

    public PortalPathValidator() {
        eventCounter = new SecurityEventCounter();
        var config = SecurityConfiguration.defaults();
        pathValidator = PipelineFactory.createUrlPathPipeline(config, eventCounter);
    }

    /**
     * Validates the given path for security issues such as path traversal,
     * null bytes, encoded attacks, etc.
     *
     * @param path the path to validate, may be null
     * @return {@code true} if the path is safe to use, {@code false} otherwise
     */
    public boolean isValidPath(String path) {
        if (null == path || path.isEmpty()) {
            return false;
        }
        try {
            pathValidator.validate(path);
            return true;
        } catch (UrlSecurityException e) {
            LOGGER.warn(WARN.PORTAL_150_MALICIOUS_PATH, path, e.getMessage());
            return false;
        }
    }
}
