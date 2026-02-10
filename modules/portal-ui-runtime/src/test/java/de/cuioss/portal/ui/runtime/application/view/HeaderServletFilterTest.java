/*
 * Copyright © 2025 CUI-OpenSource-Software (info@cuioss.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
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
import de.cuioss.test.valueobjects.junit5.contracts.ShouldBeNotNull;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import org.apache.myfaces.test.mock.MockHttpServletRequest;
import org.apache.myfaces.test.mock.MockHttpServletResponse;
import org.easymock.EasyMock;
import org.jboss.weld.junit5.auto.AddBeanClasses;
import org.jboss.weld.junit5.auto.EnableAutoWeld;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SuppressWarnings("UastIncorrectHttpHeaderInspection")
@EnableAutoWeld
@EnablePortalConfiguration
@AddBeanClasses({ViewMatcherProducer.class, HttpHeaderFilterImpl.class})
class HeaderServletFilterTest implements ShouldBeNotNull<HeaderServletFilter> {

    @Getter
    private HeaderServletFilter underTest;

    @Inject
    private PortalTestConfiguration configuration;

    @Produces
    private MockHttpServletRequest servletRequest;

    @BeforeEach
    void beforeEach() {
        servletRequest = new MockHttpServletRequest();
        servletRequest.setPathInfo(HttpHeaderFilterImplTest.TEST_URI);
        underTest = new HeaderServletFilter();
    }

    @Test
    @SuppressWarnings("UastIncorrectHttpHeaderInspection")
    void shouldDoFilter() throws Exception {
        HttpHeaderFilterImplTest.configureFilter(configuration);

        HttpServletResponse response = new MockHttpServletResponse();
        getUnderTest().doFilter(servletRequest, response, EasyMock.createNiceMock(FilterChain.class));
        assertEquals("vwx:z", response.getHeader("def"));
        assertNull(response.getHeader("mno"));
    }
}