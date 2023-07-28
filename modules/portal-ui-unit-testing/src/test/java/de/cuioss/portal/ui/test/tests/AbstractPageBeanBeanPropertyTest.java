package de.cuioss.portal.ui.test.tests;

import javax.inject.Inject;

import de.cuioss.portal.ui.test.junit5.EnablePortalUiEnvironment;
import de.cuioss.portal.ui.test.tests.support.ViewBean;
import de.cuioss.test.valueobjects.api.contracts.VerifyBeanProperty;
import lombok.Getter;

@EnablePortalUiEnvironment
@VerifyBeanProperty
class AbstractPageBeanBeanPropertyTest extends AbstractPageBeanTest<ViewBean> {

    @Inject
    @Getter
    private ViewBean underTest;

}
