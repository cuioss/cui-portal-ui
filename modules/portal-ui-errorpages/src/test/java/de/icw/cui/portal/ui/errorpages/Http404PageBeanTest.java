package de.icw.cui.portal.ui.errorpages;

import static de.icw.cui.portal.ui.errorpages.AbstractHttpErrorPage.JAVAX_SERVLET_ERROR_REQUEST_URI;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.inject.Inject;

import org.junit.jupiter.api.Test;

import de.cuioss.portal.configuration.PortalConfigurationKeys;
import de.cuioss.portal.ui.test.junit5.EnablePortalUiEnvironment;
import de.cuioss.portal.ui.test.tests.AbstractPageBeanTest;
import lombok.Getter;

@EnablePortalUiEnvironment
class Http404PageBeanTest extends AbstractPageBeanTest<Http404PageBean> {

    private static final String FACES_VIEW_JSF = "/faces/view.jsf";
    private static final String NON_FACES_VIEW_JSF = "/some/view.html";

    @Inject
    @Getter
    private Http404PageBean underTest;

    @Test
    void shouldProvideCorrectCode() {
        assertEquals(404, underTest.getErrorCode());
    }

    @Test
    void shouldDetectFacesView() {
        getRequestConfigDecorator().setViewId(FACES_VIEW_JSF).setRequestAttribute(JAVAX_SERVLET_ERROR_REQUEST_URI,
                FACES_VIEW_JSF);
        underTest.initView();
        assertTrue(underTest.isJsfView());
        assertTrue(underTest.isRequestUriAvailable());
        assertTrue(underTest.isShouldRedirect());
    }

    @Test
    void shouldDetectNonFacesView() {
        getRequestConfigDecorator().setViewId(NON_FACES_VIEW_JSF).setRequestAttribute(JAVAX_SERVLET_ERROR_REQUEST_URI,
                NON_FACES_VIEW_JSF);
        underTest.initView();
        assertFalse(underTest.isJsfView());
        assertTrue(underTest.isRequestUriAvailable());
        assertFalse(underTest.isShouldRedirect());
    }

    @Test
    void shouldDetectEmptyView() {
        getRequestConfigDecorator().setViewId("").setRequestAttribute(JAVAX_SERVLET_ERROR_REQUEST_URI, "");
        underTest.initView();
        assertFalse(underTest.isJsfView());
        assertFalse(underTest.isRequestUriAvailable());
        assertFalse(underTest.isShouldRedirect());
    }

    @Test
    void shouldHandleNotSetView() {
        getRequestConfigDecorator().setViewId(null);
        underTest.initView();
        assertFalse(underTest.isJsfView());
        assertFalse(underTest.isRequestUriAvailable());
        assertFalse(underTest.isShouldRedirect());
    }

    /**
     * Paranoia mode. Should not happen, but error-handling must always be defensive
     */
    @Test
    void shouldHandleMissingServletRequest() {
        getRequestConfigDecorator().setViewId(null);
        getExternalContext().setRequest(null);
        underTest.initView();
        assertFalse(underTest.isJsfView());
        assertFalse(underTest.isRequestUriAvailable());
        assertFalse(underTest.isShouldRedirect());
    }

    @Test
    void shouldNotRedirectIfNotConfigured() {
        configuration.fireEvent(PortalConfigurationKeys.PAGES_ERROR_404_REDIRECT, "false");

        getRequestConfigDecorator().setViewId(FACES_VIEW_JSF).setRequestAttribute(JAVAX_SERVLET_ERROR_REQUEST_URI,
                FACES_VIEW_JSF);
        underTest.initView();
        assertTrue(underTest.isJsfView());
        assertTrue(underTest.isRequestUriAvailable());
        assertFalse(underTest.isShouldRedirect());
    }
}
