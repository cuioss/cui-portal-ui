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
package de.cuioss.portal.ui.runtime.application.resources;

import de.cuioss.tools.string.MoreStrings;
import de.cuioss.tools.string.Splitter;
import jakarta.faces.application.Resource;
import jakarta.faces.application.ResourceHandler;
import jakarta.faces.context.FacesContext;
import org.omnifaces.resourcehandler.UnmappedResourceHandler;
import org.omnifaces.util.FacesLocal;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import static de.cuioss.tools.collect.CollectionLiterals.immutableList;
import static java.util.stream.Collectors.toList;

/**
 * This resource handler wrap omnifaces UnmappedResourceHandler for restriction
 * of handling only harmless resources. Blacklist could be adapted by add
 * context-param to web.xml (or fragment), this is the common JSF configuration
 * parameter
 * <p>
 * <code>
 * &lt;context-param&gt;<br>
 * &lt;param-name&gt;jakarta.faces.RESOURCE_EXCLUDES&lt;/param-name&gt;<br>
 * &lt;param-value&gt;.class .jsp .jspx .properties .xhtml .groovy&lt;/param-value&gt;<br>
 * &lt;/context-param&gt;
 * </code>
 * </p>
 * <p>
 * TODO : could be replaced by original after
 * <a href="https://github.com/omnifaces/omnifaces/issues/481">...</a> is solved.
 *
 * @author Eugen Fischer
 */
public class CuiUnmappedResourceHandler extends UnmappedResourceHandler {

    private static final List<Pattern> EXCLUDE_RESOURCES = initExclusionPatterns();

    /**
     * Creates a new instance of this unmapped resource handler which wraps the
     * given resource handler.
     *
     * @param wrapped The resource handler to be wrapped.
     */
    public CuiUnmappedResourceHandler(final ResourceHandler wrapped) {
        super(wrapped);
    }

    private static boolean shouldBeHandledHere(final String requestURI) {
        return !isExcluded(requestURI);
    }

    private static boolean isExcluded(final String resourceId) {
        for (final Pattern pattern : EXCLUDE_RESOURCES) {
            if (pattern.matcher(resourceId).matches())
                return true;
        }
        return false;
    }

    private static List<Pattern> initExclusionPatterns() {
        return immutableList(
                configuredExclusions().stream().map(pattern -> Pattern.compile(".*\\" + pattern)).collect(toList()));
    }

    /**
     * Lookup web.xml context-param
     * {@link jakarta.faces.application.ResourceHandler#RESOURCE_EXCLUDES_PARAM_NAME}.<br>
     * If exists use as space separated configuration list,<br>
     * otherwise fallback to
     * {@link jakarta.faces.application.ResourceHandler#RESOURCE_EXCLUDES_DEFAULT_VALUE}
     * FIXME owolff: Load from microprofile-config
     */
    private static List<String> configuredExclusions() {
        final var exclusions = Optional.ofNullable(getContextParameter())
                .orElse(ResourceHandler.RESOURCE_EXCLUDES_DEFAULT_VALUE);
        return Splitter.on(' ').omitEmptyStrings().trimResults().splitToList(exclusions);
    }

    private static String getContextParameter() {
        return MoreStrings.emptyToNull(FacesContext.getCurrentInstance().getExternalContext()
                .getInitParameter(ResourceHandler.RESOURCE_EXCLUDES_PARAM_NAME));
    }

    @Override
    public Resource decorateResource(final Resource resource) {

        if (null != resource && shouldBeHandledHere(resource.getRequestPath()))
            return super.decorateResource(resource);

        return resource;
    }

    @Override
    public boolean isResourceRequest(final FacesContext context) {
        final var requestURI = FacesLocal.getRequestURI(context);
        final var requestContextPath = FacesLocal.getRequestContextPath(context);
        final var isResourceRequest = requestURI.startsWith(requestContextPath + RESOURCE_IDENTIFIER);
        if (isResourceRequest) {
            context.getAttributes().put("com.sun.faces.RESOURCE_REQUEST", true);
        }
        return isResourceRequest && shouldBeHandledHere(requestURI)
                // wrapped should decide not super !
                || getWrapped().isResourceRequest(context);
    }

    @Override
    public void handleResourceRequest(final FacesContext context) throws IOException {
        final var requestURI = FacesLocal.getRequestURI(context);
        if (shouldBeHandledHere(requestURI)) {
            super.handleResourceRequest(context);
        } else {
            getWrapped().handleResourceRequest(context);
        }
    }
}
