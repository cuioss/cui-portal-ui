package de.cuioss.portal.ui.components.layout;

import javax.faces.component.FacesComponent;
import javax.faces.context.FacesContext;

import de.cuioss.jsf.api.components.base.BaseCuiPanel;
import de.cuioss.jsf.api.components.css.StyleClassBuilder;
import de.cuioss.jsf.api.components.css.StyleClassResolver;
import de.cuioss.jsf.api.components.html.Node;
import de.cuioss.jsf.api.components.partial.HtmlElementProvider;
import de.cuioss.jsf.api.components.util.ComponentUtility;
import de.cuioss.portal.ui.components.PortalCssClasses;
import de.cuioss.portal.ui.components.PortalFamily;
import lombok.experimental.Delegate;

/**
 * Renders a container for the sidebar within portal-context. Actually it
 * renders a div / nav with the style-class {@link PortalCssClasses#SIDEBAR}.
 * The corresponding elements are defined at the root.xhtml template
 *
 * <h3>Sidebar Left Sample</h3>
 *
 * <pre>
 * {@code
    <ui:define name="sidebarLeft">
        <portal:sidebar>
            <h1>Some Content in the sidebar</h1>
        </portal:sidebar>
 </ui:define>}
 * </pre>
 *
 * <h3>Sidebar Right Sample</h3>
 *
 * <pre>
 * {@code
    <ui:define name="sidebarRight">
        <portal:sidebar>
            <h1>Some Content in the right sidebar</h1>
        </portal:sidebar>
 </ui:define>}
 * </pre>
 *
 * <h2>Attributes</h2>
 * <ul>
 * <li>Common attributes like style, styleClass, rendered and id</li>
 * <li>{@link HtmlElementProvider}, defaulting to {@value Node#NAV}</li>
 * </ul>
 * <h2>Styling</h2>
 * <ul>
 * <li>The marker css class is '{@value PortalCssClasses#SIDEBAR}'</li>
 * </ul>
 *
 * @author Oliver Wolff
 *
 */
@FacesComponent(PortalFamily.SIDEBAR_COMPONENT)
@SuppressWarnings("squid:MaximumInheritanceDepth") // Artifact of Jsf-structure
public class SidebarComponent extends BaseCuiPanel implements StyleClassResolver {

    @Delegate
    private final HtmlElementProvider htmlElementProvider;

    /**
     * Default Constructor
     */
    public SidebarComponent() {
        htmlElementProvider = new HtmlElementProvider(this, Node.NAV);
        super.setRendererType(PortalFamily.SIDEBAR_RENDERER);
    }

    @Override
    public StyleClassBuilder resolveStyleClass() {
        return PortalCssClasses.SIDEBAR.getStyleClassBuilder().append(super.getStyleClass());
    }

    @Override
    public String getFamily() {
        return PortalFamily.PORTAL_FAMILY;
    }

    /**
     * Shortcut for creating and casting a component of type
     * {@link SidebarComponent}.
     *
     * @param facesContext
     * @return a newly created {@link SidebarComponent}
     */
    public static SidebarComponent createComponent(final FacesContext facesContext) {
        return ComponentUtility.createComponent(facesContext, PortalFamily.SIDEBAR_COMPONENT);
    }

}
