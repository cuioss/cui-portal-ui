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

import jakarta.faces.context.FacesContext;
import jakarta.faces.event.PhaseListener;

/**
 * Determines whether a concrete Portal-listener is to be fired
 * {@link PhaseListener#beforePhase(jakarta.faces.event.PhaseEvent)} of
 * {@link PhaseListener#afterPhase(jakarta.faces.event.PhaseEvent)}
 *
 * @author Oliver Wolff
 */
public enum PhaseExecution {
    /**
     * to be fired {@link PhaseListener#beforePhase(jakarta.faces.event.PhaseEvent)}
     */
    BEFORE_PHASE,

    /**
     * to be fired {@link PhaseListener#afterPhase(jakarta.faces.event.PhaseEvent)}.
     * It will be called for {@link FacesContext#isPostback()} calls as well.
     */
    AFTER_PHASE,

    /**
     * To be fired {@link PhaseListener#afterPhase(jakarta.faces.event.PhaseEvent)}
     * for non {@link FacesContext#isPostback()} calls.
     */
    AFTER_PHASE_EXCLUDE_POSTBACK

}
