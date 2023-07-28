package de.cuioss.portal.ui.runtime.application.view;

import static de.cuioss.portal.configuration.PortalConfigurationKeys.HTTP_HEADER_BASE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import javax.enterprise.event.Event;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import org.apache.myfaces.test.mock.MockHttpServletRequest;
import org.apache.myfaces.test.mock.MockHttpServletResponse;
import org.jboss.weld.junit5.auto.AddBeanClasses;
import org.jboss.weld.junit5.auto.EnableAutoWeld;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.cuioss.portal.configuration.PortalConfigurationSource;
import de.cuioss.portal.configuration.initializer.PortalInitializer;
import de.cuioss.portal.core.listener.literal.ServletInitialized;
import de.cuioss.portal.core.test.junit5.EnablePortalConfiguration;
import de.cuioss.portal.core.test.mocks.configuration.PortalTestConfiguration;
import de.cuioss.portal.ui.runtime.application.view.matcher.ViewMatcherProducer;
import de.cuioss.test.valueobjects.junit5.contracts.ShouldHandleObjectContracts;
import lombok.Getter;

@EnableAutoWeld
@EnablePortalConfiguration
@AddBeanClasses({ ViewMatcherProducer.class })
class HttpHeaderFilterImplTest implements ShouldHandleObjectContracts<HttpHeaderFilterImpl> {

    private static final String TEST_URI = "testURI";

    @Getter
    @Inject
    @PortalInitializer
    private HttpHeaderFilterImpl underTest;

    @Inject
    @ServletInitialized
    private Event<HttpServletResponse> servletResponseEvent;

    @Inject
    @PortalConfigurationSource
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
        configuration.put(HTTP_HEADER_BASE + "abc.enabled", "true");
        configuration.put(HTTP_HEADER_BASE + "abc.content", "def: ghi");

        configuration.put(HTTP_HEADER_BASE + "jkl.enabled", "false");
        configuration.put(HTTP_HEADER_BASE + "jkl.content", "mno: pqr");

        configuration.put(HTTP_HEADER_BASE + "stu.enabled", "true");
        configuration.put(HTTP_HEADER_BASE + "stu.content", "def: vwx:z");
        configuration.put(HTTP_HEADER_BASE + "stu.views", "testURI");

        configuration.fireEvent();

        HttpServletResponse reponse = new MockHttpServletResponse();
        servletResponseEvent.fire(reponse);
        assertEquals("vwx:z", reponse.getHeader("def"));
        assertNull(reponse.getHeader("mno"));
    }

}
