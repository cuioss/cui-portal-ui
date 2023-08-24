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
package de.cuioss.portal.ui.api;

import static de.cuioss.tools.string.MoreStrings.requireNotEmpty;

import lombok.Getter;

/**
 * Global component id's
 *
 * @author i000576
 *
 */
public enum GlobalComponentIds {

    /**
     * Time out form identifier
     */
    TIMEOUT_FORM("timeoutForm"),

    /**
     * Top Navigation bar identifier
     */
    NAVIGATION_BAR_TOP("navbarTop"),

    /**
     * Top Navigation bar home link identifier
     */
    NAVIGATION_BAR_TOP_HOME("navbarTopHome"),

    /**
     * Global messages (Growl) identifier
     */
    GLOBAL_PAGE_MESSAGES("globalPageMessages"),

    /**
     * Main container identifer
     */
    MAIN_CONTENT("container"),

    /**
     * Login page username input field id
     */
    LOGIN_PAGE_USER_NAME("username"),

    /**
     * Login page password input field id
     */
    LOGIN_PAGE_USER_PASSWORD("password");

    /**
     * Component string identifier
     */
    @Getter
    private final String id;

    GlobalComponentIds(final String idValue) {
        id = requireNotEmpty(idValue, "idValue must not be null or empty");
    }

}
