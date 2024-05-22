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
package de.cuioss.portal.ui.api.listener.view;

import java.io.Serializable;

import jakarta.faces.event.PhaseId;

import de.cuioss.jsf.api.common.view.ViewDescriptor;

/**
 * Instances of this Listener will be called from the portal as listener for
 * {@link PhaseId#RESTORE_VIEW}. whether before or after is defined by
 * {@link PhaseExecution}. It will pass the current viewId.
 *
 * @author Oliver Wolff
 */
public interface ViewListener extends Serializable {

    /**
     * Command pattern like handler for interacting on a given view. This may be
     * security checks, or sanity checks, e.g. The handler method must explicitly
     * throw an exception or fire an event in order to act.
     *
     * @param viewDescriptor identifying the requested view. Must not be null nor
     *                       empty.
     */
    void handleView(ViewDescriptor viewDescriptor);

    boolean isEnabled();
}
