package de.cuioss.portal.ui.api;

import de.cuioss.portal.core.storage.ClientStorage;
import de.cuioss.portal.core.storage.SessionStorage;
import de.cuioss.portal.ui.api.message.StickyMessageProducer;
import de.cuioss.portal.ui.api.templating.MultiTemplatingMapper;
import de.cuioss.portal.ui.api.templating.MultiViewMapper;
import lombok.experimental.UtilityClass;

/**
 * Container for static bean-names. Usually needed for access from views. In
 * other cases CDI should be used directly.
 *
 * @author Oliver Wolff
 *
 */
@UtilityClass
public class PortalCoreBeanNames {

    /**
     * Bean name for looking up instances of {@link ClientStorage}.
     */
    public static final String CLIENT_STORAGE_BEAN_NAME = "portalClientStorage";

    /**
     * Bean name for looking up instances of {@link SessionStorage}.
     */
    public static final String SESSION_STORAGE_BEAN_NAME = "portalSessionStorage";

    /**
     * Bean name for looking up instances of {@link StickyMessageProducer}.
     */
    public static final String STICKY_MESSAGE_BEAN_NAME = "stickyMessageProducer";

    /**
     * Bean name for looking up instances of {@link MultiTemplatingMapper}.
     */
    public static final String MULTI_TEMPLATING_MAPPER_BEAN_NAME = "multiTemplatingMapper";

    /**
     * Bean name for looking up instances of {@link MultiViewMapper}.
     */
    public static final String MULTI_VIEW_MAPPER_BEAN_NAME = "multiViewMapper";
}
