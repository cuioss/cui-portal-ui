package de.icw.cui.portal.ui.errorpages;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import de.cuioss.portal.configuration.PortalConfigurationKeys;
import de.cuioss.tools.base.BooleanOperations;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Page Bean for the error-code 404 (Resource not found)
 *
 * @author Oliver Wolff
 *
 */
@RequestScoped
@Named
@EqualsAndHashCode(callSuper = true)
public class Http404PageBean extends AbstractHttpErrorPage {

    private static final long serialVersionUID = -2216275532091092216L;

    @Inject
    @ConfigProperty(name = PortalConfigurationKeys.PAGES_ERROR_404_REDIRECT)
    @Getter
    private boolean shouldRedirect;

    @Override
    public String initView() {
        super.initView();
        if (BooleanOperations.isAnyTrue(!isJsfView(), !shouldRedirect, !isRequestUriAvailable())) {
            shouldRedirect = false;
        }
        return null;
    }

    @Override
    protected int getErrorCode() {
        return HttpServletResponse.SC_NOT_FOUND;
    }

}
