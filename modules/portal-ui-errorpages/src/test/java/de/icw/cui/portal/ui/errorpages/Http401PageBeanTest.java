package de.icw.cui.portal.ui.errorpages;

import static org.junit.jupiter.api.Assertions.assertEquals;

import javax.inject.Inject;

import org.jboss.weld.junit5.auto.AddBeanClasses;
import org.junit.jupiter.api.Test;

import de.cuioss.portal.core.test.mocks.core.PortalSessionStorageMock;
import de.cuioss.portal.ui.test.junit5.EnablePortalUiEnvironment;
import de.cuioss.portal.ui.test.tests.AbstractPageBeanTest;
import lombok.Getter;

@EnablePortalUiEnvironment
@AddBeanClasses({ PortalSessionStorageMock.class })
class Http401PageBeanTest extends AbstractPageBeanTest<Http401PageBean> {

    @Getter
    @Inject
    private Http401PageBean underTest;

    @Test
    void shouldProvideCorrectCode() {
        assertEquals(401, underTest.getErrorCode());
    }
}
