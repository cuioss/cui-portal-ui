/*
 * Copyright 2023 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * https://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.cuioss.portal.ui.api.exception;

/**
 * A Successor of deltaspikes ExceptionHandler. Not that elegant but works
 * anyway. In essence, it defines a handler for specific / single exception;
 */
public interface PortalExceptionHandler {

    /**
     * @param exceptionEvent the exception that may be handled. If this Handler
     *                       handles a contained {@link Throwable} it will notify
     *                       using the given {@link ExceptionAsEvent} by calling
     *                       {@link ExceptionAsEvent#handled(HandleOutcome)}
     */
    void handle(ExceptionAsEvent exceptionEvent);

}
