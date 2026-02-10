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
package de.cuioss.portal.ui.api.message;

import de.cuioss.jsf.api.components.css.ContextState;
import de.cuioss.uimodel.nameprovider.IDisplayNameProvider;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.io.Serial;
import java.io.Serializable;

/**
 * A sticky message consisting of dismissable flag, message string and state.
 *
 * @author Matthias Walliczek
 */
@Builder
@Data
@AllArgsConstructor
public class StickyMessage implements Serializable {

    @Serial
    private static final long serialVersionUID = -3226075374956046365L;

    /**
     * if true, the message could be removed by UI interaction
     */
    private final boolean dismissable;

    /**
     * {@linkplain ContextState} is required
     */
    @NonNull
    private final ContextState state;

    /**
     * Message content as {@linkplain IDisplayNameProvider} is required
     */
    @NonNull
    private final IDisplayNameProvider<?> message;

}
