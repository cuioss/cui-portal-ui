package de.cuioss.portal.ui.runtime.page;

import static de.cuioss.portal.configuration.PortalConfigurationKeys.PAGES_LOGIN_ENTER_STRATEGY;

import java.io.Serializable;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import de.cuioss.portal.configuration.PortalConfigurationChangeEvent;
import de.cuioss.portal.ui.api.ui.pages.LoginPageStrategy;
import de.cuioss.tools.logging.CuiLogger;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Provides configurable behavior of pages, e.g. {@link LoginPageStrategy}
 *
 * @author Oliver Wolff
 */
@ApplicationScoped
@Named
@EqualsAndHashCode(of = "loginPageStrategy")
@ToString(of = "loginPageStrategy")
public class PortalPagesConfiguration implements Serializable {

    private static final long serialVersionUID = 6752537251543880784L;

    private static final CuiLogger log = new CuiLogger(PortalPagesConfiguration.class);

    @Inject
    @ConfigProperty(name = PAGES_LOGIN_ENTER_STRATEGY)
    private Provider<String> loginPageStrategyProvider;

    @Getter
    private LoginPageStrategy loginPageStrategy;

    /**
     * Initializes the bean, see class documentation for details
     */
    @PostConstruct
    public void initBean() {
        loginPageStrategy = LoginPageStrategy.getFromString(loginPageStrategyProvider.get());
    }

    /**
     * Listener for {@link PortalConfigurationChangeEvent}s. Reconfigures the
     * default-pages-configuration
     *
     * @param deltaMap
     */
    void configurationChangeEventListener(
            @Observes @PortalConfigurationChangeEvent final Map<String, String> deltaMap) {
        if (deltaMap.containsKey(PAGES_LOGIN_ENTER_STRATEGY)) {
            log.debug("Setting Reloading to {}", loginPageStrategyProvider.get());
            initBean();
        }
    }

}
