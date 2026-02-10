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
package de.cuioss.portal.ui.api.listener.view;

import jakarta.faces.event.PhaseId;
import jakarta.faces.event.PhaseListener;
import jakarta.inject.Qualifier;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Marker identifying concrete instances of {@link ViewListener}, that are to be
 * fired at {@link PhaseId#RESTORE_VIEW}
 *
 * @author Oliver Wolff
 */
@Qualifier
@Retention(RUNTIME)
@Target({TYPE, METHOD, FIELD, PARAMETER})
public @interface PortalRestoreViewListener {

    /**
     * @return {@link PhaseExecution} indicating whether a concrete Portal-listener
     * is to be fired
     * {@link PhaseListener#beforePhase(jakarta.faces.event.PhaseEvent)} of
     * {@link PhaseListener#afterPhase(jakarta.faces.event.PhaseEvent)}
     */
    PhaseExecution value();
}
