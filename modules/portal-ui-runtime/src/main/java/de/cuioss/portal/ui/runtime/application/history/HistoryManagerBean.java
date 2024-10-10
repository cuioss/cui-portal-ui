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

import de.cuioss.portal.common.priority.PortalPriorities;
import de.cuioss.portal.ui.api.history.HistoryManager;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.inject.Provider;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Delegate;

import java.io.Serial;

/**
 * Bean keeping track of the view history. For configuration see package-info
 * The implementation utilizes a stack to store the history. The actual work is
 * done by {@link HistoryManagerImpl}.
 * <p>
 * FIXME: Use WindowScope again if on jsf 4. Update: ClientWindow failed with quarkus
 *
 * @author Oliver Wolff
 */
@SessionScoped
@Priority(PortalPriorities.PORTAL_CORE_LEVEL)
@Named(HistoryManagerImpl.BEAN_NAME)
@EqualsAndHashCode(exclude = "delegate")
@ToString(exclude = "delegate")
public class HistoryManagerBean implements HistoryManager {

    @Serial
    private static final long serialVersionUID = 8385906931652178190L;

    @Inject
    DefaultHistoryConfiguration historyConfiguration;
    @Inject
    Provider<FacesContext> facesContextProvider;
    @Delegate(types = HistoryManager.class)
    private HistoryManager delegate;

    /**
     * Initializes the bean. See class documentation for expected result.
     */
    @PostConstruct
    public void initBean() {
        delegate = new HistoryManagerImpl(historyConfiguration);
    }

}
