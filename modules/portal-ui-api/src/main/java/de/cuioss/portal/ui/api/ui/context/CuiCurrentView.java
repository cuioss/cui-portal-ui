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
package de.cuioss.portal.ui.api.ui.context;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.faces.event.PhaseId;
import jakarta.inject.Qualifier;

import de.cuioss.jsf.api.common.view.ViewDescriptor;

/**
 * Identifier for the current jsf view, the representation is
 * {@link ViewDescriptor}.
 *
 * <em>Caution: </em> The scope is RequestScoped, therefore you must not inject
 * this before {@link PhaseId#RESTORE_VIEW}
 *
 * @author Oliver Wolff
 */
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER })
public @interface CuiCurrentView {

}
