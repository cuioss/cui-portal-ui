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
package de.cuioss.portal.ui.api.menu;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Provider;

import de.cuioss.jsf.api.common.view.ViewDescriptor;
import de.cuioss.jsf.api.components.model.menu.NavigationMenuItemSingle;
import de.cuioss.portal.ui.api.ui.context.CuiCurrentView;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Implementation of {@link NavigationMenuItemSingle} using cdi portal and it's
 * configuration.
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = false, doNotUseGetters = true)
public class PortalNavigationMenuItemSingleImpl extends PortalNavigationMenuItemImplBase
        implements NavigationMenuItemSingle {

    private static final long serialVersionUID = -4639141255087105993L;

    @Inject
    @CuiCurrentView
    Provider<ViewDescriptor> currentViewProvider;

    @Getter
    @Setter
    private String outcome;

    @Getter
    @Setter
    private String target;

    @Getter
    @Setter
    private String onClickAction;

    @Getter
    private final Map<String, String> outcomeParameter = new HashMap<>();

    /**
     * @return true if the current view id equals the configured outcome or is
     *         contained in {@link #getActiveForAdditionalViewId()}.
     */
    @Override
    public boolean isActive() {
        return currentViewProvider.get().getViewId().contains(getOutcome())
                || (!getActiveForAdditionalViewId().isEmpty()
                        && getActiveForAdditionalViewId().contains(currentViewProvider.get().getViewId()));
    }

}
