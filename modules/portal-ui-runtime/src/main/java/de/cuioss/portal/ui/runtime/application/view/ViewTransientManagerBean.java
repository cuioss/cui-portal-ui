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
package de.cuioss.portal.ui.runtime.application.view;

import java.io.Serial;
import java.io.Serializable;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.inject.Provider;

import de.cuioss.jsf.api.common.view.ViewDescriptor;

import jakarta.annotation.PostConstruct;
import de.cuioss.portal.ui.api.context.CuiCurrentView;
import de.cuioss.portal.ui.runtime.application.configuration.ViewConfiguration;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Managed bean bridging the view with instances of
 *
 * @author Oliver Wolff
 */
@Named(ViewTransientManagerBean.BEAN_NAME)
@RequestScoped
@EqualsAndHashCode(of = "transientView", doNotUseGetters = true)
@ToString(of = "transientView", doNotUseGetters = true)
public class ViewTransientManagerBean implements Serializable {

    /**
     * Bean name for looking up instances.
     */
    static final String BEAN_NAME = "viewTransientManager";

    @Serial
    private static final long serialVersionUID = -8225392922526412945L;

    @Getter
    private boolean transientView = false;

    @Inject
    private ViewConfiguration viewConfiguration;

    @Inject
    @CuiCurrentView
    private Provider<ViewDescriptor> currentViewProvider;

    /**
     * Initializes the bean. See class documentation for expected result.
     */
    @PostConstruct
    public void initBean() {
        final var viewDescriptor = currentViewProvider.get();
        if (viewDescriptor.isViewDefined()) {
            transientView = viewConfiguration.getTransientViewMatcher().match(viewDescriptor);
        }
    }
}
