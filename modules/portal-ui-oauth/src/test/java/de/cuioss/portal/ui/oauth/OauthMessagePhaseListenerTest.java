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

import static de.cuioss.portal.ui.oauth.WrappedOauthFacadeImpl.MESSAGES_IDENTIFIER;
import static de.cuioss.tools.collect.CollectionLiterals.immutableList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.PhaseEvent;
import jakarta.faces.event.PhaseId;
import jakarta.servlet.http.HttpSession;

import org.apache.myfaces.test.mock.lifecycle.MockLifecycle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.cuioss.portal.ui.test.junit5.EnablePortalUiEnvironment;
import de.cuioss.test.jsf.util.JsfEnvironmentConsumer;
import de.cuioss.test.jsf.util.JsfEnvironmentHolder;
import de.cuioss.test.juli.junit5.EnableTestLogger;
import de.cuioss.test.valueobjects.junit5.contracts.ShouldBeNotNull;
import lombok.Getter;
import lombok.Setter;

@EnablePortalUiEnvironment
@EnableTestLogger(trace = OauthMessagePhaseListener.class)
class OauthMessagePhaseListenerTest implements ShouldBeNotNull<OauthMessagePhaseListener>, JsfEnvironmentConsumer {

    @Setter
    @Getter
    private JsfEnvironmentHolder environmentHolder;

    @Getter
    private OauthMessagePhaseListener underTest;

    @BeforeEach
    void beforeEach() {
        underTest = new OauthMessagePhaseListener();
    }

    @Test
    void shouldRestoreMessagesIfAllConditionsAreMet() {
        addSomeMessages();
        fireBeforPhase();
        assertNull(getSession().getAttribute(MESSAGES_IDENTIFIER), "Should have been removed");
    }

    @Test
    void shouldNotRestoreOnCompletedResponse() {
        addSomeMessages();
        environmentHolder.getFacesContext().responseComplete();

        fireBeforPhase();
        assertNotNull(getSession().getAttribute(MESSAGES_IDENTIFIER), "Should have been removed");
    }

    private void fireBeforPhase() {
        underTest.beforePhase(
                new PhaseEvent(FacesContext.getCurrentInstance(), PhaseId.RENDER_RESPONSE, new MockLifecycle()));
    }

    @Test
    void shouldAttachToRenderResponse() {
        assertEquals(PhaseId.RENDER_RESPONSE, underTest.getPhaseId());
    }

    private void addSomeMessages() {

        getSession().setAttribute(MESSAGES_IDENTIFIER, immutableList(new FacesMessage("message")));
    }

    private HttpSession getSession() {
        return (HttpSession) environmentHolder.getExternalContext().getSession(false);
    }
}
