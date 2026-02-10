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
package de.cuioss.portal.ui.oauth;

import de.cuioss.tools.logging.CuiLogger;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

public class MissingScopesException extends RuntimeException {

    /**
     *
     */
    @Serial
    private static final long serialVersionUID = 8581994138550480544L;

    private static final CuiLogger LOGGER = new CuiLogger(MissingScopesException.class);

    @Getter
    @Setter
    private String missingScopes;

    public MissingScopesException(String missingScopes) {
        this.missingScopes = missingScopes;
        LOGGER.debug("MissingScopesException: %s", missingScopes);
    }
}
