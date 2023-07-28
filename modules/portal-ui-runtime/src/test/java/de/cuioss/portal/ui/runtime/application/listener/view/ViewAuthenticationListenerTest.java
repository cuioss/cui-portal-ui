package de.cuioss.portal.ui.runtime.application.listener.view;

import static de.cuioss.portal.ui.test.configuration.PortalNavigationConfiguration.DESCRIPTOR_HOME;
import static de.cuioss.portal.ui.test.configuration.PortalNavigationConfiguration.DESCRIPTOR_LOGIN;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.apache.deltaspike.core.api.exception.control.event.ExceptionToCatchEvent;
import org.jboss.weld.junit5.auto.AddBeanClasses;
import org.junit.jupiter.api.Test;

import de.cuioss.portal.core.test.mocks.authentication.PortalTestUserProducer;
import de.cuioss.portal.ui.api.authentication.UserNotAuthenticatedException;
import de.cuioss.portal.ui.api.listener.view.PhaseExecution;
import de.cuioss.portal.ui.api.listener.view.PortalRestoreViewListener;
import de.cuioss.portal.ui.runtime.application.configuration.ViewConfiguration;
import de.cuioss.portal.ui.runtime.application.view.matcher.ViewMatcherProducer;
import de.cuioss.portal.ui.test.junit5.EnablePortalUiEnvironment;
import de.cuioss.test.valueobjects.junit5.contracts.ShouldHandleObjectContracts;
import lombok.Getter;

@EnablePortalUiEnvironment
@AddBeanClasses({ PortalTestUserProducer.class, ViewConfiguration.class, ViewMatcherProducer.class })
class ViewAuthenticationListenerTest implements ShouldHandleObjectContracts<ViewAuthenticationListener> {

    @Inject
    private PortalTestUserProducer portalUserProducerMock;

    @Inject
    @PortalRestoreViewListener(PhaseExecution.AFTER_PHASE)
    @Getter
    private ViewAuthenticationListener underTest;

    private UserNotAuthenticatedException authenticatedException;

    @Test
    void shouldPassOnGuestView() {
        underTest.handleView(DESCRIPTOR_LOGIN);
        assertNull(authenticatedException);
    }

    @Test
    void shouldPassOnAuthenticatedUser() {
        assertNull(authenticatedException);
        underTest.handleView(DESCRIPTOR_HOME);
        assertNull(authenticatedException);
    }

    @Test
    void shouldFailOnNotAuthenticatedUser() {
        portalUserProducerMock.authenticated(false);
        assertNull(authenticatedException);
        underTest.handleView(DESCRIPTOR_HOME);
        assertNotNull(authenticatedException);
    }

    void actOnExceptionToCatchEvent(@Observes final ExceptionToCatchEvent catchEvent) {
        final var exception = catchEvent.getException();
        authenticatedException = exception instanceof UserNotAuthenticatedException
                ? (UserNotAuthenticatedException) exception
                : null;
    }

}
