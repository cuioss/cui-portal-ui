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
package de.cuioss.portal.ui.runtime.application.history;

import javax.faces.application.NavigationCase;
import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;

import de.cuioss.jsf.api.application.navigation.BaseDelegatingNavigationHandler;
import de.cuioss.portal.core.cdi.PortalBeanManager;
import de.cuioss.portal.ui.api.history.HistoryManager;

/**
 * @author Oliver Wolff
 */
public class HistoryNavigationHandler extends BaseDelegatingNavigationHandler {

    private static final String BACK = "back";

    /**
     * @param wrapped
     */
    public HistoryNavigationHandler(final NavigationHandler wrapped) {
        super(wrapped);
    }

    @Override
    public void handleNavigation(final FacesContext context, final String from, final String outcome) {
        if (BACK.equals(outcome)) {
            PortalBeanManager.resolveBeanOrThrowIllegalStateException(HistoryManager.class, null).popPrevious()
                    .redirect(context);
        } else {
            super.handleNavigation(context, from, outcome);
        }
    }

    @Override
    public NavigationCase getNavigationCase(final FacesContext context, final String fromAction, final String outcome) {
        if (BACK.equals(outcome)) {
            return PortalBeanManager.resolveBeanOrThrowIllegalStateException(HistoryManager.class, null).peekPrevious()
                    .toBackNavigationCase();
        }
        return super.getNavigationCase(context, fromAction, outcome);
    }
}
