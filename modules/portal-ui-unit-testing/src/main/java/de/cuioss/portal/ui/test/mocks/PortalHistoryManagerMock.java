package de.cuioss.portal.ui.test.mocks;

import javax.enterprise.context.ApplicationScoped;

import de.cuioss.jsf.test.mock.application.HistoryManagerMock;
import de.cuioss.portal.ui.api.history.PortalHistoryManager;

/**
 * @author Oliver Wolff
 */
@PortalHistoryManager
@ApplicationScoped
public class PortalHistoryManagerMock extends HistoryManagerMock {

    private static final long serialVersionUID = -3934691506290620858L;

}
