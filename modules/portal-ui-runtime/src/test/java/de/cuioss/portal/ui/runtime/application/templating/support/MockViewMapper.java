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
package de.cuioss.portal.ui.runtime.application.templating.support;

import java.net.URL;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;

import de.cuioss.portal.ui.api.templating.MultiViewMapper;
import de.cuioss.portal.ui.api.templating.PortalMultiViewMapper;
import de.cuioss.tools.io.FileLoaderUtility;
import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("javadoc")
@PortalMultiViewMapper
@Alternative
@ApplicationScoped
public class MockViewMapper implements MultiViewMapper {

    private static final long serialVersionUID = -155012364802045126L;

    @Getter
    @Setter
    private String basePath;

    @Override
    public URL resolveViewPath(final String requestedResource) {
        return FileLoaderUtility.getLoaderForPath(basePath + requestedResource).getURL();
    }

}
