package de.cuioss.portal.ui.components;

import de.cuioss.jsf.bootstrap.accordion.AccordionComponent;
import de.cuioss.portal.ui.components.layout.SidebarComponent;
import lombok.experimental.UtilityClass;

/**
 * Simple Container for identifying portal-components family
 *
 * @author Oliver Wolff
 */
@UtilityClass
public final class PortalFamily {

    /** Defines the portal components family. */
    public static final String PORTAL_FAMILY = "de.icw.cui.portal.family";

    /** The component for {@link SidebarComponent} */
    public static final String SIDEBAR_COMPONENT = "de.icw.cui.portal.sidebar";

    /** Default Renderer for {@link AccordionComponent} */
    public static final String SIDEBAR_RENDERER = "de.icw.cui.portal.sidebar_renderer";

}
