package de.cuioss.portal.ui.oauth;

import static org.junit.jupiter.api.Assertions.assertEquals;

import javax.inject.Inject;
import javax.inject.Provider;

import org.jboss.weld.junit5.auto.AddBeanClasses;
import org.junit.jupiter.api.Test;

import de.cuioss.portal.authentication.oauth.LoginPagePath;
import de.cuioss.portal.ui.test.configuration.PortalNavigationConfiguration;
import de.cuioss.portal.ui.test.junit5.EnablePortalUiEnvironment;

@EnablePortalUiEnvironment
@AddBeanClasses({ LoginPagePathProducer.class })
class LoginPagePathProducerTest {

    @Inject
    @LoginPagePath
    private Provider<String> loginPathProvider;

    @Test
    void shouldUseDefaults() {
        assertEquals(PortalNavigationConfiguration.VIEW_LOGIN_LOGICAL_VIEW_ID, loginPathProvider.get());
    }

}
