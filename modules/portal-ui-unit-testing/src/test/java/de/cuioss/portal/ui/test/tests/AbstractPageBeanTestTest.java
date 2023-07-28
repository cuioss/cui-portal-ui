package de.cuioss.portal.ui.test.tests;

import static org.junit.jupiter.api.Assertions.assertFalse;

import javax.inject.Inject;

import org.junit.jupiter.api.Test;

import de.cuioss.portal.ui.test.junit5.EnablePortalUiEnvironment;
import de.cuioss.portal.ui.test.tests.support.ViewBean;
import lombok.Getter;

@EnablePortalUiEnvironment
class AbstractPageBeanTestTest extends AbstractPageBeanTest<ViewBean> {

    @Inject
    @Getter
    private ViewBean underTest;

    @Test
    void shouldNotCallInitViewAction() {
        assertFalse(getUnderTest().isInitCalled());
    }

}
