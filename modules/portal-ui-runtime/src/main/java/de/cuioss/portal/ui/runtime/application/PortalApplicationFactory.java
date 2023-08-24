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
package de.cuioss.portal.ui.runtime.application;

import javax.faces.application.Application;
import javax.faces.application.ApplicationFactory;
import javax.faces.application.ApplicationWrapper;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Factory for creating / wrapping an existing {@link ApplicationFactory}
 *
 * @author Oliver Wolff
 */
@RequiredArgsConstructor
public class PortalApplicationFactory extends ApplicationFactory {

    @Getter
    private final ApplicationFactory wrapped;
    private Application application;

    /**
     * Returns an instance of {@link PortalApplication} which wraps the original
     * application.
     */
    @Override
    public Application getApplication() {
        return application == null ? createPortalApplication(wrapped.getApplication()) : application;
    }

    /**
     * Sets the given application instance as the current instance. If it's not an
     * instance of {@link PortalApplication}, nor wraps the
     * {@link PortalApplication}, then it will be wrapped by a new instance of
     * {@link PortalApplication}.
     */
    @Override
    public void setApplication(final Application application) {
        wrapped.setApplication(createPortalApplication(application));
    }

    /**
     * If the given application not an instance of {@link PortalApplication}, nor
     * wraps the {@link PortalApplication}, then it will be wrapped by a new
     * instance of {@link PortalApplication} and set as the current instance and
     * returned.
     */
    private Application createPortalApplication(final Application application) {
        synchronized (PortalApplicationFactory.class) {
            var toBeWrapped = application;
            while (!(toBeWrapped instanceof PortalApplication) && toBeWrapped instanceof ApplicationWrapper) {
                toBeWrapped = ((ApplicationWrapper) toBeWrapped).getWrapped();
            }

            if (!(toBeWrapped instanceof PortalApplication)) {
                toBeWrapped = new PortalApplication(toBeWrapped);
            }
            this.application = toBeWrapped;
            return this.application;
        }
    }

}
