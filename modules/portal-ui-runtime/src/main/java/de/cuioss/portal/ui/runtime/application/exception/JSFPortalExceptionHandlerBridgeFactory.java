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
package de.cuioss.portal.ui.runtime.application.exception;

import jakarta.faces.context.ExceptionHandler;
import jakarta.faces.context.ExceptionHandlerFactory;

import de.cuioss.tools.logging.CuiLogger;

/**
 * Factory for creating instances of {@link JSFPortalExceptionHandlerBridge}
 */
public class JSFPortalExceptionHandlerBridgeFactory extends ExceptionHandlerFactory {

    private static final CuiLogger LOGGER = new CuiLogger(JSFPortalExceptionHandlerBridgeFactory.class);

    public JSFPortalExceptionHandlerBridgeFactory(ExceptionHandlerFactory parent) {
        super(parent);
        LOGGER.debug("Creating JSFPortalExceptionHandlerBridgeFactory with parent = '%s'", parent);
    }

    @Override
    public ExceptionHandler getExceptionHandler() {
        return new JSFPortalExceptionHandlerBridge(super.getWrapped().getExceptionHandler());
    }

}
