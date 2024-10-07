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

import de.cuioss.portal.authentication.oauth.LoginPagePath;
import de.cuioss.portal.ui.test.configuration.PortalNavigationConfiguration;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Provider;
import org.jboss.weld.junit5.auto.ActivateScopes;
import org.jboss.weld.junit5.auto.AddBeanClasses;
import org.jboss.weld.junit5.auto.EnableAutoWeld;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
