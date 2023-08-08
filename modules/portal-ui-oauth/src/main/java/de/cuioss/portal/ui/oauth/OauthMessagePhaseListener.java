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
import javax.servlet.http.HttpServletRequest;

import de.cuioss.jsf.api.common.util.CheckContextState;
import de.cuioss.jsf.api.servlet.ServletAdapterUtil;
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
@EqualsAndHashCode
@ToString
public class OauthMessagePhaseListener implements PhaseListener {

    private static final long serialVersionUID = 837984685534479200L;

    private static final CuiLogger log = new CuiLogger(OauthMessagePhaseListener.class);

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
        var session = ServletAdapterUtil.getSession(event.getFacesContext());
        if (CheckContextState.isResponseNotComplete(context) && !response.isCommitted() && (session.isPresent())
                && (null != session.get().getAttribute(MESSAGES_IDENTIFIER))) {
            @SuppressWarnings("unchecked")
            var messages = (List<FacesMessage>) session.get().getAttribute(MESSAGES_IDENTIFIER);
            log.trace("restore messages: {}", messages);
            messages.forEach(message -> event.getFacesContext().addMessage(null,
                    // because the old message may already be rendered (and the rendered flag was
                    // set) we need to reset it
                    new FacesMessage(message.getSeverity(), message.getSummary(), message.getDetail())));
            session.get().removeAttribute(MESSAGES_IDENTIFIER);
        }
    }

    @Override
    public PhaseId getPhaseId() {
        return RENDER_RESPONSE;
    }
}
