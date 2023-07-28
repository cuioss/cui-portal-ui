package de.icw.cui.portal.ui.errorpages;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.annotation.Priority;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletResponse;

import de.cuioss.jsf.api.servlet.ServletAdapterUtil;
import de.cuioss.portal.configuration.common.PortalPriorities;
import de.cuioss.portal.core.storage.MapStorage;
import de.cuioss.portal.core.storage.PortalSessionStorage;
import de.cuioss.portal.ui.api.exceptions.DefaultErrorMessage;
import de.cuioss.portal.ui.api.ui.pages.ErrorPage;
import de.cuioss.portal.ui.api.ui.pages.PortalCorePagesError;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Portal base implementation of {@link ErrorPage} <em>Note:</em> It is assumed
 * that the {@link DefaultErrorMessage} can be derived from the
 * {@link PortalSessionStorage} with the key
 * {@link DefaultErrorMessage#LOOKUP_KEY}. While retrieving the
 * {@link DefaultErrorMessage} it will implicitly be removed.
 *
 * @author Oliver Wolff
 */
@PortalCorePagesError
@RequestScoped
@Priority(PortalPriorities.PORTAL_CORE_LEVEL)
@Named(ErrorPage.BEAN_NAME)
@EqualsAndHashCode(of = "message", doNotUseGetters = true)
@ToString(of = "message", doNotUseGetters = true)
public class ErrorPageBean implements ErrorPage {

    private static final long serialVersionUID = -3785494532638995890L;

    @Inject
    @PortalSessionStorage
    private MapStorage<Serializable, Serializable> mapStorage;

    @Inject
    private FacesContext context;

    @Getter
    private DefaultErrorMessage message;

    /**
     * Retrieve and removes the {@link DefaultErrorMessage} found under the key
     * {@link DefaultErrorMessage#LOOKUP_KEY}
     */
    @PostConstruct
    public void init() {
        if (mapStorage.containsKey(DefaultErrorMessage.LOOKUP_KEY)) {
            message = (DefaultErrorMessage) mapStorage.remove(DefaultErrorMessage.LOOKUP_KEY);
        }
        ServletAdapterUtil.getResponse(context).setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }

    @Override
    public boolean isMessageAvailable() {
        return null != message;
    }

}
