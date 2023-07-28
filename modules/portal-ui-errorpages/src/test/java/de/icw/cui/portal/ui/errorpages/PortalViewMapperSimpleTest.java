package de.icw.cui.portal.ui.errorpages;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.inject.Inject;

import org.jboss.weld.junit5.auto.EnableAutoWeld;
import org.junit.jupiter.api.Test;

import de.cuioss.portal.ui.api.templating.PortalMultiViewMapper;
import de.cuioss.portal.ui.runtime.application.templating.PortalViewMapper;
import de.cuioss.test.valueobjects.junit5.contracts.ShouldHandleObjectContracts;
import lombok.Getter;

@EnableAutoWeld
class PortalViewMapperSimpleTest implements ShouldHandleObjectContracts<PortalViewMapper> {

    public static final String PORTAL = "/META-INF/faces/";

    public static final String UNAUTHORIZED = "guest/401.xhtml";
    public static final String FORBIDDEN = "guest/403.xhtml";
    public static final String NOT_FOUND = "guest/404.xhtml";
    public static final String ERROR = "guest/error.xhtml";

    public static final String NOT_THERE = "not.there.xhtml";

    @Inject
    @PortalMultiViewMapper
    @Getter
    private PortalViewMapper underTest;

    @Test
    void shouldInitCorrectly() {
        assertTrue(underTest.resolveViewPath(ERROR).getPath().endsWith(PORTAL + ERROR));
        assertTrue(underTest.resolveViewPath(UNAUTHORIZED).getPath().endsWith(PORTAL + UNAUTHORIZED));
        assertTrue(underTest.resolveViewPath(FORBIDDEN).getPath().endsWith(PORTAL + FORBIDDEN));
        assertTrue(underTest.resolveViewPath(NOT_FOUND).getPath().endsWith(PORTAL + NOT_FOUND));
    }

    @Test
    void shouldFailOnNoneExistingResource() {
        assertThrows(NullPointerException.class, () -> {
            assertTrue(underTest.resolveViewPath(NOT_THERE).getPath().endsWith(PORTAL + NOT_THERE));
        });
    }
}
