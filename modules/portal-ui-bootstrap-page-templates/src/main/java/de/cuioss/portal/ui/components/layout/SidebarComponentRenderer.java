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
package de.cuioss.portal.ui.components.layout;

import de.cuioss.jsf.api.components.renderer.BaseDecoratorRenderer;
import de.cuioss.jsf.api.components.renderer.DecoratingResponseWriter;
import de.cuioss.portal.ui.components.PortalFamily;
import jakarta.faces.context.FacesContext;
import jakarta.faces.render.FacesRenderer;
import jakarta.faces.render.Renderer;

import java.io.IOException;

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
