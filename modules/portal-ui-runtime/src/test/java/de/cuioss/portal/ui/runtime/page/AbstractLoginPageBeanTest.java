package de.cuioss.portal.ui.runtime.page;

import static org.junit.jupiter.api.Assertions.assertNull;

import javax.inject.Inject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.cuioss.portal.core.test.mocks.authentication.PortalAuthenticationFacadeMock;
import de.cuioss.portal.ui.test.junit5.EnablePortalUiEnvironment;
import de.cuioss.portal.ui.test.tests.AbstractPageBeanTest;
import lombok.Getter;

@EnablePortalUiEnvironment
class AbstractLoginPageBeanTest extends AbstractPageBeanTest<TestLoginPage> {

    @Inject
    @Getter
    private TestLoginPage underTest;

    private PortalAuthenticationFacadeMock facadeMock = new PortalAuthenticationFacadeMock();

    @BeforeEach
    void before() {
        facadeMock.init();
        underTest.setUserInfo(facadeMock.retrieveCurrentAuthenticationContext(null));
    }

    @Test
    void shouldHandlePositiveLogin() {
        assertNull(underTest.doLogin());
    }

    @Test
    void shouldReactOnErrors() {
        underTest.setSimulateLoginError(true);
        assertNull(underTest.doLogin());
    }

}
