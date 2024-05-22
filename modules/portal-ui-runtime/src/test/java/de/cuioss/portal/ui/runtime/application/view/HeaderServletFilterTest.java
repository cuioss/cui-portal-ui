package de.cuioss.portal.ui.runtime.application.view;

import de.cuioss.portal.configuration.PortalConfigurationSource;
import de.cuioss.portal.core.test.junit5.EnablePortalConfiguration;
import de.cuioss.portal.core.test.mocks.configuration.PortalTestConfiguration;
import de.cuioss.portal.ui.runtime.application.view.matcher.ViewMatcherProducer;
import de.cuioss.test.valueobjects.junit5.contracts.ShouldBeNotNull;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import org.apache.myfaces.test.mock.MockHttpServletRequest;
import org.apache.myfaces.test.mock.MockHttpServletResponse;
import org.easymock.EasyMock;
import org.jboss.weld.junit5.auto.AddBeanClasses;
import org.jboss.weld.junit5.auto.EnableAutoWeld;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@EnableAutoWeld
@EnablePortalConfiguration
@AddBeanClasses({ViewMatcherProducer.class, HttpHeaderFilterImpl.class})
class HeaderServletFilterTest implements ShouldBeNotNull<HeaderServletFilter> {

    @Getter
    private HeaderServletFilter underTest;

    @Inject
    @PortalConfigurationSource
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
    void shouldDoFilter() throws ServletException, IOException {
        HttpHeaderFilterImplTest.configureFilter(configuration);

        HttpServletResponse response = new MockHttpServletResponse();
        getUnderTest().doFilter(servletRequest, response, EasyMock.createNiceMock(FilterChain.class));
        assertEquals("vwx:z", response.getHeader("def"));
        assertNull(response.getHeader("mno"));
    }
}