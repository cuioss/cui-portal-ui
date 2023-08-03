package de.cuioss.portal.ui.api.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import de.cuioss.portal.core.storage.impl.MapStorageImpl;
import de.cuioss.portal.ui.api.exception.DefaultErrorMessage;
import de.cuioss.test.valueobjects.ValueObjectTest;
import de.cuioss.test.valueobjects.api.contracts.VerifyConstructor;

@VerifyConstructor(of = { "errorCode", "errorTicket", "errorMessage", "pageId" })
class DefaultErrorMessageTest extends ValueObjectTest<DefaultErrorMessage> {

    @Test
    void shouldAddToMap() {
        var message = anyValueObject();
        var storage = new MapStorageImpl<>();
        DefaultErrorMessage.addErrorMessageToSessionStorage(message, storage);
        assertEquals(message, storage.get(DefaultErrorMessage.LOOKUP_KEY));
    }
}
