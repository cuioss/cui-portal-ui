package de.icw.cui.portal.ui.errorpages;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.inject.Inject;

import org.junit.jupiter.api.Test;

import de.cuioss.portal.core.storage.PortalSessionStorage;
import de.cuioss.portal.core.test.mocks.core.PortalSessionStorageMock;
import de.cuioss.portal.ui.api.exception.DefaultErrorMessage;
import de.cuioss.portal.ui.api.ui.pages.PortalCorePagesError;
import de.cuioss.portal.ui.test.junit5.EnablePortalUiEnvironment;
import de.cuioss.portal.ui.test.tests.AbstractPageBeanTest;
import lombok.Getter;

@EnablePortalUiEnvironment
class ErrorPageBeanTest extends AbstractPageBeanTest<ErrorPageBean> {

    @Inject
    @PortalSessionStorage
    private PortalSessionStorageMock mapStorage;

    @Inject
    @PortalCorePagesError
    @Getter
    private ErrorPageBean underTest;

    @Test
    void shouldIgnoreNotExisitingMessage() {
        assertFalse(underTest.isMessageAvailable());
    }

    @Test
    void shouldResolveAndRemoveErrorMessage() {
        var errorMessage = new DefaultErrorMessage("errorCode", "errorTicket", "errorMessage",
                "pageId");
        DefaultErrorMessage.addErrorMessageToSessionStorage(errorMessage, mapStorage);

        assertTrue(mapStorage.containsKey(DefaultErrorMessage.LOOKUP_KEY));
        assertTrue(underTest.isMessageAvailable());
        assertEquals(errorMessage, underTest.getMessage());
        assertFalse(mapStorage.containsKey(DefaultErrorMessage.LOOKUP_KEY));
    }

}
