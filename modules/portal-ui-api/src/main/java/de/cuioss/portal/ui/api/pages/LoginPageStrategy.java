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
package de.cuioss.portal.ui.api.pages;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static de.cuioss.tools.string.MoreStrings.requireNotEmpty;

/**
 * @author Oliver Wolff
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum LoginPageStrategy {

    /**
     * Defines that the login-page should redirect home.
     */
    GOTO_HOME("goto_home"),

    /**
     * Defines that the login-page should log out if user is already logged in.
     */
    LOGOUT("logout");

    @Getter
    private final String strategyName;

    /**
     * Factory method for creating {@link LoginPageStrategy} instances from a given
     * String
     *
     * @param loginPageStrategyName must not be null and represent an instance of
     *                              {@link LoginPageStrategy#values()}
     * @return the found {@link LoginPageStrategy}
     * @throws IllegalArgumentException if no strategy can be found
     */
    public static LoginPageStrategy getFromString(final String loginPageStrategyName) {
        requireNotEmpty(loginPageStrategyName, "loginPageStrategyName");
        var lowercaseName = loginPageStrategyName.toLowerCase();
        for (LoginPageStrategy strategy : LoginPageStrategy.values()) {
            if (strategy.strategyName.equals(lowercaseName)) {
                return strategy;
            }
        }
        throw new IllegalArgumentException("No loginPageStrategyName found for " + loginPageStrategyName);
    }
}
