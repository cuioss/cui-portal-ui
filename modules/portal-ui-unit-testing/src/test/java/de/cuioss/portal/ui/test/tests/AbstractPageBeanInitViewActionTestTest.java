package de.cuioss.portal.ui.test.tests;

import javax.inject.Inject;

import de.cuioss.portal.ui.test.junit5.EnablePortalUiEnvironment;
import de.cuioss.portal.ui.test.tests.support.ViewBean;

@EnablePortalUiEnvironment
class AbstractPageBeanInitViewActionTestTest extends AbstractPageBeanTest<ViewBean> {

    @Inject
    private ViewBean underTest;

    @Override
    public ViewBean getUnderTest() {
        underTest.initBean();
        return underTest;
    }
}
