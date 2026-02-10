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
package de.cuioss.portal.ui.api.exception;

/**
 * Defines the possible outcome of the concrete {@link PortalExceptionHandler}
 */
public enum HandleOutcome {

    /**
     * Indicates, that the event was solely logged
     */
    LOGGED,

    /**
     * Indicates, that the exception was thrown again, usually in context of
     * development
     */
    RE_THROWN,

    /**
     * Indicates, that a user-message was created / added
     */
    USER_MESSAGE,

    /**
     * Indicates, that the handler did nothing. Default for {@link ExceptionAsEvent}
     */
    NO_OP,

    /**
     * The handler executed a redirect
     */
    REDIRECT

}
