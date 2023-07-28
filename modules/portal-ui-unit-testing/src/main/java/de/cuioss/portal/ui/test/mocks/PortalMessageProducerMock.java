package de.cuioss.portal.ui.test.mocks;

import javax.enterprise.context.ApplicationScoped;

import de.cuioss.jsf.api.application.message.MessageProducer;
import de.cuioss.jsf.test.mock.application.MessageProducerMock;
import de.cuioss.portal.ui.api.message.PortalMessageProducer;

/**
 * Mock version of {@link MessageProducer} simplifying the testing. The message
 * are sorted to {@link #getComponentMessages()} and
 * {@link #getGlobalMessages()}. The keys will not be resolved but written
 * directly to the messages.
 *
 * @author Oliver Wolff
 */
@ApplicationScoped
@PortalMessageProducer
public class PortalMessageProducerMock extends MessageProducerMock {

    private static final long serialVersionUID = -7244733672736029893L;

}
