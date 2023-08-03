package de.icw.cui.portal.ui.errorpages;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.Serializable;

import javax.inject.Inject;

import org.jboss.weld.junit5.auto.AddBeanClasses;
import org.junit.jupiter.api.Test;

import de.cuioss.portal.core.storage.MapStorage;
import de.cuioss.portal.core.storage.PortalSessionStorage;
import de.cuioss.portal.core.test.mocks.core.PortalSessionStorageMock;
import de.cuioss.portal.ui.api.exception.DefaultErrorMessage;
import de.cuioss.portal.ui.test.junit5.EnablePortalUiEnvironment;
import de.cuioss.portal.ui.test.tests.AbstractPageBeanTest;
import lombok.Getter;

@EnablePortalUiEnvironment
@AddBeanClasses({ PortalSessionStorageMock.class })
class Http401PageBeanTest extends AbstractPageBeanTest<Http401PageBean> {

    @Getter
    @Inject
    private Http401PageBean underTest;

    @Inject
    @PortalSessionStorage
    private MapStorage<Serializable, Serializable> mapStorage;

    @Test
    void shouldProvideCorrectCode() {
        assertEquals(401, underTest.getErrorCode());
    }

    @Test
    void shouldHandleNoMessage() {
        underTest.initView();
        assertFalse(underTest.isMessageAvailable());
    }

    @Test
    void shouldHandleMessage() {
        DefaultErrorMessage.addErrorMessageToSessionStorage(new DefaultErrorMessage("1", "1", "1", "1"), mapStorage);
        underTest.initView();
        assertTrue(underTest.isMessageAvailable());
    }
}
