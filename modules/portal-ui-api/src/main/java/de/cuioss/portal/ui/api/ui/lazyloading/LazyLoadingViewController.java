package de.cuioss.portal.ui.api.ui.lazyloading;

/**
 * A controller to start {@link LazyLoadingRequest} to be run asynchronously.
 */
public interface LazyLoadingViewController {

    /**
     * Starts the {@link LazyLoadingRequest#backendRequest()} method of the given
     * {@link LazyLoadingRequest}.
     *
     * @param request The request to start.
     */
    void startRequest(LazyLoadingRequest<?> request);
}
