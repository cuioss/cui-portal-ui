package de.cuioss.portal.ui.oauth.support;

import org.apache.deltaspike.core.api.exception.control.event.ExceptionEvent;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Mock variant of {@link ExceptionEvent} that can be used as a recorder for
 * calls.
 *
 * @author Oliver Wolff
 * @param <T> the concrete throwable.
 */
@RequiredArgsConstructor
public class MockExceptionEvent<T extends Throwable> implements ExceptionEvent<T> {

    @Getter
    private final T exception;

    @Getter
    private boolean aborted = false;

    @Getter
    private boolean throwOriginal = false;

    @Getter
    private boolean handled = false;

    @Getter
    private boolean handledAndContinue = false;

    @Getter
    private boolean skipCause = false;

    @Getter
    private boolean unmute = false;

    @Getter
    private Throwable rethrownException;

    @Override
    public void abort() {
        aborted = true;
    }

    @Override
    public void throwOriginal() {
        throwOriginal = true;
    }

    @Override
    public void handled() {
        handled = true;
    }

    @Override
    public void handledAndContinue() {
        this.handled = true;
        this.handledAndContinue = true;
    }

    @Override
    public void skipCause() {
        this.skipCause = true;
    }

    @Override
    public void unmute() {
        this.unmute = true;
    }

    @Override
    public void rethrow(final Throwable t) {
        this.rethrownException = t;
    }

    @Override
    public boolean isMarkedHandled() {
        return handled;
    }

}
