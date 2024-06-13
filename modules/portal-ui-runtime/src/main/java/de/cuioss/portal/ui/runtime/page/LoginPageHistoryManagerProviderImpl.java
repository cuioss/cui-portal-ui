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
package de.cuioss.portal.ui.runtime.page;

import static de.cuioss.portal.ui.api.pages.LoginPage.KEY_USERNAME;
import static de.cuioss.portal.ui.api.pages.LoginPage.KEY_USERSTORE;

import java.io.Serial;
import java.io.Serializable;
import java.util.Optional;
import java.util.function.Predicate;

import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import jakarta.inject.Provider;

import de.cuioss.jsf.api.application.navigation.ViewIdentifier;
import de.cuioss.portal.ui.api.history.HistoryManager;
import de.cuioss.portal.ui.api.pages.LoginPageHistoryManagerProvider;
import de.cuioss.tools.net.UrlParameter;
import de.cuioss.tools.string.MoreStrings;
import de.cuioss.uimodel.application.LoginCredentials;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * {@linkplain LoginPageHistoryManagerProviderImpl} is a decorator for
 * {@linkplain HistoryManager} which provide shortcut methods for login page
 * specific interaction
 *
 * @author i000576
 */
@Dependent
@ToString
@EqualsAndHashCode
public class LoginPageHistoryManagerProviderImpl implements LoginPageHistoryManagerProvider, Serializable {

    @Serial
    private static final long serialVersionUID = 6346481935899028211L;

    @Inject
    private Provider<HistoryManager> historyManagerProvider;

    /**
     * Extract userStore and userName from deep link URL
     *
     * @param userStore used as default value
     * @param username  used as default value
     * @return option for {@linkplain LoginCredentials}, if userStore or userName is
     *         missing opntion is empty
     */
    @Override
    public Optional<LoginCredentials> extractFromDeepLinkingUrlParameter(final String userStore,
            final String username) {

        var extractedUserStore = MoreStrings.emptyToNull(userStore);
        var extractedUserName = MoreStrings.emptyToNull(username);

        final var viewOutcome = historyManagerProvider.get().getCurrentView();

        for (final UrlParameter currentParam : viewOutcome.getUrlParameters()) {
            if (isUserName(currentParam)) {
                extractedUserName = currentParam.getValue();
            }
            if (isUserStore(currentParam)) {
                extractedUserStore = currentParam.getValue();
            }
        }

        if (null != extractedUserStore || null != extractedUserName) {
            return Optional
                    .of(LoginCredentials.builder().userStore(extractedUserStore).username(extractedUserName).build());
        }

        return Optional.empty();
    }

    /**
     * Retrieve current view with cleaned up URL parameter
     *
     * @return {@linkplain ViewIdentifier}
     */
    @Override
    public ViewIdentifier getCurrentViewExcludeUserStoreAndUserName() {
        var currentView = getWrapped().getCurrentView();
        final var newParams = currentView.getUrlParameters().stream().filter(excludeUserStoreAndUserName()).toList();
        return new ViewIdentifier(currentView.getViewId(), currentView.getOutcome(), newParams);
    }

    @Override
    public HistoryManager getWrapped() {
        return historyManagerProvider.get();
    }

    private static boolean isUserStore(final UrlParameter currentParam) {
        return KEY_USERSTORE.equals(currentParam.getName());
    }

    private static boolean isUserName(final UrlParameter currentParam) {
        return KEY_USERNAME.equals(currentParam.getName());
    }

    private static Predicate<? super UrlParameter> excludeUserStoreAndUserName() {
        return (var param) -> !isUserStore(param) && !isUserName(param);
    }
}
