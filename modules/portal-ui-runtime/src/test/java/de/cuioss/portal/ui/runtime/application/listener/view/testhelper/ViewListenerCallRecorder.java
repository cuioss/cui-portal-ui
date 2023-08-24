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
package de.cuioss.portal.ui.runtime.application.listener.view.testhelper;

import de.cuioss.jsf.api.common.view.ViewDescriptor;
import de.cuioss.portal.ui.api.listener.view.ViewListener;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@EqualsAndHashCode
@ToString
public class ViewListenerCallRecorder implements ViewListener {

    private static final long serialVersionUID = -7770258964826877169L;

    @Getter
    private ViewDescriptor handledView;

    @Override
    public void handleView(final ViewDescriptor viewId) {
        this.handledView = viewId;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
