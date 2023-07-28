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
