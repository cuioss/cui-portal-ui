package de.cuioss.portal.ui.api;

import de.cuioss.portal.ui.api.message.StickyMessage;
import de.cuioss.portal.ui.api.test.support.IDisplayNameProviderTypedGenerator;
import de.cuioss.test.valueobjects.ValueObjectTest;
import de.cuioss.test.valueobjects.api.contracts.VerifyBuilder;
import de.cuioss.test.valueobjects.api.generator.PropertyGenerator;

@SuppressWarnings("javadoc")
@PropertyGenerator(IDisplayNameProviderTypedGenerator.class)
@VerifyBuilder(required = { "state", "message" })
class StickyMessageTest extends ValueObjectTest<StickyMessage> {

}
