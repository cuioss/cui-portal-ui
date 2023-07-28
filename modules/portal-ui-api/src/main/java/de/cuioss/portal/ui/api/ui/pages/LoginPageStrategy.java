package de.cuioss.portal.ui.api.ui.pages;

import static de.cuioss.tools.string.MoreStrings.requireNotEmpty;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author Oliver Wolff
 *
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum LoginPageStrategy {

    /** Defines that the login-page should redirect to home. */
    GOTO_HOME("goto_home"),

    /**
     * Defines that the login-page should logout if user is already logged in.
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
     *
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
