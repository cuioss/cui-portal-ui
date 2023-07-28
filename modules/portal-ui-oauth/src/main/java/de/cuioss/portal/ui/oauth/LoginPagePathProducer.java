package de.cuioss.portal.ui.oauth;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.faces.context.FacesContext;

import de.cuioss.jsf.api.application.navigation.NavigationUtils;
import de.cuioss.portal.authentication.oauth.LoginPagePath;
import de.cuioss.portal.ui.api.ui.pages.LoginPage;
import de.cuioss.tools.logging.CuiLogger;

@RequestScoped
public class LoginPagePathProducer {

    private static final CuiLogger log = new CuiLogger(LoginPagePathProducer.class);

    /** Default login page based on CUI-portal-oauth faces-config.xml */
    private static final String DEFAULT = "/faces/guest/login.xhtml";

    @Produces
    @LoginPagePath
    private String loginUrl = DEFAULT;

    /**
     * Initialize the bean.
     */
    @PostConstruct
    public void init() {
        final var context = FacesContext.getCurrentInstance();
        if (null != context) {
            loginUrl = NavigationUtils.lookUpToLogicalViewIdBy(context, LoginPage.OUTCOME);
        } else {
            log.debug("No FacesContext available. Using URI: {}", loginUrl);
        }
    }
}
