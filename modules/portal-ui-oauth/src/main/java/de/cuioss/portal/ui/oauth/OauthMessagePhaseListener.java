/*
 * Copyright 2023 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * https://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.cuioss.portal.ui.oauth;

import de.cuioss.jsf.api.common.util.CheckContextState;
import de.cuioss.jsf.api.servlet.ServletAdapterUtil;
import de.cuioss.tools.logging.CuiLogger;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.event.PhaseEvent;
import jakarta.faces.event.PhaseId;
import jakarta.faces.event.PhaseListener;
import jakarta.servlet.http.HttpServletRequest;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serial;
import java.util.List;

import static de.cuioss.jsf.api.application.navigation.NavigationUtils.getCurrentView;
import static de.cuioss.jsf.api.servlet.ServletAdapterUtil.getResponse;
import static de.cuioss.portal.ui.oauth.WrappedOauthFacadeImpl.MESSAGES_IDENTIFIER;
import static jakarta.faces.event.PhaseId.RENDER_RESPONSE;

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

    @Serial
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
        if (CheckContextState.isResponseNotComplete(context) && !response.isCommitted() && session.isPresent()
                && null != session.get().getAttribute(MESSAGES_IDENTIFIER)) {
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
