package de.cuioss.portal.ui.test.mocks;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import de.cuioss.portal.ui.api.ui.lazyloading.LazyLoadingRequest;
import de.cuioss.portal.ui.api.ui.lazyloading.LazyLoadingViewController;
import lombok.Getter;

/**
 * @author Oliver Wolff
 *
 */
@ApplicationScoped
public class PortalLazyLoadingViewControllerMock implements LazyLoadingViewController {

    @Getter
    private final List<LazyLoadingRequest<?>> started = new ArrayList<>();

    @Override
    public void startRequest(LazyLoadingRequest<?> request) {
        started.add(request);
    }

}
