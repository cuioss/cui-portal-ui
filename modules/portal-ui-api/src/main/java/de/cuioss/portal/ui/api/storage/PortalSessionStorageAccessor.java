package de.cuioss.portal.ui.api.storage;

import static de.cuioss.portal.ui.api.PortalCoreBeanNames.SESSION_STORAGE_BEAN_NAME;

import de.cuioss.jsf.api.common.accessor.ManagedBeanAccessor;
import de.cuioss.portal.core.storage.SessionStorage;

/**
 * Helper class for accessing instances of {@link SessionStorage} within objects
 * that are not under control of the MangedBeanFacility, e.g. Converter,
 * validators, components.
 *
 * @author Oliver Wolff
 * @deprecated use CDI directly
 */
@Deprecated
public class PortalSessionStorageAccessor extends ManagedBeanAccessor<SessionStorage> {

    private static final long serialVersionUID = 6941913636918722401L;

    /**
     * Constructor
     */
    public PortalSessionStorageAccessor() {
        super(SESSION_STORAGE_BEAN_NAME, SessionStorage.class, true);
    }
}
