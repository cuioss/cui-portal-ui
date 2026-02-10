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
package de.cuioss.portal.ui.errorpages;

import de.cuioss.tools.logging.LogRecord;
import de.cuioss.tools.logging.LogRecordModel;
import lombok.experimental.UtilityClass;

/**
 * Centralized log messages for the portal-ui-errorpages module.
 */
@UtilityClass
public final class PortalUiErrorPagesLogMessages {

    public static final String PREFIX = "PORTAL-UI-ERR";

    @UtilityClass
    public static final class WARN {

        /** Portal-137: HTTP error page raised. */
        public static final LogRecord PORTAL_137_HTTP_ERROR = LogRecordModel.builder()
                .template("Portal-137: Http-Error '%s' for requested-uri '%s' was raised")
                .prefix(PREFIX)
                .identifier(100)
                .build();
    }
}
