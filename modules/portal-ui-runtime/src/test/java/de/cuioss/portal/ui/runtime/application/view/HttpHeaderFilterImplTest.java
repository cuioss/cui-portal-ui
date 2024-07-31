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
package de.cuioss.portal.ui.runtime.application.view;

import de.cuioss.portal.core.test.junit5.EnablePortalConfiguration;
import de.cuioss.portal.core.test.mocks.configuration.PortalTestConfiguration;
import de.cuioss.portal.ui.runtime.application.view.matcher.ViewMatcherProducer;
import de.cuioss.test.valueobjects.junit5.contracts.ShouldHandleObjectContracts;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import org.apache.myfaces.test.mock.MockHttpServletRequest;
import org.apache.myfaces.test.mock.MockHttpServletResponse;
import org.jboss.weld.junit5.auto.AddBeanClasses;
import org.jboss.weld.junit5.auto.EnableAutoWeld;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static de.cuioss.portal.configuration.PortalConfigurationKeys.HTTP_HEADER_BASE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@EnableAutoWeld
@EnablePortalConfiguration
@AddBeanClasses({ViewMatcherProducer.class})
class HttpHeaderFilterImplTest implements ShouldHandleObjectContracts<HttpHeaderFilterImpl> {

    static final String TEST_URI = "testURI";

    @Getter
    @Inject
    private HttpHeaderFilterImpl underTest;

    @Inject
    private PortalTestConfiguration configuration;

    @Produces
    private MockHttpServletRequest servletRequest;

    @BeforeEach
    void beforeEach() {
        servletRequest = new MockHttpServletRequest();
        servletRequest.setPathInfo(TEST_URI);
    }

    @Test
    void testCustomConfig() {
        configureFilter(configuration);

        HttpServletResponse response = new MockHttpServletResponse();
        getUnderTest().onCreate(servletRequest, response);
        assertEquals("vwx:z", response.getHeader("def"));
        assertNull(response.getHeader("mno"));
    }

    static void configureFilter(PortalTestConfiguration configuration) {
        configuration.put(HTTP_HEADER_BASE + "abc.enabled", "true");
        configuration.put(HTTP_HEADER_BASE + "abc.content", "def: ghi");

        configuration.put(HTTP_HEADER_BASE + "jkl.enabled", "false");
        configuration.put(HTTP_HEADER_BASE + "jkl.content", "mno: pqr");

        configuration.put(HTTP_HEADER_BASE + "stu.enabled", "true");
        configuration.put(HTTP_HEADER_BASE + "stu.content", "def: vwx:z");
        configuration.put(HTTP_HEADER_BASE + "stu.views", "testURI");

        configuration.fireEvent();
    }

}
