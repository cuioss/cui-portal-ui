package de.cuioss.portal.ui.oauth;

import de.cuioss.tools.logging.CuiLogger;
import lombok.Getter;
import lombok.Setter;

public class MissingScopesException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = 8581994138550480544L;

    private static final CuiLogger log = new CuiLogger(MissingScopesException.class);

    @Getter
    @Setter
    private String missingScopes;

    public MissingScopesException(String missingScopes) {
        this.missingScopes = missingScopes;
        log.debug("MissingScopesException: {}", missingScopes);
    }
}
