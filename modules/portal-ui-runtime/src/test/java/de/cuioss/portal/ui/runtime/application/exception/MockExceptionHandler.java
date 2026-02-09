/*
 * Copyright © 2025 CUI-OpenSource-Software (info@cuioss.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.cuioss.portal.ui.runtime.application.exception;

import jakarta.faces.FacesException;
import jakarta.faces.context.ExceptionHandler;
import jakarta.faces.context.ExceptionHandlerWrapper;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ExceptionQueuedEvent;
import jakarta.faces.event.ExceptionQueuedEventContext;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

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
