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
package de.cuioss.portal.ui.runtime.application.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;

import org.jboss.weld.junit5.auto.AddBeanClasses;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import de.cuioss.portal.common.bundle.ResourceBundleWrapper;
import de.cuioss.portal.common.bundle.ResourceBundleRegistry;
import de.cuioss.portal.configuration.PortalConfigurationKeys;
import de.cuioss.portal.configuration.PortalConfigurationSource;
import de.cuioss.portal.core.servlet.CuiContextPath;
import de.cuioss.portal.core.storage.PortalClientStorage;
import de.cuioss.portal.core.test.mocks.configuration.PortalTestConfiguration;
import de.cuioss.portal.ui.runtime.application.bundle.InstallationResourceBundleWrapperMock;
import de.cuioss.portal.ui.test.junit5.EnablePortalUiEnvironment;
import de.cuioss.portal.ui.test.mocks.PortalLocaleProducerMock;
import de.cuioss.test.valueobjects.junit5.contracts.ShouldHandleObjectContracts;
import lombok.Getter;

@EnablePortalUiEnvironment
@AddBeanClasses({ PortalClientStorageImpl.class, PortalLocaleProducerMock.class, ResourceBundleWrapper.class,
        InstallationResourceBundleWrapperMock.class, ResourceBundleRegistry.class })
class PortalClientStorageImplTest implements ShouldHandleObjectContracts<PortalClientStorageImpl> {

    private static final String testKey = "testKey";

    @Inject
    @PortalClientStorage
    @Getter
    private PortalClientStorageImpl underTest;

    @Inject
    @PortalConfigurationSource
    private PortalTestConfiguration configuration;

    @Produces
    @CuiContextPath
    String contextPath = "context";

    @Test
    @Disabled("""
        org.apache.myfaces.test.mock.MockExternalContext20#addResponseCookie doesn't know the cookie key \
         (myfaces-test23-1.0.8-sources.jar!/org/apache/myfaces/test/mock/MockExternalContext20.java:346)\
        'httpOnly' added by 'omnifaces-3.1.jar!/org/omnifaces/util/FacesLocal.class:758' and throws \
        an Exception therefore. \
        See: https://issues.apache.org/jira/browse/MYFACESTEST-72\
        deltaspike-jsf-module-api 1.8.1 uses myfaces-test20, see: \
        deltaspike-jsf-module-api-1.8.1-sources.jar!/META-INF/maven/org.apache.deltaspike.modules/deltaspike-jsf-module-api/pom.xml:55\
        So: omnifaces-3.1 doesn't go with myfaces-2.3""")
    void testRoundTrip() {
        configuration.fireEvent(PortalConfigurationKeys.CLIENT_STORAGE_COOKIE_MAXAGE, "666");

        underTest.remove(testKey);

        assertFalse(underTest.containsKey(testKey));
        assertEquals("0", underTest.get(testKey, "0"));
        underTest.put(testKey, "1");

        assertTrue(underTest.containsKey(testKey));
        assertEquals("1", underTest.get(testKey));
        assertEquals("1", underTest.get(testKey, "0"));
        assertEquals("1", underTest.remove(testKey));

        assertFalse(underTest.containsKey(testKey));
        assertEquals("0", underTest.get(testKey, "0"));
    }
}
