package de.cuioss.portal.ui.runtime.application.lazyloading;

import java.io.Serializable;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import de.cuioss.portal.configuration.initializer.PortalInitializer;
import de.cuioss.portal.ui.api.ui.lazyloading.LazyLoadingRequest;
import de.cuioss.portal.ui.api.ui.lazyloading.LazyLoadingViewController;
import de.cuioss.tools.logging.CuiLogger;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Implementation of {@link LazyLoadingViewController} using the
 * {@link ThreadManager} to start backend requests.
 */
@Dependent
@EqualsAndHashCode(exclude = { "threadManager" })
@ToString(exclude = { "threadManager" })
public class LazyLoadingViewControllerImpl implements LazyLoadingViewController, Serializable {

    private static final long serialVersionUID = 4698361355745984370L;

    private static final CuiLogger log = new CuiLogger(LazyLoadingViewControllerImpl.class);

    @Inject
    @PortalInitializer
    private ThreadManager threadManager;

    @Override
    public void startRequest(LazyLoadingRequest<?> request) {
        log.trace("startRequest {}", request);
        threadManager.store(request.getRequestId(), request::backendRequest, request);
    }
}
