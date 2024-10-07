package de.cuioss.portal.ui.runtime.application.view;

import de.cuioss.portal.common.cdi.PortalBeanManager;
import de.cuioss.tools.logging.CuiLogger;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;

import java.io.IOException;

public class HeaderServletFilter implements Filter {

    private static final CuiLogger LOGGER = new CuiLogger(HeaderServletFilter.class);

    @Getter(lazy = true)
    private final HttpHeaderFilterImpl headerFilter = PortalBeanManager.resolveRequiredBean(HttpHeaderFilterImpl.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        LOGGER.trace("doFilter with %s and %s ", request, response);
        getHeaderFilter().onCreate((HttpServletRequest) request, (HttpServletResponse) response);
        chain.doFilter(request, response);
    }
}
