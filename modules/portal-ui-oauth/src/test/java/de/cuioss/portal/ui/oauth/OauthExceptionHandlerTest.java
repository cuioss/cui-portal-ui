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

import static org.junit.jupiter.api.Assertions.assertEquals;

import jakarta.inject.Inject;

import org.jboss.weld.junit5.auto.AddBeanClasses;
import org.junit.jupiter.api.Test;

import de.cuioss.jsf.api.application.message.MessageProducerBean;
import de.cuioss.portal.authentication.oauth.OauthAuthenticationException;
import de.cuioss.portal.core.test.mocks.core.PortalSessionStorageMock;
import de.cuioss.portal.ui.api.exception.ExceptionAsEvent;
import de.cuioss.portal.ui.api.exception.HandleOutcome;
import de.cuioss.portal.ui.test.junit5.EnablePortalUiEnvironment;
import de.cuioss.test.valueobjects.junit5.contracts.ShouldBeSerializable;
import lombok.Getter;

@EnablePortalUiEnvironment
@AddBeanClasses({ PortalSessionStorageMock.class, MessageProducerBean.class })
class OauthExceptionHandlerTest implements ShouldBeSerializable<OauthExceptionHandler> {

    @Inject
    @Getter
    private OauthExceptionHandler underTest;

    @Test
    void shouldHandle() {
        final var event = new ExceptionAsEvent(new OauthAuthenticationException("oautherror"));
        underTest.handle(event);
        assertEquals(HandleOutcome.REDIRECT, event.getOutcome());
    }
}
