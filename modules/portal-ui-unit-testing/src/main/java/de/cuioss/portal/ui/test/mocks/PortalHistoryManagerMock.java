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
package de.cuioss.portal.ui.test.mocks;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import de.cuioss.jsf.api.application.navigation.ViewIdentifier;
import de.cuioss.jsf.api.common.view.ViewDescriptor;
import de.cuioss.portal.ui.api.history.HistoryManager;
import de.cuioss.tools.collect.CollectionLiterals;
import de.cuioss.tools.net.ParameterFilter;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Oliver Wolff
 */
@ApplicationScoped
public class PortalHistoryManagerMock implements HistoryManager {

    private static final long serialVersionUID = -3934691506290620858L;

    /** The home navigation view */
    public static final String VIEW_HOME = "/portal/home.jsf";

    /**
     * {@link ViewIdentifier}, representing home navigation.
     */
    public static final ViewIdentifier IDENTIFIER_HOME = new ViewIdentifier(VIEW_HOME, "home", new ArrayList<>());

    @Getter
    @Setter
    private boolean pageReload = false;

    /** The storage for the history. */
    private List<ViewIdentifier> history;

    @Getter
    @Setter
    private ViewIdentifier currentView;

    @Getter
    @Setter
    private ParameterFilter parameterFilter;

    /**
     * Initializes the bean. See class documentation for expected result.
     */
    public PortalHistoryManagerMock() {
        history = new ArrayList<>();
        currentView = IDENTIFIER_HOME;
        history.add(IDENTIFIER_HOME);
        parameterFilter = new ParameterFilter(CollectionLiterals.immutableList("sessionid", "jfwid"), true);
    }

    @Override
    public Iterator<ViewIdentifier> iterator() {
        return history.iterator();
    }

    @Override
    public void addCurrentUriToHistory(final ViewDescriptor viewIdentifier) {
        final var currentViewIdentifier = ViewIdentifier.getFromViewDesciptor(viewIdentifier, parameterFilter);
        var oldCurrentView = getCurrentView();
        // Ensure that reloading the page will not duplicate history entries
        if (null != oldCurrentView && !oldCurrentView.equals(currentViewIdentifier)) {
            history.add(oldCurrentView);
        }
        currentView = currentViewIdentifier;
    }

    @Override
    public ViewIdentifier popPrevious() {
        if (history.size() > 1) {
            return history.remove(history.size() - 1);
        }
        return IDENTIFIER_HOME;
    }

    @Override
    public ViewIdentifier peekPrevious() {
        if (history.size() > 1) {
            return history.get(history.size() - 1);
        }
        return IDENTIFIER_HOME;
    }

}
