package de.cuioss.portal.ui.runtime.application.lazyloading;

import java.util.concurrent.Future;

import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * DTO to store and retrieve {@link Future}s in {@link ThreadManager}.
 */
@Data
@RequiredArgsConstructor
class FutureHandle {

    /**
     * The {@link Future} to store.
     */
    private final Future<?> future;

    /**
     * A context.
     */
    private final Object context;

    /**
     * Timestamp of creation in milliseconds.
     */
    private final long timestamp;
}
