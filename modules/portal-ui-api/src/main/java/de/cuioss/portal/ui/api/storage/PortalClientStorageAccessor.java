package de.cuioss.portal.ui.api.storage;

import static de.cuioss.portal.ui.api.PortalCoreBeanNames.CLIENT_STORAGE_BEAN_NAME;

import de.cuioss.jsf.api.common.accessor.ManagedBeanAccessor;
import de.cuioss.portal.core.storage.ClientStorage;

/**
 * Helper class for accessing instances of {@link ClientStorage} within objects
 * that are not under control of the MangedBeanFacility, e.g. Converter,
 * validators, components.
 *
 * @author Matthias Walliczek
 * @deprecated use CDI directly
 */
@Deprecated
public class PortalClientStorageAccessor extends ManagedBeanAccessor<ClientStorage> {

    private static final long serialVersionUID = 6941913636918722401L;

    /**
     * Constructor
     */
    public PortalClientStorageAccessor() {
        super(CLIENT_STORAGE_BEAN_NAME, ClientStorage.class, true);
    }

}
