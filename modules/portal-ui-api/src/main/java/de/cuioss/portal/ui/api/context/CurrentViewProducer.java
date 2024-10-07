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
package de.cuioss.portal.ui.api.context;

import de.cuioss.jsf.api.application.navigation.NavigationUtils;
import de.cuioss.jsf.api.common.view.ViewDescriptor;
import de.cuioss.jsf.api.common.view.ViewDescriptorImpl;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;

/**
 * Produces a requestScoped {@link ViewDescriptor} typed with
 * {@link CuiCurrentView}.
 *
 * @author Oliver Wolff
 */
@ApplicationScoped
public class CurrentViewProducer {

    /**
     * @return the derived {@link ViewDescriptor}
     */
    @Produces
    @Named
    @CuiCurrentView
    @RequestScoped
    ViewDescriptor getCurrentView() {
        final var context = FacesContext.getCurrentInstance();
        if (null == context) {
            return ViewDescriptorImpl.builder().build();
        }
        return NavigationUtils.getCurrentView(context);
    }
}
