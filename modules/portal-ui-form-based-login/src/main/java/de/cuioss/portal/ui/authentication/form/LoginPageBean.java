package de.cuioss.portal.ui.authentication.form;

import static de.cuioss.portal.configuration.PortalConfigurationKeys.PAGES_LOGIN_DEFAULT_USERSTORE;
import static de.cuioss.portal.ui.api.GlobalComponentIds.LOGIN_PAGE_USER_NAME;
import static de.cuioss.portal.ui.api.GlobalComponentIds.LOGIN_PAGE_USER_PASSWORD;
import static de.cuioss.tools.string.MoreStrings.isEmpty;

import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.annotation.Priority;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;
import javax.servlet.http.HttpServletRequest;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.omnifaces.cdi.Param;

import de.cuioss.jsf.api.application.message.DisplayNameProviderMessageProducer;
import de.cuioss.jsf.api.application.message.MessageProducer;
import de.cuioss.jsf.api.servlet.ServletAdapterUtil;
import de.cuioss.portal.authentication.AuthenticatedUserInfo;
import de.cuioss.portal.authentication.PortalUser;
import de.cuioss.portal.authentication.facade.FormBasedAuthenticationFacade;
import de.cuioss.portal.authentication.facade.PortalAuthenticationFacade;
import de.cuioss.portal.authentication.model.UserStore;
import de.cuioss.portal.configuration.common.PortalPriorities;
import de.cuioss.portal.ui.api.message.PortalMessageProducer;
import de.cuioss.portal.ui.api.ui.pages.HomePage;
import de.cuioss.portal.ui.api.ui.pages.LoginPage;
import de.cuioss.portal.ui.api.ui.pages.LoginPageClientStorage;
import de.cuioss.portal.ui.api.ui.pages.LoginPageHistoryManagerProvider;
import de.cuioss.portal.ui.api.ui.pages.PortalCorePagesLogin;
import de.cuioss.portal.ui.runtime.page.AbstractLoginPageBean;
import de.cuioss.portal.ui.runtime.page.PortalPagesConfiguration;
import de.cuioss.uimodel.application.LoginCredentials;
import de.cuioss.uimodel.nameprovider.IDisplayNameProvider;
import de.cuioss.uimodel.result.ResultObject;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Page bean for the login. it is {@link RequestScoped} in order to be used with
 * non-transient views.
 */
@PortalCorePagesLogin
@Named(LoginPage.BEAN_NAME)
@Priority(PortalPriorities.PORTAL_CORE_LEVEL)
@RequestScoped
@EqualsAndHashCode(of = { "loginCredentials", "availableUserStores" }, doNotUseGetters = true, callSuper = false)
@ToString(of = { "loginCredentials", "availableUserStores" }, doNotUseGetters = true)
public class LoginPageBean extends AbstractLoginPageBean implements LoginPage {

    private static final long serialVersionUID = 8709729494565906154L;

    @Getter
    private LoginCredentials loginCredentials;

    @SuppressWarnings("cdi-ambiguous-dependency")
    @Inject
    @Param
    private String username;

    @SuppressWarnings("cdi-ambiguous-dependency")
    @Inject
    @Param
    private String userstore;

    @Getter
    private List<UserStore> availableUserStores;

    @SuppressWarnings("cdi-ambiguous-dependency")
    @Inject
    @PortalAuthenticationFacade
    private FormBasedAuthenticationFacade authenticationFacade;

    @Inject
    private LoginPageHistoryManagerProvider historyManagerProvider;

    @Inject
    private LoginPageClientStorage localStorage;

    @Inject
    @PortalMessageProducer
    private MessageProducer messageProducer;

    @Inject
    @PortalUser
    private AuthenticatedUserInfo userInfo;

    @Inject
    private Provider<FacesContext> facesContextProvider;

    @Inject
    private PortalPagesConfiguration pagesConfiguration;

    @Inject
    @ConfigProperty(name = PAGES_LOGIN_DEFAULT_USERSTORE)
    private Optional<String> defaultConfiguredUserStore;

    @Getter
    @Setter
    private String errorTextKey;

    /**
     * Initializes the availableUserStores and {@link LoginCredentials}
     */
    @PostConstruct
    public void init() {

        availableUserStores = authenticationFacade.getAvailableUserStores();

        loginCredentials = historyManagerProvider.extractFromDeepLinkingUrlParameter(userstore, username)
                .orElseGet(localStorage.extractFromClientStorage());

        checkUserStoreAndAdjustIfNeeded();
    }

    private void checkUserStoreAndAdjustIfNeeded() {
        if (!availableUserStores.isEmpty() && isEnteredUserStoreEmptyOrInvalid()) {

            final var userStoreFromCookie = localStorage.extractFromClientStorage().get().getUserStore();

            if (isUserStoreValueValid(userStoreFromCookie)) {
                loginCredentials.setUserStore(userStoreFromCookie);
            } else {
                loginCredentials.setUserStore(defaultUserStore());
            }
        }
    }

    private boolean isEnteredUserStoreEmptyOrInvalid() {
        return !isUserStoreValueValid(loginCredentials.getUserStore());
    }

    @SuppressWarnings("squid:S3655") // owolff: Optional is checked properly
    private String defaultUserStore() {
        if (defaultConfiguredUserStore.isPresent() && isUserStoreValueValid(defaultConfiguredUserStore.get())) {
            return defaultConfiguredUserStore.get();
        }
        return availableUserStores.get(0).getName();
    }

    private boolean isUserStoreValueValid(final String userStoreFromCookie) {
        return availableUserStores.stream().anyMatch(userStore -> userStore.getName().equals(userStoreFromCookie));
    }

    @Override
    public String initViewAction() {
        String outcome = null;
        if (userInfo.isAuthenticated()) {
            final var strategy = pagesConfiguration.getLoginPageStrategy();
            switch (strategy) {
            case GOTO_HOME:
                outcome = HomePage.OUTCOME;
                break;
            case LOGOUT:
                authenticationFacade.logout(ServletAdapterUtil.getRequest(facesContextProvider.get()));
                break;
            default:
                throw new IllegalStateException("Unknown LoginPageStrategy found: " + strategy);
            }
        }
        if (!isEmpty(errorTextKey)) {
            messageProducer.setGlobalErrorMessage(errorTextKey);
        }
        return outcome;
    }

    @Override
    public String getFocusComponent() {
        if (isEmpty(loginCredentials.getUsername())) {
            return LOGIN_PAGE_USER_NAME.getId();
        }
        return LOGIN_PAGE_USER_PASSWORD.getId();
    }

    @Override
    public String login() {

        localStorage.updateLocalStored(loginCredentials);

        return loginAction(() -> historyManagerProvider.getCurrentViewExcludeUserStoreAndUserName(),
                ServletAdapterUtil.getRequest(facesContextProvider.get()), facesContextProvider.get());
    }

    @Override
    public boolean isShouldDisplayUserStoreDropdown() {
        return availableUserStores.size() > 1;
    }

    @Override
    protected ResultObject<AuthenticatedUserInfo> doLogin(final HttpServletRequest currentServletRequest) {
        return authenticationFacade.login(currentServletRequest, loginCredentials);
    }

    @Override
    protected void handleLoginFailed(final IDisplayNameProvider<?> reason) {
        // FIXME: MWL reactivate
        // if (!clientInformation.canIUse(FeatureName.COOKIES)) {
        // messageProducer.setGlobalErrorMessage("message.error.cookies.disable");
        // }
        if (null != reason) {
            new DisplayNameProviderMessageProducer(messageProducer).showAsGlobalMessage(reason,
                    FacesMessage.SEVERITY_ERROR);
        }
    }

}
