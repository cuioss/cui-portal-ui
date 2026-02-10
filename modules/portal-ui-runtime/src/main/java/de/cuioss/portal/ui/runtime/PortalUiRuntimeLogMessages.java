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
package de.cuioss.portal.ui.runtime;

import de.cuioss.tools.logging.LogRecord;
import de.cuioss.tools.logging.LogRecordModel;
import lombok.experimental.UtilityClass;

/**
 * Centralized log messages for the portal-ui-runtime module.
 * Uses the DSL-style constants pattern with LogRecord for structured logging.
 *
 * <p>Portal identifier mapping:
 * <ul>
 *   <li>Portal-103: Insufficient permissions for view</li>
 *   <li>Portal-111: Unspecified exception with view context</li>
 *   <li>Portal-112: Unspecified exception without view context</li>
 *   <li>Portal-113: Invalidated session detected</li>
 *   <li>Portal-122: Unable to search/access path</li>
 *   <li>Portal-126: Template not found</li>
 *   <li>Portal-127: View not found</li>
 *   <li>Portal-128: Invalid configuration key</li>
 *   <li>Portal-129: Invalid configuration value</li>
 *   <li>Portal-130: Error on error page</li>
 *   <li>Portal-144: View/template resource cannot be resolved</li>
 *   <li>Portal-145: Customization resource cannot be resolved</li>
 *   <li>Portal-150: Path traversal / invalid path rejected</li>
 *   <li>Portal-502: Unable to recreate session</li>
 *   <li>Portal-505: Navigation loop detected</li>
 * </ul>
 */
@UtilityClass
public final class PortalUiRuntimeLogMessages {

    public static final String PREFIX = "PORTAL-UI-RT";

    @UtilityClass
    public static final class INFO {

        /** No installation-specific customization detected. */
        public static final LogRecord CUSTOMIZATION_DEFAULTS = LogRecordModel.builder()
                .template("No installation specific customization detected, using defaults. If this is intentional you can ignore this message")
                .prefix(PREFIX)
                .identifier(1)
                .build();

        /** Custom messages resource bundle found. */
        public static final LogRecord CUSTOM_MESSAGES_FOUND = LogRecordModel.builder()
                .template("Custom messages found at '%s'.")
                .prefix(PREFIX)
                .identifier(2)
                .build();

        /** Custom messages resource bundle not found. */
        public static final LogRecord CUSTOM_MESSAGES_NOT_FOUND = LogRecordModel.builder()
                .template("Custom messages not found at '%s', ignoring.")
                .prefix(PREFIX)
                .identifier(3)
                .build();

        /** Searching customization resources in folder. */
        public static final LogRecord SEARCHING_CUSTOMIZATION_RESOURCES = LogRecordModel.builder()
                .template("Searching customization resources in folder %s")
                .prefix(PREFIX)
                .identifier(4)
                .build();

        /** Found customization resources. */
        public static final LogRecord FOUND_RESOURCES = LogRecordModel.builder()
                .template("Found resources: %s")
                .prefix(PREFIX)
                .identifier(5)
                .build();
    }

    @UtilityClass
    public static final class WARN {

        /** Portal-103: Insufficient permissions for view access. */
        public static final LogRecord PORTAL_103_INSUFFICIENT_PERMISSIONS = LogRecordModel.builder()
                .template("Portal-103: View '%s' requires the roles '%s', but user '%s' only has the roles: '%s'")
                .prefix(PREFIX)
                .identifier(100)
                .build();

        /** Portal-113: Invalidated session detected, trying to recreate. */
        public static final LogRecord PORTAL_113_INVALID_SESSION = LogRecordModel.builder()
                .template("Portal-113: Detected an invalidated session, trying to recreate, reason=%s")
                .prefix(PREFIX)
                .identifier(101)
                .build();

        /** Portal-122: Unable to search or access path. */
        public static final LogRecord PORTAL_122_UNABLE_TO_SEARCH_PATH = LogRecordModel.builder()
                .template("Portal-122: Unable to search path: %s")
                .prefix(PREFIX)
                .identifier(102)
                .build();

        /** Portal-122: Access denied to path. */
        public static final LogRecord PORTAL_122_ACCESS_DENIED = LogRecordModel.builder()
                .template("Portal-122: access denied to: %s")
                .prefix(PREFIX)
                .identifier(103)
                .build();

        /** Portal-126: Template not found. */
        public static final LogRecord PORTAL_126_TEMPLATE_NOT_FOUND = LogRecordModel.builder()
                .template("Portal-126: Template %s with path %s from descriptor %s was not found")
                .prefix(PREFIX)
                .identifier(104)
                .build();

        /** Portal-127: View not found. */
        public static final LogRecord PORTAL_127_VIEW_NOT_FOUND = LogRecordModel.builder()
                .template("Portal-127: View %s with path %s from descriptor %s was not found")
                .prefix(PREFIX)
                .identifier(105)
                .build();

        /** Portal-128: Invalid HTTP header configuration key. */
        public static final LogRecord PORTAL_128_INVALID_KEY = LogRecordModel.builder()
                .template("Portal-128: Invalid configuration key '%s'")
                .prefix(PREFIX)
                .identifier(106)
                .build();

        /** Portal-129: Invalid HTTP header configuration value. */
        public static final LogRecord PORTAL_129_INVALID_VALUE = LogRecordModel.builder()
                .template("Portal-129: Invalid configuration value '%s' for key '%s'")
                .prefix(PREFIX)
                .identifier(107)
                .build();

        /** Portal-144: View/template resource cannot be resolved. */
        public static final LogRecord PORTAL_144_RESOURCE_NOT_RESOLVED = LogRecordModel.builder()
                .template("Portal-144: Configured view/template resource '%s' can not be resolved, skipped")
                .prefix(PREFIX)
                .identifier(108)
                .build();

        /** Portal-145: Customization resource cannot be resolved to URL. */
        public static final LogRecord PORTAL_145_CUSTOMIZATION_UNRESOLVABLE = LogRecordModel.builder()
                .template("Portal-145: Customization resource '%s' can not be resolved to an URL")
                .prefix(PREFIX)
                .identifier(109)
                .build();

        /** Portal-150: Path traversal attempt rejected. */
        public static final LogRecord PORTAL_150_PATH_TRAVERSAL = LogRecordModel.builder()
                .template("Portal-150: Rejected path traversal attempt: '%s/%s'")
                .prefix(PREFIX)
                .identifier(110)
                .build();

        /** Portal-150: Potentially malicious path rejected. */
        public static final LogRecord PORTAL_150_MALICIOUS_PATH = LogRecordModel.builder()
                .template("Portal-150: Rejected potentially malicious path '%s': %s")
                .prefix(PREFIX)
                .identifier(111)
                .build();

        /** Portal-150: Invalid view resource path rejected. */
        public static final LogRecord PORTAL_150_INVALID_VIEW_RESOURCE_PATH = LogRecordModel.builder()
                .template("Portal-150: Rejected invalid view resource path: '%s'")
                .prefix(PREFIX)
                .identifier(112)
                .build();

        /** Portal-150: Invalid template resource path rejected. */
        public static final LogRecord PORTAL_150_INVALID_TEMPLATE_PATH = LogRecordModel.builder()
                .template("Portal-150: Rejected invalid template resource path: '%s'")
                .prefix(PREFIX)
                .identifier(113)
                .build();

        /** Portal-150: Invalid view path rejected. */
        public static final LogRecord PORTAL_150_INVALID_VIEW_PATH = LogRecordModel.builder()
                .template("Portal-150: Rejected invalid view path: '%s'")
                .prefix(PREFIX)
                .identifier(114)
                .build();

        /** Invalid locale configuration detected. */
        public static final LogRecord INVALID_LOCALE_CONFIG = LogRecordModel.builder()
                .template("Invalid configuration found for %s defaulting to %s")
                .prefix(PREFIX)
                .identifier(115)
                .build();

        /** Non-secured views configuration results in all views requiring authorization. */
        public static final LogRecord ALL_VIEWS_SECURED = LogRecordModel.builder()
                .template("The configuration of %s results in all views of the application being only accessible for authorized user. If this is intentional you can ignore this warning")
                .prefix(PREFIX)
                .identifier(116)
                .build();

        /** Unable to determine view during listener execution. */
        public static final LogRecord UNABLE_TO_DETERMINE_VIEW = LogRecordModel.builder()
                .template("Unable to determine view.")
                .prefix(PREFIX)
                .identifier(117)
                .build();

        /** Illegal state: phase already started. */
        public static final LogRecord PHASE_ALREADY_STARTED = LogRecordModel.builder()
                .template("IllegalState: Must only be started once, phaseName='%s'")
                .prefix(PREFIX)
                .identifier(118)
                .build();

        /** Illegal state: phase already stopped. */
        public static final LogRecord PHASE_ALREADY_STOPPED = LogRecordModel.builder()
                .template("IllegalState: Must only be stopped once, phase='%s'")
                .prefix(PREFIX)
                .identifier(119)
                .build();

        /** Exception handling failed due to invalid session. */
        public static final LogRecord EXCEPTION_HANDLING_INVALID_SESSION = LogRecordModel.builder()
                .template("Portal-113: Exception handling failed due to invalid session: %s")
                .prefix(PREFIX)
                .identifier(120)
                .build();
    }

    @UtilityClass
    public static final class ERROR {

        /** Portal-111: Unspecified exception with view context. */
        public static final LogRecord PORTAL_111_UNSPECIFIED_EXCEPTION = LogRecordModel.builder()
                .template("Portal-111: An unspecified exception has been caught and handled by fallback strategy while trying to access view %s, errorTicket=%s")
                .prefix(PREFIX)
                .identifier(200)
                .build();

        /** Portal-112: Unspecified exception without view context. */
        public static final LogRecord PORTAL_112_UNSPECIFIED_EXCEPTION_NO_VIEW = LogRecordModel.builder()
                .template("Portal-112: An unspecified exception has been caught and handled by fallback strategy")
                .prefix(PREFIX)
                .identifier(201)
                .build();

        /** Portal-128: Invalid HTTP header configuration key (error level). */
        public static final LogRecord PORTAL_128_INVALID_KEY = LogRecordModel.builder()
                .template("Portal-128: Invalid configuration key '%s'")
                .prefix(PREFIX)
                .identifier(202)
                .build();

        /** Portal-130: Error occurring on error page. */
        public static final LogRecord PORTAL_130_ERROR_ON_ERROR_PAGE = LogRecordModel.builder()
                .template("Portal-130: Previous error occurs on error page. This will lead to damaged output and is a sign of corrupted deployment")
                .prefix(PREFIX)
                .identifier(203)
                .build();

        /** Portal-502: Unable to recreate session. */
        public static final LogRecord PORTAL_502_SESSION_RECREATE_FAILED = LogRecordModel.builder()
                .template("Portal-502: Unable to recreate session, reason=%s")
                .prefix(PREFIX)
                .identifier(204)
                .build();

        /** Portal-505: Navigation loop detected. */
        public static final LogRecord PORTAL_505_NAV_LOOP = LogRecordModel.builder()
                .template("Portal-505: The view '%s' is suppressed but is the designated navigation target at the same time. This would result in a loop. The error page is displayed therefore instead.")
                .prefix(PREFIX)
                .identifier(205)
                .build();
    }
}
