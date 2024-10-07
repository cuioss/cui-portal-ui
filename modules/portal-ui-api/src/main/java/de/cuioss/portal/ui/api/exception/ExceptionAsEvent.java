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

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;

/**
 * Simplified version of deltaspike's ExceptionToCatchEvent
 */
@EqualsAndHashCode
@ToString
public class ExceptionAsEvent implements Serializable {

    @Serial
    private static final long serialVersionUID = -1422214652443353900L;

    @Getter
    private HandleOutcome outcome;

    @Getter
    private final Throwable exception;

    public ExceptionAsEvent(Throwable exception) {
        this.exception = exception;
        outcome = HandleOutcome.NO_OP;
    }

    /**
     * Sets the outcome of the handler
     *
     * @param outcome must not be null
     * @return the object itself
     */
    public ExceptionAsEvent handled(HandleOutcome outcome) {
        this.outcome = outcome;
        return this;
    }

    /**
     * @return boolean indicating whether the contained exception was already
     *         handled
     */
    public boolean isHandled() {
        return HandleOutcome.NO_OP != outcome;
    }
}
