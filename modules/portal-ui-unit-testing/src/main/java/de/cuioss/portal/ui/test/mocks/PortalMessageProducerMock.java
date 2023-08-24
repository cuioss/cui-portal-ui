/*
 * Copyright 2023 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * https://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
