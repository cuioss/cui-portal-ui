/*
 * Copyright © 2025 CUI-OpenSource-Software (info@cuioss.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.cuioss.portal.ui.runtime.application;

import de.cuioss.tools.logging.CuiLogger;
import jakarta.faces.application.Application;
import jakarta.faces.application.ApplicationFactory;
import jakarta.faces.application.ApplicationWrapper;

/**
 * Factory for creating / wrapping an existing {@link ApplicationFactory}
 *
 * @author Oliver Wolff
 */
public class PortalApplicationFactory extends ApplicationFactory {

    private static final CuiLogger LOGGER = new CuiLogger(PortalApplicationFactory.class);
    @SuppressWarnings("java:S3077") // thread-safe via createPortalApplication
    private Application application;

    /**
     * Construct a new OPortalApplicationFactory around the given wrapped factory.
     *
     * @param wrapped The wrapped factory.
     */
    public PortalApplicationFactory(ApplicationFactory wrapped) {
        super(wrapped);
    }

    /**
     * Returns an instance of {@link PortalApplication} which wraps the original
     * application.
     */
    @Override
    public Application getApplication() {
        return application == null ? createPortalApplication(getWrapped().getApplication()) : application;
    }

    /**
     * Sets the given application instance as the current instance. If it's not an
     * instance of {@link PortalApplication}, nor wraps the
     * {@link PortalApplication}, then it will be wrapped by a new instance of
     * {@link PortalApplication}.
     */
    @Override
    public void setApplication(final Application application) {
        getWrapped().setApplication(createPortalApplication(application));
    }

    /**
     * If the given application not an instance of {@link PortalApplication}, nor
     * wraps the {@link PortalApplication}, then it will be wrapped by a new
     * instance of {@link PortalApplication} and set as the current instance and
     * returned.
     */
    private Application createPortalApplication(final Application application) {
        synchronized (PortalApplicationFactory.class) {
            LOGGER.debug("Initializing with given application: %s", application.getClass().getName());
            var toBeWrapped = application;
            while (!(toBeWrapped instanceof PortalApplication) && toBeWrapped instanceof ApplicationWrapper) {
                LOGGER.debug("Found wrapped application: %s", toBeWrapped.getClass().getName());
                toBeWrapped = ((ApplicationWrapper) toBeWrapped).getWrapped();
            }

            if (!(toBeWrapped instanceof PortalApplication)) {
                LOGGER.debug("Wrapping application: %s", application.getClass().getName());
                toBeWrapped = new PortalApplication(application);
            }
            this.application = toBeWrapped;
            return this.application;
        }
    }

}
