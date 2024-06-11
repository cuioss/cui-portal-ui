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
package de.cuioss.portal.ui.runtime.application.dashboard.support;

import jakarta.enterprise.context.Dependent;

import de.cuioss.portal.ui.api.dashboard.PortalDashboardWidget;
import de.cuioss.uimodel.nameprovider.IDisplayNameProvider;
import de.cuioss.uimodel.nameprovider.LabeledKey;

import java.io.Serial;

@PortalDashboardWidget
@Dependent
public class TestWidgetWithIdA extends AbstractTestWidget {

    @Serial
    private static final long serialVersionUID = -7473374640743772845L;

    @Override
    public String getId() {
        return "A";
    }

    @Override
    protected void doInit() {
    }

    @Override
    public LabeledKey getNoItemsMessage() {
        return null;
    }

    @Override
    public IDisplayNameProvider<?> getTitle() {
        return null;
    }
}
