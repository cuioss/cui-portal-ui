package de.cuioss.portal.ui.runtime.application.history;

import javax.annotation.PostConstruct;
import javax.annotation.Priority;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;

import org.apache.deltaspike.core.api.scope.WindowScoped;

import de.cuioss.jsf.api.application.history.HistoryManager;
import de.cuioss.jsf.api.application.history.impl.HistoryManagerImpl;
import de.cuioss.portal.configuration.common.PortalPriorities;
import de.cuioss.portal.ui.api.history.PortalHistoryManager;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Delegate;

/**
 * Bean keeping track of the view history. For configuration see package-info
 * The implementation utilizes a stack to store the history. The actual work is
 * done by {@link HistoryManagerImpl}.
 *
 * @author Oliver Wolff
 */
@PortalHistoryManager
@WindowScoped
@Priority(PortalPriorities.PORTAL_CORE_LEVEL)
@Named(HistoryManagerImpl.BEAN_NAME)
@EqualsAndHashCode(exclude = "delegate")
@ToString(exclude = "delegate")
public class HistoryManagerBean implements HistoryManager {

    private static final long serialVersionUID = 8385906931652178190L;

    @Inject
    private DefaultHistoryConfiguration historyConfiguration;

    @Delegate(types = HistoryManager.class)
    private HistoryManager delegate;

    @Inject
    private Provider<FacesContext> facesContextProvider;

    /**
     * Initializes the bean. See class documentation for expected result.
     */
    @PostConstruct
    public void initBean() {
        delegate = new HistoryManagerImpl(historyConfiguration);
    }

}
