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

import java.io.Serializable;

import de.cuioss.portal.core.storage.MapStorage;
import de.cuioss.portal.core.storage.SessionStorage;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * Simple Container for communicating and displaying exception information at
 * the ui.
 *
 * @author Oliver Wolff
 */
@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public class DefaultErrorMessage implements Serializable {

    /**
     * The lookup key for identifying error-messages within portal specific
     * {@link SessionStorage}
     */
    public static final String LOOKUP_KEY = "defaultErrorMessage";

    /** Serial version UID. */
    private static final long serialVersionUID = 5486339618618279597L;

    /** The error code. */
    @Getter
    private final String errorCode;

    /** The error code. */
    @Getter
    private final String errorTicket;

    /** The error code. */
    @Getter
    private final String errorMessage;

    /** The page that raised the error. */
    @Getter
    private final String pageId;

    @Getter
    private String stackTrace;

    /**
     * Store a given {@link DefaultErrorMessage} into the given {@link MapStorage}
     *
     * @param errorMessage must not be null
     * @param storage      m,sut not be null
     */
    public static final void addErrorMessageToSessionStorage(final DefaultErrorMessage errorMessage,
            final MapStorage<Serializable, Serializable> storage) {
        storage.put(LOOKUP_KEY, errorMessage);
    }

}
