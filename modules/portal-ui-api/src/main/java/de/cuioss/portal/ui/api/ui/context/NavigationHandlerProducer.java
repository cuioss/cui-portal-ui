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

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;
import javax.inject.Named;

/**
 * Produces a applicationScoped {@link NavigationHandler} instance
 *
 * @author Oliver Wolff
 */
@ApplicationScoped
public class NavigationHandlerProducer {

    /**
     * @return the derived {@link NavigationHandler}
     */
    @Produces
    @CuiNavigationHandler
    @ApplicationScoped
    @Named
    NavigationHandler getNavigationHandler() {
        return FacesContext.getCurrentInstance().getApplication().getNavigationHandler();
    }
}
