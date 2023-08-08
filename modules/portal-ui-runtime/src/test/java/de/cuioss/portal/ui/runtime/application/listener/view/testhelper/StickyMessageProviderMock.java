package de.cuioss.portal.ui.runtime.application.listener.view.testhelper;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;

import de.cuioss.jsf.api.components.css.ContextState;
import de.cuioss.portal.ui.api.message.StickyMessage;
import de.cuioss.portal.ui.api.message.StickyMessageProvider;
import de.cuioss.uimodel.nameprovider.DisplayName;
import lombok.Data;

/**
 * Mock impl for StickyMessageProvider
 *
 * @author i000576
 */
@Data
@Dependent
public class StickyMessageProviderMock implements StickyMessageProvider {

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
