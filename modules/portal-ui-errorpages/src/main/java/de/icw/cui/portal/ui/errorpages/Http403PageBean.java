package de.icw.cui.portal.ui.errorpages;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.servlet.http.HttpServletResponse;

/**
 * Page Bean for the error-code 403 (Forbidden)
 *
 * @author Oliver Wolff
 *
 */
@RequestScoped
@Named
public class Http403PageBean extends AbstractHttpErrorPage {

    private static final long serialVersionUID = -2216275532091092216L;

    @Override
    protected int getErrorCode() {
        return HttpServletResponse.SC_FORBIDDEN;
    }

}
