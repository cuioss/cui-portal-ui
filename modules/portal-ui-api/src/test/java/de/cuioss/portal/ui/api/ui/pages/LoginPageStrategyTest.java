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
