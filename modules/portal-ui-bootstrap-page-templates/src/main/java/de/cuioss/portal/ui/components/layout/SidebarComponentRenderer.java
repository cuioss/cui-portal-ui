package de.cuioss.portal.ui.components.layout;

import java.io.IOException;

import javax.faces.context.FacesContext;
import javax.faces.render.FacesRenderer;
import javax.faces.render.Renderer;

import de.cuioss.jsf.api.components.renderer.BaseDecoratorRenderer;
import de.cuioss.jsf.api.components.renderer.DecoratingResponseWriter;
import de.cuioss.portal.ui.components.PortalFamily;

/**
 * {@link Renderer} for {@link SidebarComponent}
 *
 * @author Oliver Wolff
 */
@FacesRenderer(rendererType = PortalFamily.SIDEBAR_RENDERER, componentFamily = PortalFamily.PORTAL_FAMILY)
public class SidebarComponentRenderer extends BaseDecoratorRenderer<SidebarComponent> {

    /**
     */
    public SidebarComponentRenderer() {
        super(false);
    }

    @Override
    protected void doEncodeBegin(final FacesContext context, final DecoratingResponseWriter<SidebarComponent> writer,
            final SidebarComponent component) throws IOException {
        writer.withStartElement(component.resolveHtmlElement());
        writer.withStyleClass(component.resolveStyleClass());
        writer.withAttributeStyle(component.getStyle());
        writer.withClientIdIfNecessary();
        writer.withPassThroughAttributes();
    }

    @Override
    protected void doEncodeEnd(final FacesContext context, final DecoratingResponseWriter<SidebarComponent> writer,
            final SidebarComponent component) throws IOException {
        writer.withEndElement(component.resolveHtmlElement());
    }
}
