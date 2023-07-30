package de.cuioss.portal.ui.oauth;

import static org.junit.jupiter.api.Assertions.assertEquals;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Provider;

import org.jboss.weld.junit5.auto.ActivateScopes;
import org.jboss.weld.junit5.auto.AddBeanClasses;
import org.jboss.weld.junit5.auto.EnableAutoWeld;
import org.junit.jupiter.api.Test;

import de.cuioss.portal.authentication.oauth.LoginPagePath;
import de.cuioss.portal.ui.test.configuration.PortalNavigationConfiguration;

@EnableAutoWeld
@AddBeanClasses({ LoginPagePathProducer.class })
@ActivateScopes({ RequestScoped.class })
class LoginPagePathProducerWOFacesContextTest {

    @Inject
    @LoginPagePath
    private Provider<String> loginPathProvider;

    @Test
    void shouldUseDefaults() {
        assertEquals(PortalNavigationConfiguration.VIEW_LOGIN_VIEW_ID, loginPathProvider.get());
    }

}
