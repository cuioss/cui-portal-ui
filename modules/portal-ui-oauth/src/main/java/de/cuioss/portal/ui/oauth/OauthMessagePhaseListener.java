package de.cuioss.portal.ui.oauth;

import static de.cuioss.jsf.api.application.navigation.NavigationUtils.getCurrentView;
import static de.cuioss.jsf.api.servlet.ServletAdapterUtil.getResponse;
import static de.cuioss.portal.ui.oauth.WrappedOauthFacadeImpl.MESSAGES_IDENTIFIER;
import static javax.faces.event.PhaseId.RENDER_RESPONSE;

import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.inject.Inject;
import javax.inject.Provider;
import javax.servlet.http.HttpServletRequest;

import org.apache.deltaspike.jsf.api.listener.phase.JsfPhaseListener;

import de.cuioss.jsf.api.common.util.CheckContextState;
import de.cuioss.tools.logging.CuiLogger;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Restore {@link FacesMessage}s after a redirect to the oauth server and back
 * again. Before redirecting to the oauth server
 * {@link WrappedOauthFacadeImpl#preserveCurrentView(HttpServletRequest)} will
 * store the existing messages in the messing, and this class will restore them
 * before {@link PhaseId#RENDER_RESPONSE}.
 */
@JsfPhaseListener
@EqualsAndHashCode
@ToString
public class OauthMessagePhaseListener implements PhaseListener {

    private static final long serialVersionUID = 837984685534479200L;

    private static final CuiLogger log = new CuiLogger(OauthMessagePhaseListener.class);

    @Inject
    private Provider<HttpServletRequest> servletRequestProvider;

    @Override
    public void afterPhase(PhaseEvent event) {
        // NOP
    }

    @Override
    public void beforePhase(PhaseEvent event) {
        var context = event.getFacesContext();
        final var response = getResponse(context);
        if (log.isTraceEnabled()) {
            log.trace("currentView: {}", getCurrentView(context));
            log.trace("responseComplete: {}", context.getResponseComplete());
            log.trace("released: {}", context.isReleased());
            log.trace("postback: {}", context.isPostback());
            log.trace("committed: {}", response.isCommitted());
        }
        if (CheckContextState.isResponseNotComplete(context) && !response.isCommitted()
                && (null != servletRequestProvider.get().getSession(false))
                && (null != servletRequestProvider.get().getSession().getAttribute(MESSAGES_IDENTIFIER))) {
            @SuppressWarnings("unchecked")
            var messages = (List<FacesMessage>) servletRequestProvider.get().getSession()
                    .getAttribute(MESSAGES_IDENTIFIER);
            log.trace("restore messages: {}", messages);
            messages.forEach(message -> event.getFacesContext().addMessage(null,
                    // because the old message may already be rendered (and the rendered flag was
                    // set) we need to reset it
                    new FacesMessage(message.getSeverity(), message.getSummary(), message.getDetail())));
            servletRequestProvider.get().getSession().removeAttribute(MESSAGES_IDENTIFIER);
        }
    }

    @Override
    public PhaseId getPhaseId() {
        return RENDER_RESPONSE;
    }
}
