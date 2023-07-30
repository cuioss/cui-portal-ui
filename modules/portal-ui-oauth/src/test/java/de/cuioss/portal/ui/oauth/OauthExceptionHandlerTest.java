package de.cuioss.portal.ui.oauth;

import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.inject.Inject;

import org.jboss.weld.junit5.auto.AddBeanClasses;
import org.junit.jupiter.api.Test;

import de.cuioss.portal.authentication.oauth.OauthAuthenticationException;
import de.cuioss.portal.core.test.mocks.core.PortalSessionStorageMock;
import de.cuioss.portal.ui.oauth.support.MockExceptionEvent;
import de.cuioss.portal.ui.test.junit5.EnablePortalUiEnvironment;
import de.cuioss.test.valueobjects.junit5.contracts.ShouldBeSerializable;
import lombok.Getter;

@EnablePortalUiEnvironment
@AddBeanClasses({ PortalSessionStorageMock.class })
class OauthExceptionHandlerTest implements ShouldBeSerializable<OauthExceptionHandler> {

    @Inject
    @Getter
    private OauthExceptionHandler underTest;

    @Test
    void shouldHandle() {
        final var event = new MockExceptionEvent<>(new OauthAuthenticationException("oautherror"));
        underTest.handleOauthAuthenticationException(event);
        assertTrue(event.isHandled());
    }
}
