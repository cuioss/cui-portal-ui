package de.cuioss.portal.ui.runtime.application.templating;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.inject.Inject;

import org.jboss.weld.junit5.auto.EnableAutoWeld;
import org.junit.jupiter.api.Test;

import de.cuioss.portal.ui.api.templating.PortalMultiViewMapper;
import de.cuioss.test.valueobjects.junit5.contracts.ShouldHandleObjectContracts;
import lombok.Getter;

@EnableAutoWeld
class PortalViewMapperSimpleTest implements ShouldHandleObjectContracts<PortalViewMapper> {

    public static final String PORTAL = "/META-INF/faces/";

    public static final String INDEX = "pages/index.xhtml";

    public static final String NOT_THERE = "not.there.xhtml";

    @Inject
    @PortalMultiViewMapper
    @Getter
    private PortalViewMapper underTest;

    @Test
    void shouldInitCorrectly() {
        assertTrue(underTest.resolveViewPath(INDEX).getPath().endsWith(PORTAL + INDEX));
    }

    @Test
    void shouldFailOnNoneExistingResource() {
        assertThrows(NullPointerException.class, () -> {
            assertTrue(underTest.resolveViewPath(NOT_THERE).getPath().endsWith(PORTAL + NOT_THERE));
        });
    }

}
