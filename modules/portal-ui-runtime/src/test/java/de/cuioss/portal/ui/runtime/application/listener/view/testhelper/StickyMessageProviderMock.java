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
package de.cuioss.portal.ui.runtime.application.listener.view.testhelper;

import de.cuioss.jsf.api.components.css.ContextState;
import de.cuioss.portal.ui.api.message.StickyMessage;
import de.cuioss.portal.ui.api.message.StickyMessageProvider;
import de.cuioss.uimodel.nameprovider.DisplayName;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.Dependent;
import lombok.Data;

import java.io.Serial;
import java.util.HashSet;
import java.util.Set;

/**
 * Mock impl for StickyMessageProvider
 *
 * @author i000576
 */
@Data
@Dependent
public class StickyMessageProviderMock implements StickyMessageProvider {

    @Serial
    private static final long serialVersionUID = -2569248357894675575L;

    private Set<StickyMessage> messages = new HashSet<>(0);

    @PostConstruct
    public void initBean() {
        simulateNewMessage();
    }

    @Override
    public Set<StickyMessage> retrieveMessages() {
        return messages;
    }

    public void simulateNewMessage() {
        final var testMessage = StickyMessage.builder().message(new DisplayName("Test")).dismissable(true)
                .state(ContextState.INFO).build();
        messages.add(testMessage);
    }
}
