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
package de.cuioss.portal.ui.runtime.application.listener.afterconfiguration;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import de.cuioss.portal.configuration.initializer.ApplicationInitializer;
import de.cuioss.portal.configuration.initializer.PortalInitializer;
import de.cuioss.portal.ui.api.templating.MultiViewMapper;
import de.cuioss.portal.ui.api.templating.PortalMultiViewMapper;

/**
 * See package javadoc for details
 *
 * @author Oliver Wolff
 */
@PortalInitializer
@Dependent
public class ElAccessibleObjectInitializer implements ApplicationInitializer {

    @Inject
    @PortalMultiViewMapper
    private MultiViewMapper multiViewMapper;

    @Override
    public void initialize() {
        multiViewMapper.toString();
    }

    @Override
    public Integer getOrder() {
        return ApplicationInitializer.ORDER_LATE;
    }
}
