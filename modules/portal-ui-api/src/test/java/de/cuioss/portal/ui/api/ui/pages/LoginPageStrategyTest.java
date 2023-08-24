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
package de.cuioss.portal.ui.api.ui.pages;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class LoginPageStrategyTest {

    @Test
    void shouldReturnStrategyOnExisitingName() {
        assertEquals(LoginPageStrategy.GOTO_HOME,
                LoginPageStrategy.getFromString(LoginPageStrategy.GOTO_HOME.getStrategyName()));
        assertEquals(LoginPageStrategy.LOGOUT,
                LoginPageStrategy.getFromString(LoginPageStrategy.LOGOUT.getStrategyName()));

        assertEquals(LoginPageStrategy.GOTO_HOME,
                LoginPageStrategy.getFromString(LoginPageStrategy.GOTO_HOME.getStrategyName().toUpperCase()));
        assertEquals(LoginPageStrategy.LOGOUT,
                LoginPageStrategy.getFromString(LoginPageStrategy.LOGOUT.getStrategyName().toUpperCase()));
    }

    @Test
    void shouldFailOnInvalidName() {
        assertThrows(IllegalArgumentException.class, () -> {
            LoginPageStrategy.getFromString("NoThere");
        });
    }

}
