package de.cuioss.portal.ui.runtime.page;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import de.cuioss.jsf.api.application.navigation.ViewIdentifier;
import de.cuioss.jsf.api.servlet.ServletAdapterUtil;
import de.cuioss.portal.authentication.AuthenticatedUserInfo;
import de.cuioss.portal.ui.test.configuration.PortalNavigationConfiguration;
import de.cuioss.uimodel.nameprovider.IDisplayNameProvider;
import de.cuioss.uimodel.nameprovider.LabeledKey;
import de.cuioss.uimodel.result.ResultDetail;
import de.cuioss.uimodel.result.ResultObject;
import de.cuioss.uimodel.result.ResultState;
import lombok.Getter;
import lombok.Setter;

@SuppressWarnings({ "javadoc" })
@RequestScoped
public class TestLoginPage extends AbstractLoginPageBean {

    private static final long serialVersionUID = -3147741576450873465L;

    @Getter
    @Setter
    private AuthenticatedUserInfo userInfo;

    @Inject
    private FacesContext facesContext;

    @Getter
    @Setter
    private boolean simulateLoginError;

    @Override
    protected ResultObject<AuthenticatedUserInfo> doLogin(final HttpServletRequest servletRequest) {
        if (simulateLoginError) {
            return ResultObject.<AuthenticatedUserInfo>builder().state(ResultState.WARNING)
                    .resultDetail(new ResultDetail(new LabeledKey("OOPS ... something went wrong ... ")))
                    .result(userInfo).build();
        }
        return ResultObject.<AuthenticatedUserInfo>builder().state(ResultState.VALID).result(userInfo).build();
    }

    @Override
    protected void handleLoginFailed(final IDisplayNameProvider<?> errorMessage) {
    }

    String doLogin() {
        return loginAction(
                () -> ViewIdentifier.getFromViewDesciptor(PortalNavigationConfiguration.DESCRIPTOR_HOME, null),
                ServletAdapterUtil.getRequest(facesContext), facesContext);
    }

}
