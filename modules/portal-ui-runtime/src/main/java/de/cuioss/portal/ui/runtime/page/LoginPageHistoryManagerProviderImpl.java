package de.cuioss.portal.ui.runtime.page;

import static de.cuioss.portal.ui.api.ui.pages.LoginPage.KEY_USERNAME;
import static de.cuioss.portal.ui.api.ui.pages.LoginPage.KEY_USERSTORE;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Provider;

import de.cuioss.jsf.api.application.history.HistoryManager;
import de.cuioss.jsf.api.application.navigation.ViewIdentifier;
import de.cuioss.portal.ui.api.history.PortalHistoryManager;
import de.cuioss.portal.ui.api.ui.pages.LoginPageHistoryManagerProvider;
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

    private static final long serialVersionUID = 6346481935899028211L;

    @SuppressWarnings("cdi-ambiguous-dependency")
    @Inject
    @PortalHistoryManager
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
        final List<UrlParameter> newParams = currentView.getUrlParameters().stream()
                .filter(excludeUserStoreAndUserName()).collect(Collectors.toList());
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
