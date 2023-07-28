package de.cuioss.portal.ui.runtime.common;

import static de.cuioss.portal.configuration.PortalConfigurationKeys.PORTAL_SESSION_MAX_INACTIVE_INTERVAL;
import static de.cuioss.portal.configuration.PortalConfigurationKeys.PORTAL_SESSION_TIMEOUT;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import de.cuioss.portal.ui.runtime.application.view.ViewTransientManagerBean;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Session bean to control over timeout.
 *
 * @author Oliver Wolff
 */
@Named(ClientWindowManager.BEAN_NAME)
@SessionScoped
@EqualsAndHashCode(of = "maxInactiveInterval", doNotUseGetters = true)
@ToString(of = "maxInactiveInterval", doNotUseGetters = true)
public class ClientWindowManager implements Serializable {

    /**
     * Bean name for looking up instances using EL.
     */
    public static final String BEAN_NAME = "clientWindowManager";

    private static final long serialVersionUID = 8603571267932838043L;

    @Inject
    @ConfigProperty(name = PORTAL_SESSION_MAX_INACTIVE_INTERVAL)
    private Integer maxInactiveInterval;

    @Inject
    @ConfigProperty(name = PORTAL_SESSION_TIMEOUT)
    private Integer sessionTimeout;

    @Inject
    private Provider<ViewTransientManagerBean> viewTransientManagerProvider;

    /**
     * @return boolean indicating whether to render the timeout-form
     */
    public boolean isRenderTimeoutForm() {
        return !viewTransientManagerProvider.get().isTransientView() && 0 != getMaxInactiveInterval();
    }

    /**
     * @return the interval to be used for determining after which time the
     *         corresponding window should show the timeout-popup
     */
    public int getMaxInactiveInterval() {
        if (maxInactiveInterval > -1) {
            return maxInactiveInterval;
        }
        return sessionTimeout;
    }
}
