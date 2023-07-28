package de.icw.cui.portal.ui.errorpages;

import static org.junit.jupiter.api.Assertions.assertEquals;

import javax.inject.Inject;

import org.junit.jupiter.api.Test;

import de.cuioss.portal.ui.test.junit5.EnablePortalUiEnvironment;
import de.cuioss.portal.ui.test.tests.AbstractPageBeanTest;
import lombok.Getter;

@EnablePortalUiEnvironment
class Http403PageBeanTest extends AbstractPageBeanTest<Http403PageBean> {

    @Getter
    @Inject
    private Http403PageBean underTest;

    @Test
    void shouldProvideCorrectCode() {
        assertEquals(403, underTest.getErrorCode());
    }
}
