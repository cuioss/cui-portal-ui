package de.cuioss.portal.ui.authentication.form;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.jboss.weld.junit5.auto.AddBeanClasses;
import org.junit.jupiter.api.Test;

import de.cuioss.portal.authentication.LoginEvent;
import de.cuioss.portal.authentication.PortalLoginEvent;
import de.cuioss.portal.authentication.facade.PortalAuthenticationFacade;
import de.cuioss.portal.core.test.mocks.authentication.PortalAuthenticationFacadeMock;
import de.cuioss.portal.core.test.mocks.authentication.PortalTestUserProducer;
import de.cuioss.portal.ui.api.ui.pages.LoginPage;
import de.cuioss.portal.ui.api.ui.pages.PortalCorePagesLogout;
import de.cuioss.portal.ui.runtime.page.PortalPagesConfiguration;
import de.cuioss.portal.ui.test.junit5.EnablePortalUiEnvironment;
import de.cuioss.portal.ui.test.tests.AbstractPageBeanTest;
import lombok.Getter;

@EnablePortalUiEnvironment
@AddBeanClasses({ PortalPagesConfiguration.class, PortalTestUserProducer.class })
class LogoutPageBeanTest extends AbstractPageBeanTest<LogoutPageBean> {

    @Inject
    @PortalCorePagesLogout
    @Getter
    private LogoutPageBean underTest;

    @Inject
    @PortalAuthenticationFacade
    private PortalAuthenticationFacadeMock authenticationFacadeMock;

    private LoginEvent event;

    @Test
    void shouldLogoutOnViewAction() {
        assertNull(event);
        assertEquals(LoginPage.OUTCOME, underTest.logoutViewAction());
        authenticationFacadeMock.assertAuthenticated(false);
        assertNotNull(event);
        assertEquals(LoginEvent.Action.LOGOUT, event.getAction());
    }

    void onLoginEventListener(@Observes @PortalLoginEvent final LoginEvent givenEvent) {
        event = givenEvent;
    }

}
