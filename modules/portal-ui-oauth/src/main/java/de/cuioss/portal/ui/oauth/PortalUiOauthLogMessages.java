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

import de.cuioss.tools.logging.LogRecord;
import de.cuioss.tools.logging.LogRecordModel;
import lombok.experimental.UtilityClass;

/**
 * Centralized log messages for the portal-ui-oauth module.
 */
@UtilityClass
public final class PortalUiOauthLogMessages {

    public static final String PREFIX = "PORTAL-UI-OAUTH";

    @UtilityClass
    public static final class WARN {

        /** Missing URL parameter name for post-logout redirect URI. */
        public static final LogRecord MISSING_LOGOUT_REDIRECT_PARAM = LogRecordModel.builder()
                .template("postLogoutRedirectUri set, but no url-parameter name. Set via: %s")
                .prefix(PREFIX)
                .identifier(100)
                .build();
    }

    @UtilityClass
    public static final class ERROR {

        /** Unable to resolve CDI bean for OAuth authentication. */
        public static final LogRecord BEAN_RESOLUTION_FAILED = LogRecordModel.builder()
                .template("Unable to resolve bean of type '%s' with qualifier '%s'")
                .prefix(PREFIX)
                .identifier(200)
                .build();
    }
}
