package de.cuioss.portal.ui.oauth;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import javax.inject.Inject;

import org.junit.jupiter.api.Test;

import de.cuioss.portal.ui.test.junit5.EnablePortalUiEnvironment;
import de.cuioss.test.valueobjects.junit5.contracts.ShouldBeNotNull;
import lombok.Getter;

@EnablePortalUiEnvironment
class RedirectorImplTest implements ShouldBeNotNull<RedirectorImpl> {

    @Inject
    @Getter
    private RedirectorImpl underTest;

    @Test
    void shouldRedirect() {
        assertDoesNotThrow(() -> underTest.sendRedirect("https://a.de"));
    }

}
