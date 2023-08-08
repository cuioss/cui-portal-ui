package de.cuioss.portal.ui.runtime.application.exception;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import javax.faces.FacesException;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.context.FacesContext;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;

import lombok.Getter;
import lombok.Setter;

public class MockExceptionHandler extends ExceptionHandlerWrapper {

    @Getter
    @Setter
    private List<ExceptionQueuedEvent> unhandledExceptionQueuedEvents;

    private boolean handled;

    public MockExceptionHandler(ExceptionHandler delegate) {
        super(delegate);
        unhandledExceptionQueuedEvents = new ArrayList<>();
        handled = false;
    }

    public void addUnhandledException(Throwable throwable) {
        unhandledExceptionQueuedEvents.add(new ExceptionQueuedEvent(
                new ExceptionQueuedEventContext(FacesContext.getCurrentInstance(), throwable)));
    }

    @Override
    public void handle() throws FacesException {
        handled = true;
    }

    public void assertHandleCalled() {
        assertTrue(handled);
    }
}
